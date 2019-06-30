<template>
  <vx-card class="md:w-1/2 w-full mb-base" title="Gửi thông báo cho người mượn sách">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Email người nhận</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="email" />
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Nội dung</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-textarea v-model="message" />
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Kiểu</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="type" />
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button class="mr-3 mb-2" icon="check" @click="submit">Gửi thông báo đẩy và email</vs-button>
      </div>
    </div>
  </vx-card>
</template>

<script>
export default {
  data() {
    return {
      email: "",
      message: "",
      type: ""
    };
  },
  methods: {
    submit() {
      if (!this.email || !this.message) {
        this.$vs.notify({
          title: "Lỗi",
          text: "Bạn phải nhập email và nội dung thông báo",
          color: "warning",
          position: "top-center"
        });

        return;
      }

      this.$vs.loading();
      this.$http
        .post(`${this.$http.nodeUrl}/notifications/push`, {
          email: this.email,
          message: this.message,
          type: this.type
        })
        .then(response => {
          const status = response.status;
          if (status === 200) {
            this.$vs.notify({
              title: "Đã gửi",
              text:
                "Đã gửi tin nhắn cho người dùng, hiện tại người dùng này không online",
              color: "success",
              position: "top-center"
            });
          } else if (status === 201) {
            this.$vs.notify({
              title: "Đã gửi",
              text:
                "Đã gửi tin nhắn cho người dùng, hiện tại người dùng này đang online",
              color: "success",
              position: "top-center"
            });
          }
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
  }
};
</script>
