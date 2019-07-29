<template>
  <div id="app">
    <router-view v-if="!error"></router-view>
    <error-500 v-if="error" :error="error"></error-500>
    <vx-tour :steps="steps" />

    <global-popup></global-popup>
  </div>
</template>

<script>
/* eslint-disable */

import themeConfig from "@/../themeConfig.js";
import Error500 from "./views/Error500";

import initSocket from "@core/socket";

import GlobalPopup from "@/views/components/GlobalPopup.vue";

const VxTour = () => import("@/views/components/VxTour.vue");

export default {
  data() {
    return {
      error: false,
      tourOptions: {
        useKeyboardNavigation: true,
        labels: {
          buttonSkip: "Bỏ qua hướng dẫn",
          buttonPrevious: "Trước đó",
          buttonNext: "Tiếp theo",
          buttonStop: "Xong"
        }
      },
      steps: [
        {
          target: ".the-navbar__user-meta",
          content:
            "Chào mừng bạn đến với <strong>Thư viện FU</strong>, đây là tour hướng dẫn sử dụng app"
        },
        {
          target:
            window.isMobile() === true ? ".feather-menu" : "#btnSidebarToggler",
          content: "Bấm vào đây để mở/đóng menu"
        },
        {
          target: "#menu-group-1",
          content:
            "Xem các sách của bạn ở đây, gồm sách đang giữ, sách đang yêu cầu trả và sách đang yêu cầu nhận"
        },
        {
          target: "#menu-item-200",
          content:
            "Vào đây để mượn sách mới, ví dụ khi bạn vào kì mới, học môn <strong>MAE101</strong> và cần mượn sách toán, nhập mã môn vào đây và bấm yêu cầu nhận sách"
        },
        {
          target: "#menu-item-300",
          content:
            "Vào đây khi bạn có mã nhận sách từ thư viện hoặc từ người chuyển sách cho bạn"
        }
      ]
    };
  },
  watch: {
    "$store.state.theme"(val) {
      this.toggleClassInBody(val);
    }
  },
  components: {
    Error500,
    VxTour,
    GlobalPopup
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
  created() {
    // Socket
    if (this.$auth.isAuthenticated()) initSocket();
  },
  async mounted() {
    this.toggleClassInBody(themeConfig.theme);

    if (!this.$localStorage.getItem("publicKey")) {
      this.$router.push("/keypair");
    }

    if (
      this.$auth.isAuthenticated() &&
      (!this.$localStorage.getItem("privateKey") &&
        !this.$localStorage.getItem("publicKey"))
    ) {
      this.$router.push("/keypair");
    } else if (this.$auth.isAuthenticated()) {
      this.$vs.loading({
        background: "primary",
        color: "white",
        scale: "1.5",
        type: "sound"
      });

      try {
        await this.$store.dispatch("getProfile");
        this.$store.dispatch("getNotification");
        !this.$auth.isAdmin() && this.$store.dispatch("getNumOfBooks");
      } catch (e) {
        // Catch error
        this.$router.push("/error");
      }

      this.$vs.loading.close();
    }
  },
  errorCaptured(err, vm, info) {
    // Print log error
    console.log("Error: ", err.toString());
    console.log("Info: ", info.toString());

    this.error = true;

    this.$vs.notify({
      title: "Lỗi xảy ra",
      text: "Lỗi bất ngờ xảy ra, vui lòng tải lại ứng dụng",
      color: "danger",
      position: "top-center"
    });

    // Log API
    const metadata = {
      err: err.toString(),
      info: info.toString()
    };

    if (!window.webpackHotUpdate) {
      this.$http.post(`${this.$http.nodeUrl}/logs`, {
        type: "error",
        source: "webapp",
        metadata: JSON.stringify(metadata)
      });
    }

    this.$router.push("/error");
    if (this.$vs.loading) this.$vs.loading.close();
  }
};
</script>
