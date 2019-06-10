<template>
  <div class="vx-row">
    <!-- CARD 1: CONGRATS -->
    <div class="vx-col w-full lg:w-1/2 mb-base">
      <vx-card slot="no-body" class="text-center bg-primary-gradient greet-user">
        <img
          src="@/assets/images/elements/decore-left.png"
          class="decore-left"
          alt="Decore Left"
          width="200"
        >
        <img
          src="@/assets/images/elements/decore-right.png"
          class="decore-right"
          alt="Decore Right"
          width="175"
        >
        <feather-icon
          icon="AwardIcon"
          class="p-6 mb-8 bg-primary inline-flex rounded-full text-white shadow"
          svgClasses="h-8 w-8"
        ></feather-icon>
        <h1 class="mb-6 text-white">Chúc mừng {{ name || "Thủ thư" }},</h1>
        <p class="xl:w-3/4 lg:w-4/5 md:w-2/3 w-4/5 mx-auto text-white">
          Ứng dụng
          <strong>Thư viện FU</strong> đã giúp bạn giải quyết tới
          <strong>83.9%</strong> khối lượng công việc, xem chi tiết hơn ở phần hiệu suất.
        </p>
      </vx-card>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    name() {
      return this.$store.state.fullname;
    }
  }
};
</script>
