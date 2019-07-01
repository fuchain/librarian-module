<template>
  <vx-card title="Theo dõi mạng BigchainDB">
    <vue-friendly-iframe
      src="https://cluster.bigchain.fptu.tech"
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
