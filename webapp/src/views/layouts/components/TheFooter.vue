<template>
  <div class="hidden md:block">
    <footer class="the-footer flex-wrap justify-between" :class="classes">
      <span></span>
      <span class="md:flex hidden items-center">
        <span>
          Powered by
          <a
            href="https://blockchain.fptu.tech"
            target="_blank"
            style="background: #7367F0; color: white; padding: 0.5rem; border-radius: 5px; cursor: pointer;"
          >FUChain</a>
        </span>
      </span>
    </footer>
  </div>
</template>

<script>
export default {
  name: "the-footer",
  props: {
    classes: {
      type: String
    }
  }
};
</script>
