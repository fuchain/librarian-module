<template>
  <div>
    <vx-card>
      <h4 class="mb-4">Nhập mã PIN xác nhận</h4>
      <div class="mb-4">
        <vs-input size="large" class="w-full" placeholder="Mã PIN xác nhận" v-model="pin"/>
      </div>
      <div>
        <vs-button class="w-full" @click="verify" icon="done">Xác nhận đã nhận sách</vs-button>
      </div>
    </vx-card>

    <vs-popup title="Xác nhận nhận sách" :active.sync="verifyPopup">
      <div class="flex items-center justify-center">
        <book-card :item="book" style="max-width: 300px;">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="doneVerify">XÁC NHẬN NHẬN SÁCH</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="BookOpenIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2">CHI TIẾT</span>
              </div>
            </div>
          </template>
        </book-card>
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
      pin: "",
      verifyPopup: false,
      book: {}
    };
  },
  methods: {
    async fakeLoad(customTime) {
      return new Promise((resolve, reject) => {
        this.$vs.loading();
        setTimeout(
          function() {
            this.$vs.loading.close();
            resolve();
          }.bind(this),
          customTime || 500
        );
      });
    },
    async verify() {
      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/requests/manually/verify?pin=${this.pin}`)
        .then(response => {
          const data = response.data;

          // Log
          this.book = {
            name: data.bookDetail.name,
            description: data.bookDetail.description,
            image: data.bookDetail.thumbnail,
            code:
              (data.bookDetail.parseedSubjectCode &&
                data.bookDetail.parseedSubjectCode.length &&
                data.bookDetail.parseedSubjectCode[0]) ||
              "N/A",
            status: data.status
          };
          this.verifyPopup = true;
        })
        .catch(e => {
          // Catch error
          console.log(e);

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
    doneVerify() {
      this.verifyPopup = false;
      this.confirm();
    },
    async confirm() {
      this.$vs.loading();

      this.$http
        .put(`${this.$http.baseUrl}/requests/manually/confirm`, {
          pin: this.pin
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Đã nhận được sách",
            color: "primary",
            position: "top-center"
          });

          this.$store.dispatch("getNumOfBooks");
          this.$router.push("/books/keeping");
        })
        .catch(e => {
          // Catch error
          console.log(e);

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
    }
  }
};
</script>

<style lang="css" scoped>
.vx-card {
  max-width: 650px;
}

.vs-popup {
  width: 100px;
}
</style>
