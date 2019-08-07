<template>
  <div id="app">
    <router-view v-if="!error"></router-view>
    <vue-progress-bar></vue-progress-bar>
    <error-500 v-if="error" :error="error"></error-500>
    <vx-tour :steps="steps" />

    <global-popup></global-popup>
    <book-detail-popup></book-detail-popup>
  </div>
</template>

<script>
/* eslint-disable */

import themeConfig from "@/../themeConfig.js";
import Error500 from "./views/Error500";

import initSocket from "@core/socket";

import GlobalPopup from "@/views/components/GlobalPopup.vue";
import BookDetailPopup from "@/views/components/BookDetailPopup.vue";

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
            "Xem các sách trong ví của bạn ở đây, gồm sách đang giữ, sách đang yêu cầu trả và sách đang yêu cầu nhận"
        },
        {
          target: "#menu-item-200",
          content: "Vào đây để tìm sách và yêu cầu mượn sách"
        },
        {
          target: "#menu-item-200",
          content:
            "Khi bạn vào kì học mới, học môn <strong>MAE101</strong> và cần mượn sách toán, nhập mã môn hoặc tên sách vào đây và bấm yêu cầu nhận sách"
        },
        {
          target: "#menu-item-200",
          content:
            "Hệ thống sẽ tìm người trả sách cho bạn, hai bạn sẽ hẹn gặp nhau để trả/nhận sách mà không cần phải lên thư viện xếp hàng"
        },
        {
          target: "#menu-group-1",
          content:
            "Khi bạn kết thúc kì học, bạn có thể vào mục sách đang giữ, và chọn trả sách toán MAE101 cho sinh viên khóa sau mượn"
        },
        {
          target: "#menu-item-400",
          content: "Vào đây để xem lịch sử nhận sách của bạn"
        },
        {
          target: "#menu-item-500",
          content:
            "Bạn cũng có thể dùng tính năng quét mã QR để chuyển sách nhanh hơn (nếu dùng di động)"
        },
        {
          target: "#menu-item-300",
          content:
            "Bạn có thể vào mục này và nhập coupon 'FUCHAIN' để nhận thử vài quyển sách"
        },
        {
          target: "#menu-item-600",
          content:
            "Đừng quên giúp chúng mình đánh giá trải nghiệm ứng dụng tại đây nhé"
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
    GlobalPopup,
    BookDetailPopup
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

    setTimeout(
      function() {
        if (!window.navigator.onLine)
        this.$vs.notify({
          title: "Không có mạng",
          text: "Ứng dụng cần có mạng để sử dụng các tính năng",
          color: "danger",
          position: "top-center",
          fixed: true,
        });
      }.bind(this),
      1000
    );
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

    this.$router.push("/error");
    if (this.$vs.loading) this.$vs.loading.close();
  }
};
</script>
