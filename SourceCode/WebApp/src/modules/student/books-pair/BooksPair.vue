<template>
  <div>
    <vx-card title="Nhập mã PIN xác nhận">
      <div class="mb-4">
        <vs-input size="large" class="w-full" placeholder="Mã PIN xác nhận" v-model="pin"/>
      </div>
      <div>
        <vs-button class="w-full" @click="confirm" icon="done">Đã nhận sách</vs-button>
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
    async confirm() {
      await this.fakeLoad(3000);

      this.$vs.notify({
        title: "Lỗi",
        text: "Mã PIN không hợp lệ",
        color: "warning",
        position: "top-center"
      });
    }
  }
};
</script>
