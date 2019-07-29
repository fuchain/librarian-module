<template>
  <vx-card>
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
    notify(val) {
      console.log(val);
    },
    handleFail(val) {
      if (val) {
        this.error = true;
      }
    }
  }
};
</script>
