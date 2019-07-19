<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">
      Sách đang trả
      <vs-button color="primary" type="relief" size="small" class="ml-4" @click="callReload">Làm mới</vs-button>
    </h2>
    <vs-input
      size="large"
      icon="search"
      class="w-full mb-4"
      placeholder="Tìm tên sách"
      v-model="searchText"
    />
    <div v-if="books.length && needTab()">
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
        <book-card :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
                v-if="item.user"
                @click="beginConfirm(item)"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">XÁC NHẬN</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                v-if="item.user"
                @click="triggerCall(item.user)"
              >
                <feather-icon icon="PhoneIncomingIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">LIÊN LẠC</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                v-if="!item.user"
                @click="doCancel(item)"
              >
                <feather-icon icon="XIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">HỦY BỎ VIỆC TRẢ SÁCH</span>
              </div>
            </div>
          </template>
        </book-card>
      </div>
    </div>

    <vx-card class="mt-6" title="Bạn đang không trả sách nào." v-else>
      <vs-button @click="$router.push('/books/keeping')" icon="book">Trả sách</vs-button>
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
import BookCard from "@/views/components/BookCard.vue";

export default {
  components: {
    BookCard
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
      const matched = this.books.filter(e => e.matched);
      if (!matched.length) {
        return true;
      }

      return false;
    },
    isMatchingNull() {
      const matching = this.books.filter(e => !e.matched);
      if (!matching.length) {
        return true;
      }

      return false;
    },
    listBooks() {
      if (!this.needTab()) return this.books;

      const showMatchedBooks = this.showMatched
        ? this.books.filter(e => e.matched)
        : this.books.filter(e => !e.matched);

      if (!this.searchText.trim()) return showMatchedBooks;

      return showMatchedBooks.filter(e =>
        e.name.toLowerCase().includes(this.searchText.trim().toLowerCase())
      );
    }
  },
  methods: {
    triggerCall(user) {
      if (!user) return;
      window.location.href = `tel:${user.phone}`;
    },
    async beginConfirm(item) {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/transfer/create`, {
          to: {
            email: item.user
          },
          asset_id: item.id
        })
        .then(response => {
          const tx = response.data;

          this.$store.dispatch("openTxPopup", tx);
        })
        .catch(() => {
          this.$vs.notify({
            title: "Thất bại",
            text: "Có lỗi xảy ra, vui lòng thử lại",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    callReload() {
      this.$emit("doReload");
    },
    needTab() {
      const needArr = [true, false];
      const statusArr = this.books.map(e => {
        return e.matched;
      });

      const needTab = needArr.every(i => statusArr.includes(i));
      return needTab;
    },
    doCancel(item) {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/matching/cancel_request`, {
          book_detail_id: item.bookDetailId,
          book_id: item.id
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Đã gửi yêu cầu hủy bỏ việc trả sách thành công",
            color: "primary",
            position: "top-center"
          });

          this.$store.dispatch("getNumOfBooks");

          this.$router.push("/books/keeping");
        })
        .catch(e => {
          this.$vs.notify({
            title: "Lỗi",
            text: "Chưa thể hủy bỏ việc trả sách",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
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
