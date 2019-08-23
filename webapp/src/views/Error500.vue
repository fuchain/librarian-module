<template>
  <div class="h-screen flex w-full bg-img">
    <div
      class="vx-col flex items-center justify-center flex-col sm:w-1/3 md:w-3/5 lg:w-3/4 xl:w-1/2 mx-auto text-center"
    >
      <img
        src="@/assets/images/pages/404.png"
        alt="graphic-404"
        class="mx-auto mb-4"
        style="max-width: 250px"
      />
      <h1
        class="mb-12 pl-4 pr-4 text-5xl d-theme-heading-color"
        style="color: white;"
      >Xảy ra lỗi trên ứng dụng!</h1>
      <p
        class="mb-16 d-theme-text-inverse ml-4 mr-8"
        style="background: white; padding: 0.5rem; border-radius: 5px;"
      >Chúng tôi đã gửi mã lỗi này về cho nhóm phát triển, lỗi này sẽ được khắc phục sớm.</p>
      <vs-button size="large" type="relief" @click="doReload" icon="update">Tải lại ứng dụng</vs-button>
      <vs-button
        class="mt-4"
        color="black"
        size="large"
        type="relief"
        @click="doLogout"
        icon="eject"
      >Thoát tài khoản</vs-button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    error: Object
  },
  methods: {
    doReload() {
      window.location.href = "/";
    },
    doLogout: function() {
      this.$vs.loading({
        color: "white",
        background: "darkorange",
        text: "Đang đăng xuất"
      });

      setTimeout(
        function() {
          this.$auth.clearAuth();
          this.$router.push("/login");

          this.$vs.loading.close();
        }.bind(this),
        500
      );
    }
  },
  mounted() {
    if (!this.error) {
      this.$router.push("/");
    }
  }
};
</script>

<style>
.title-loading {
  color: white;
}
</style>
