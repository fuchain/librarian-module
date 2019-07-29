<template>
  <QRScan @printCode="notify" />
</template>

<script>
import QRScan from "@/views/components/QRScan.vue";

export default {
  components: {
    QRScan
  },
  methods: {
    notify(val) {
      console.log(val);
    }
  }
};
</script>
