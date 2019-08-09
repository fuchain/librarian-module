<template>
  <vx-card title="Hạ tầng Swarm">
    <vue-friendly-iframe
      src="https://swarm.fptu.tech"
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
