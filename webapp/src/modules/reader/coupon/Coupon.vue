<template>
  <div>
    <vx-card style="max-width: 400px;">
      <h4 class="mb-4">Nhập mã coupon nhận sách</h4>
      <div class="mb-4">
        <vs-input
          v-on:keyup.enter="submit"
          size="large"
          class="w-full"
          placeholder="Coupon"
          v-model="coupon"
          autofocus
        />
      </div>
      <div>
        <vs-button class="w-full" @click="submit" icon="done">Nhận sách</vs-button>
      </div>
    </vx-card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      coupon: "FUCHAIN"
    };
  },
  methods: {
    submit() {
      this.$vs.loading({
        type: "radius",
        background: "danger",
        color: "white"
      });

      this.$http
        .post(`${this.$http.baseUrl}/transfer/test`, {
          coupon: this.coupon || "null"
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Đã sử dụng coupon",
            color: "primary",
            position: "top-center"
          });
        })
        .catch(() => {
          this.$vs.notify({
            title: "Thất bại",
            text: "Coupon không hợp lệ hoặc bạn đã sử dụng quá nhiều",
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
