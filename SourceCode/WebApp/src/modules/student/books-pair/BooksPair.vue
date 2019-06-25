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
      <div class="mb-4">
        Bạn có chắc muốn nhận quyển sách
        <strong>{{ bookName }}</strong>
        (ID {{ bookID }}) không?
      </div>
      <div>
        <vs-button class="w-full" icon="check" @click="doneVerify">Xác nhận</vs-button>
      </div>
    </vs-popup>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pin: "",
      verifyPopup: false,
      bookName: "",
      bookID: 0
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
          this.bookName = data.bookDetail.name;
          this.bookID = data.id;
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

<style lang="scss" scoped>
.vx-card {
  max-width: 650px;
  min-height: 200px;
  background: linear-gradient(
      120deg,
      rgba(109, 213, 237, 0.8),
      rgba(33, 147, 176, 0.5)
    ),
    url("https://i.imgur.com/g80iDn3.jpg");
  background-repeat: no-repeat;
  color: black;

  h4 {
    color: white;
    font-weight: 500;
  }
}
</style>
