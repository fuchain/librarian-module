<template>
  <vx-card title="Quét mã QR">
    <QRScan v-if="!error" @printCode="notify" @onFail="handleFail" />
    <p v-else>Bạn phải cho phép ứng dụng sử dụng máy ảnh.</p>
  </vx-card>
</template>

<script>
import QRScan from "@/views/components/QRScan.vue";

export default {
  components: {
    QRScan
  },
  data() {
    return {
      error: false
    };
  },
  methods: {
    notify(assetId) {
      this.$http
        .post(`${this.$http.baseUrl}/user/getbook`, { asset_id: assetId })
        .then(response => {
          const { book_detail, email } = response.data;
          this.$store.dispatch("openDetailsPopup", book_detail);
          this.$store.dispatch("setAssetData", {
            assetId: assetId,
            email
          });
        });
    },
    handleFail(val) {
      if (val) {
        this.error = true;
      }
    }
  }
};
</script>
