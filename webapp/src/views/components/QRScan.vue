<template>
  <div>
    <qrcode-stream v-if="!error" @decode="onDecode" @init="onInit"></qrcode-stream>
  </div>
</template>

<script>
import { QrcodeStream } from "vue-qrcode-reader";

export default {
  data() {
    return {
      error: false
    };
  },
  components: {
    QrcodeStream
  },
  methods: {
    async onInit(promise) {
      try {
        await promise;
      } catch (error) {
        this.error = true;
        this.$emit("onFail", true);
        if (error.name === "NotAllowedError") {
          this.notifyError("Bạn phải cho phép dùng camera");
        } else if (error.name === "NotFoundError") {
          this.notifyError("Không có camera trên thiết bị");
        } else if (error.name === "NotSupportedError") {
          this.notifyError("Kết nối không an toàn");
        } else if (error.name === "NotReadableError") {
          this.notifyError("Camera đang được tiến trình khác dùng");
        } else if (error.name === "OverconstrainedError") {
          this.notifyError("Camera không phù hợp");
        } else if (error.name === "StreamApiNotSupportedError") {
          this.notifyError("Không hỗ trợ camera trên thiết bị");
        }
      }
    },
    onDecode(decodedString) {
      this.$emit("printCode", decodedString);
    },
    notifyError(msg) {
      this.$vs.notify({
        title: "Lỗi khi dùng camera",
        text: msg,
        color: "danger",
        position: "top-center",
        fixed: true
      });
    }
  }
};
</script>
