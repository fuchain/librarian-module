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
        />
        <img
          src="@/assets/images/elements/decore-right.png"
          class="decore-right"
          alt="Decore Right"
          width="175"
        />
        <feather-icon
          icon="AwardIcon"
          class="p-6 mb-8 bg-primary inline-flex rounded-full text-white shadow"
          svgClasses="h-8 w-8"
        ></feather-icon>
        <h1 class="mb-6 text-white">Xin chào {{ name || "Thủ thư" }},</h1>
        <p class="xl:w-3/4 lg:w-4/5 md:w-2/3 w-4/5 mx-auto text-white">
          Ứng dụng
          <strong>Thư viện FU</strong>
          đang giúp
          <strong>{{ totalUsers }}</strong> người đọc trao đổi
          <strong>
            <span style="font-size: 1.5rem;">{{ totalBookDetails }}</span>
          </strong> quyển sách của
          <strong>{{ totalBookInstances }}</strong> đầu sách trong thư viện, xem chi tiết hơn ở phần hiệu suất.
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
  },
  data() {
    return {
      totalBookInstances: 0,
      totalBookDetails: 0,
      totalUsers: 0
    };
  },
  mounted() {
    this.getOverview();
  },
  methods: {
    getOverview() {
      this.$Progress.start();
      this.$http
        .get(`${this.$http.baseUrl}/librarian/overviews`)
        .then(response => {
          const {
            book_detail_total,
            book_instance_total,
            user_total
          } = response.data;

          this.totalBookInstances = book_detail_total;
          this.totalBookDetails = book_instance_total;
          this.totalUsers = user_total;
        })
        .finally(() => {
          this.$Progress.finish();
        });
    }
  },
  beforeDestroy() {
    this.$Progress.finish();
  }
};
</script>

<style lang="scss" scoped>
.greet-user {
  position: relative;
  .decore-left {
    position: absolute;
    left: 0;
    top: 0;
  }
  .decore-right {
    position: absolute;
    right: 0;
    top: 0;
  }
}
</style>
