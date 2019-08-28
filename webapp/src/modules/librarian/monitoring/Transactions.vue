<template>
  <vx-card title="Số giao dịch trong mạng FUChain">
    <vue-friendly-iframe
      src="https://stats.fuchain.fptu.tech"
      @load="onLoad"
      @document-load="onDocumentLoad"
    ></vue-friendly-iframe>
  </vx-card>
</template>

<style lang="css">
iframe {
  width: 100%;
  height: 70vh;
  border: 0;
}
</style>
