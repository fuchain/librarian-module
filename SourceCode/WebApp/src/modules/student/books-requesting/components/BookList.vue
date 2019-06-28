<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">
      Sách đang yêu cầu
      <vs-button color="primary" type="relief" size="small" class="ml-4" @click="callReload">Làm mới</vs-button>
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
        <book-card :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
                v-if="item.user"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="beginConfirm(item)">XÁC NHẬN</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="XIcon" svgClasses="h-4 w-4" v-if="!item.user"/>
                <feather-icon icon="PhoneIncomingIcon" svgClasses="h-4 w-4" v-if="item.user"/>

                <span
                  class="text-sm font-semibold ml-2"
                  @click="triggerCall(item)"
                >{{ item.user ? "LIÊN LẠC" : "HỦY BỎ VIỆC YÊU CẦU SÁCH" }}</span>
              </div>
            </div>
          </template>
        </book-card>
      </div>
    </div>

    <vx-card class="mt-6" title="Bạn đang không yêu cầu mượn sách nào." v-else>
      <vs-button @click="$router.push('/books/request')">Yêu cầu mượn sách</vs-button>
    </vx-card>

    <vs-popup title="Xác nhận" :active.sync="confirmPopup">
      <div class="mb-4">
        <vs-input
          size="large"
          class="w-full"
          placeholder="Xác nhận mã PIN gồm 6 chữ số"
          v-model="pin"
        />
      </div>
      <div>
        <vs-button
          class="w-full"
          @click="validateConfirm"
          :disabled="pin.trim().length !== 6"
        >Đồng ý nhận sách</vs-button>
      </div>
      <vs-divider>Hoặc</vs-divider>
      <div class="mt-2">
        <vs-button
          color="danger"
          class="w-full"
          @click="doReject"
          :disabled="pin.trim().length > 0"
        >Từ chối nhận sách</vs-button>
      </div>
    </vs-popup>

    <vs-popup title="Từ chối nhận sách" :active.sync="rejectPopup">
      <p>Vui lòng nhập lí do từ chối nhận sách và xác nhận từ chối nhận sách:</p>
      <div class="mt-2">
        <vs-textarea label="Lí do từ chối nhận sách" v-model="reason"></vs-textarea>
      </div>
      <div class="mt-2">
        <vs-upload
          :action="$http.baseUrl + '/transaction/upload' "
          fileName="file"
          :headers="uploadHeader"
          @on-success="successUpload"
          @on-error="failUpload"
          text="Up ảnh bằng chứng"
          limit="1"
          :automatic="true"
        />
      </div>
      <div class="mt-2">
        <vs-button
          color="danger"
          class="w-full"
          @click="confirmReject"
          :disabled="!reason.trim() || !imageUrl"
        >Từ chối nhận sách</vs-button>
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
      confirmPopup: false,
      rejectPopup: false,
      pin: "",
      reason: "",
      requestId: 0,
      matchingId: 0,
      showMatched: true,
      searchText: "",
      // Reject
      imageUrl: ""
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
      const showMatchedBooks = this.showMatched
        ? this.books.filter(e => e.status === 2)
        : this.books.filter(e => e.status === 1);

      if (!this.searchText.trim()) return showMatchedBooks;

      return showMatchedBooks.filter(e =>
        e.name.toLowerCase().includes(this.searchText.trim().toLowerCase())
      );
    },
    uploadHeader() {
      return {
        Authorization: `Bearer ${this.$auth.getAccessToken()}`
      };
    }
  },
  methods: {
    triggerCall(item) {
      if (!item.user) {
        this.$vs.loading();

        this.$http
          .put(`${this.$http.baseUrl}/requests/cancel`, {
            request_id: item.requestId
          })
          .then(() => {
            this.$vs.notify({
              title: "Thành công",
              text: "Huỷ bỏ yêu cầu nhận sách thành công",
              color: "primary",
              position: "top-center"
            });

            this.$emit("doReload");
          })
          .catch(() => {
            this.$vs.notify({
              title: "Lỗi",
              text: "Yêu cầu hủy bỏ không hợp lệ",
              color: "warning",
              position: "top-center"
            });
          })
          .finally(() => {
            this.$vs.loading.close();
          });
      } else {
        window.location.href = `tel:${item.user.phone}`;
      }
    },
    async beginConfirm(item) {
      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/requests/${item.requestId}/matched`)
        .then(response => {
          const matchingId = response.data.matching_id;
          this.matchingId = matchingId;
          this.requestId = item.requestId;
          this.confirmPopup = true;
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    async validateConfirm() {
      this.$vs.loading();
      this.$http
        .put(`${this.$http.baseUrl}/requests/transfer`, {
          type: 2,
          matchingId: this.matchingId,
          pin: this.pin
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Nhận sách thành công",
            color: "primary",
            position: "top-center"
          });

          this.confirmPopup = false;
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

          this.$vs.notify({
            title: "Lỗi",
            text: "Mã PIN không hợp lệ",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    doReject() {
      this.confirmPopup = false;
      this.rejectPopup = true;
    },
    async confirmReject() {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/requests/reject`, {
          image_url: this.imageUrl,
          matching_id: this.matchingId,
          reason: this.reason
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Từ chối nhận sách thành công",
            color: "primary",
            position: "top-center"
          });

          this.rejectPopup = false;
          this.reason = "";
          this.imageUrl = "";

          this.callReload();
        })
        .catch(err => {
          // Catch error
          console.log(err);

          this.$vs.notify({
            title: "Lỗi",
            text: "Có lỗi xảy ra, chưa thể từ chối nhận sách",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    successUpload(e) {
      const response = JSON.parse(e.target.response);
      const url = response.url;

      this.imageUrl = url;
    },
    failUpload(e) {
      console.log(e);
    },
    callReload() {
      this.$emit("doReload");
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
