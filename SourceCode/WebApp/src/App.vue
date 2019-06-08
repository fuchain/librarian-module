<template>
  <div id="app">
    <router-view v-if="!error"></router-view>
    <error-500 v-if="error"></error-500>
  </div>
</template>

<script>
import themeConfig from "@/../themeConfig.js";
import Error500 from "./views/Error500";

export default {
  data() {
    return {
      error: false
    };
  },
  watch: {
    "$store.state.theme"(val) {
      this.toggleClassInBody(val);
    }
  },
  components: {
    Error500
  },
  methods: {
    toggleClassInBody(className) {
      if (className === "dark") {
        if (document.body.className.match("theme-semi-dark")) {
          document.body.classList.remove("theme-semi-dark");
        }
        document.body.classList.add("theme-dark");
      } else if (className === "semi-dark") {
        if (document.body.className.match("theme-dark")) {
          document.body.classList.remove("theme-dark");
        }
        document.body.classList.add("theme-semi-dark");
      } else {
        if (document.body.className.match("theme-dark")) {
          document.body.classList.remove("theme-dark");
        }
        if (document.body.className.match("theme-semi-dark")) {
          document.body.classList.remove("theme-semi-dark");
        }
      }
    }
  },
  async mounted() {
    this.toggleClassInBody(themeConfig.theme);

    this.$vs.loading({
      background: "primary",
      color: "white",
    });

    // Get profile
    if (this.$auth.isAuthenticated()) {
      await this.$store.dispatch("getProfile");
    }

    // Get num of books
    if (this.$auth.isAuthenticated()) {
      await this.$store.dispatch("getNumOfBooks");
    }

    this.$vs.loading.close();
  },
  errorCaptured(err, vm, info) {
    this.error = true;

    this.$vs.notify({
      title: "Lỗi xảy ra",
      text: "Lỗi do đường truyền kém, vui lòng tải lại ứng dụng",
      color: "danger",
      position: "top-center"
    });

    this.$router.push("/error");

    // Log error
    console.log("Error: ", err);
    console.log("VM: ", vm);
    console.log("Info: ", info);
  }
};
</script>
