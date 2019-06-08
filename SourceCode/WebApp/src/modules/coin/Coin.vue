<template>
  <vx-card>
    <h2 class="mb-4">Ví của bạn</h2>
    <p>Tính năng này sắp ra mắt. Chúng tôi sẽ thông báo cho bạn khi tính năng này sẵn sàng.</p>
    <vs-divider>Bản Beta</vs-divider>
    <p class="mt-2">
      Email:
      <strong>{{ email }}</strong>
    </p>
    <p class="mt-2">
      FUCoin:
      <strong>{{ coin }}</strong>
    </p>
    <canvas id="canvas"></canvas>
    <p>Quét mã QR để thanh toán bằng ví này</p>
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
