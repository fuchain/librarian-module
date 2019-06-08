<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">
      Sách đang trả
      <vs-button
        color="primary"
        type="relief"
        size="small"
        class="ml-4"
        @click="$router.go()"
      >Làm mới</vs-button>
    </h2>
    <vs-input
      size="large"
      icon="search"
      class="w-full mb-4"
      placeholder="Tìm tên sách"
      v-model="searchText"
    />
    <div>
      <vs-tabs alignment="fixed">
        <vs-tab label="Đã ghép" icon="check" @click="showMatched = true">
          <div>{{ isMatchedNull ? "Chưa có quyển sách nào được ghép." : "" }}</div>
        </vs-tab>
        <vs-tab label="Đang ghép" icon="refresh" @click="showMatched = false">
          <div>{{ isMatchingNull ? "Không có quyển sách nào đang ghép." : "" }}</div>
        </vs-tab>
      </vs-tabs>
    </div>
    <div class="items-grid-view vx-row match-height" v-if="books.length" appear>
      <div
        class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
        v-for="item in listBooks"
        :key="item.id"
      >
        <item-grid-view :item="item" v-if="(showMatched ? item.status === 2 : item.status === 1)">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
                v-if="item.user"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="beginConfirm(item)">ĐÃ TRẢ</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                v-if="item.user"
              >
                <feather-icon icon="PhoneIncomingIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="triggerCall(item.user)">LIÊN LẠC</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                v-if="!item.user"
              >
                <feather-icon icon="ArchiveIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2">CHƯA CÓ NGƯỜI NHẬN</span>
              </div>
            </div>
          </template>
        </item-grid-view>
      </div>
    </div>

    <vx-card class="mt-6" title="Bạn đang không trả sách nào." v-else>
      <vs-button @click="$router.push('/books/keeping')">Trả sách</vs-button>
    </vx-card>

    <vs-popup title="Người nhận xác nhận" :active.sync="popupActive">
      <div style="font-size: 1.5rem; text-align: center;">Mã số PIN xác nhận</div>
      <div style="font-size: 3rem; text-align: center;">{{ randomPIN }}</div>
      <div style="text-align: center; margin-bottom: 1rem;">
        Chỉ tồn tại trong
        <strong>{{ remainTime }} giây</strong>
      </div>
    </vs-popup>
  </div>
</template>

<script>
const ItemGridView = () => import("./ItemGridView.vue");

let countInterval;

export default {
  components: {
    ItemGridView
  },
  data() {
    return {
      popupActive: false,
      randomPIN: 0,
      remainTime: 0,
      matchingId: 0,
      showMatched: true,
      searchText: ""
    };
  },
  props: {
    books: {
      type: Array
    }
  },
  computed: {
    isMatchedNull() {
      const matched = this.books.filter(e => e.status !== 1);
      if (!matched.length) {
        return true;
      }

      return false;
    },
    isMatchingNull() {
      const matched = this.books.filter(e => e.status === 1);
      if (!matched.length) {
        return true;
      }

      return false;
    },
    listBooks() {
      if (!this.searchText.trim()) return this.books;

      return this.books.filter(e =>
        e.name.toLowerCase().includes(this.searchText.trim().toLowerCase())
      );
    }
  },
  watch: {
    popupActive(val) {
      if (val === false && countInterval) {
        clearInterval(countInterval);
      }
    }
  },
  methods: {
    triggerCall(user) {
      if (!user) return;
      window.location.href = `tel:${user.phone}`;
    },
    async beginConfirm(request) {
      this.$vs.loading();
      this.$http
        .get(`${this.$http.baseUrl}/requests/${request.requestId}/matched`)
        .then(response => {
          const matchingId = response.data.matching_id;
          this.matchingId = matchingId;
          this.$http
            .put(`${this.$http.baseUrl}/requests/transfer`, {
              type: 1,
              matchingId
            })
            .then(response => {
              const data = response.data;

              const { pin } = data;

              this.randomPIN = pin;
              this.startCount();
              this.popupActive = true;
              this.$vs.loading.close();
            });
        });
    },
    async validateConfirm() {
      this.$http
        .get(`${this.$http.baseUrl}/matchings/${this.matchingId}/confirm`)
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Người nhận đã xác nhận mã PIN",
            color: "primary",
            position: "top-center"
          });

          this.popupActive = false;
          this.$store.dispatch("getNumOfBooks");

          setTimeout(
            function() {
              this.$router.push("/books/keeping");
            }.bind(this),
            500
          );
        })
        .catch(err => {
          // Catch
          console.log(err);
        })
        .finally(() => {});
    },
    startCount() {
      this.remainTime = 300;
      clearInterval(countInterval);

      countInterval = setInterval(
        function() {
          this.remainTime = this.remainTime - 1;
          this.validateConfirm();

          if (this.remainTime <= 0) {
            this.$vs.notify({
              title: "Lỗi",
              text: "Hết hạn xác nhận mã PIN, vui lòng thao tác lại từ đầu",
              color: "warning",
              position: "top-center"
            });

            clearInterval(countInterval);
          }
        }.bind(this),
        1000
      );
    }
  },
  beforeDestroy() {
    clearInterval(countInterval);
  }
};
</script>

<style lang="scss" scoped>
#ecommerce-wishlist-demo {
  .item-view-primary-action-btn {
    color: #2c2c2c !important;
    background-color: #f6f6f6;
    min-width: 50%;
  }

  .item-view-secondary-action-btn {
    min-width: 50%;
  }
}
</style>
