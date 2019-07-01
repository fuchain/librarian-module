<template>
  <vx-card class="md:w-1/2 w-full mb-base" title="Lập lịch hệ thống">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Kích hoạt lập lịch</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-switch v-model="enable" @click="doUpdate">
          <span slot="on">Đang chạy</span>
          <span slot="off">Đang tắt</span>
        </vs-switch>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Chạy định kì</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" value="Mỗi giờ chạy một lần" disabled="true" />
      </div>
    </div>
  </vx-card>
</template>

<script>
export default {
  data() {
    return {
      enable: true,
      intervalExpression: "0 0 0/1 1/1 * ? *"
    };
  },
  methods: {
    doUpdate() {
      this.$http
        .put(`${this.$http.baseUrl}/librarian/scheduler/enable`, {
          enable: !this.enable,
          interval_expression: this.intervalExpression
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Thay đổi của bạn đã được áp dụng",
            color: "success",
            position: "top-center"
          });
        })
        .catch(() => {
          this.$vs.notify({
            title: "Lỗi",
            text: "Thay đổi của bạn chưa được áp dụng",
            color: "warning",
            position: "top-center"
          });
        });
    }
  },
  mounted() {
    this.$http
      .get(`${this.$http.baseUrl}/librarian/scheduler/status`)
      .then(response => {
        const data = response.data;

        this.enable = data.enable;
      });
  }
};
</script>
