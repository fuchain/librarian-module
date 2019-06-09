<template>
  <vx-card>
    <h2 class="mb-4">Mã QR của tôi</h2>
    <canvas id="canvas"></canvas>
    <p>Quét mã QR để định danh người dùng</p>
  </vx-card>
</template>

<script>
import QRCode from "qrcode";

export default {
  computed: {
    email() {
      return this.$store.state.email;
    },
    coin() {
      return this.$store.state.coin;
    }
  },
  async mounted() {
    await this.$store.dispatch("getProfile");

    const canvas = document.getElementById("canvas");

    QRCode.toCanvas(
      canvas,
      `https://library.fptu.tech/wallet?email=${this.email}`,
      { width: 250 },
      function(error) {
        if (error) console.error(error);
      }
    );
  }
};
</script>
