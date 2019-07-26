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

window.isMobile = function() {
  var check = false;
  (function(a) {
    if (
      /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(
        a
      ) ||
      /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(
        a.substr(0, 4)
      )
    ) {
      check = true;
    }
  })(navigator.userAgent || navigator.vendor || window.opera);
  return check;
};

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
    initSocket();
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
