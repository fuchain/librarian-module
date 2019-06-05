<template>
  <div id="ecommerce-wishlist-demo" v-if="isMounted">
    <h2 class="mb-6">Sách đang trả</h2>
    <div class="items-grid-view vx-row match-height" v-if="listBooks.length" appear>
      <div
        class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
        v-for="item in listBooks"
        :key="item.id"
      >
        <item-grid-view :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
                v-if="item.user"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="beginConfirm">ĐÃ TRẢ</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                v-if="item.user"
              >
                <feather-icon icon="PhoneIncomingIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="triggerCall()">LIÊN LẠC</span>
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

    <vx-card title="Bạn đang không trả sách nào." v-else>
      <vs-button @click="$router.push('/books')">Trả sách</vs-button>
    </vx-card>

    <vs-popup title="Người nhận xác nhận" :active.sync="popupActive">
      <div style="font-size: 1.5rem; text-align: center;">Mã số PIN xác nhận</div>
      <div style="font-size: 3rem; text-align: center;">{{ randomPIN }}</div>
      <div style="text-align: center; margin-bottom: 1rem;">
        Chỉ tồn tại trong
        <strong>{{ remainTime }} giây</strong>
      </div>
      <div style="text-align: center;">
        <vs-button @click="validateConfirm">Đã xong</vs-button>
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
      isMounted: false,
      listBooks: [],
      popupActive: false,
      randomPIN: 0,
      remainTime: 0
    };
  },
  watch: {
    popupActive(val) {
      if (val === false && countInterval) {
        clearInterval(countInterval);
      }
    }
  },
  methods: {
    async fakeLoad() {
      return new Promise((resolve, reject) => {
        this.$vs.loading();
        setTimeout(
          function() {
            this.$vs.loading.close();
            resolve();
          }.bind(this),
          2000
        );
      });
    },
    triggerCall() {
      window.location.href = "tel:0796870446";
    },
    async beginConfirm() {
      await this.fakeLoad();

      this.randomPIN = Math.floor(100000 + Math.random() * 900000);
      this.startCount();
      this.popupActive = true;
    },
    async validateConfirm() {
      await this.fakeLoad();

      this.$vs.notify({
        title: "Lỗi",
        text: "Người nhận chưa xác nhận mã PIN",
        color: "warning",
        position: "top-center"
      });
    },
    startCount() {
      this.remainTime = 300;

      countInterval = setInterval(
        function() {
          this.remainTime = this.remainTime - 1;

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
  },
  mounted() {
    this.$vs.loading();

    this.$http
      .get(`${this.$http.baseUrl}/requests/get_list?type=2`)
      .then(response => {
        const data = response.data;

        const books = data.map(e => {
          return {
            id: e.book.id,
            name: e.book.bookDetail.name,
            description: `Book ${
              e.book.bookDetail.name
            } for Software Engineering learning at FPT University`,
            image: "https://i.imgur.com/2j6B1n5.jpg",
            code: e.book.bookDetail.name.substring(0, 3).toUpperCase() + "101",
            status: e.status
          };
        });

        this.listBooks = [].concat(books);
        this.$vs.loading.close();
        this.isMounted = true;
      });
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
