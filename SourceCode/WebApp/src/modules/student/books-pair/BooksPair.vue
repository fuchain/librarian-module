<template>
  <div>
    <vx-card title="Nhập mã PIN xác nhận">
      <div class="mb-4">
        <vs-input size="large" class="w-full" placeholder="Mã PIN xác nhận" v-model="pin"/>
      </div>
      <div>
        <vs-button class="w-full" @click="verify" icon="done">Xác nhận đã nhận sách</vs-button>
      </div>
    </vx-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pin: ""
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
        .put(`${this.$http.baseUrl}/requests/manually/confirm`, {
          pin: this.pin
        })
        .then(response => {
          const data = response.data;

          // Log
          console.log(data);
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
    async confirm() {
      this.$vs.loading();

      this.$http
        .put(`${this.$http.baseUrl}/requests/manually/verify`, {
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
