import io from "socket.io-client";
import auth from "@auth";
import $http from "@http";
import redirect from "@core/socket/redirect";

export let socket;

function init() {
  // Socket
  socket = io.connect($http.socketUrl, {
    query: "token=" + auth.getAccessToken(),
    transports: ["websocket"]
  });

  socket.on("logout", function() {
    window.vue.$vs.notify({
      fixed: true,
      title: "Có người khác đăng nhập tài khoản",
      text: "Bạn sẽ bị đăng xuất",
      color: "danger",
      position: "top-center",
      iconPack: "feather",
      icon: "icon-x"
    });

    window.vue.$vs.loading({
      color: "white",
      background: "darkorange",
      text: "Đang đăng xuất"
    });

    setTimeout(function() {
      window.vue.$auth.clearAuth();
      window.vue.$router.push("/login");

      window.vue.$vs.loading.close();
    }, 500);
  });

  socket.on("notification", function({ message, type, id }) {
    window.vue.$vs.notify({
      fixed: true,
      title: "Thông báo mới",
      text: message,
      color: "primary",
      position: "top-center",
      iconPack: "feather",
      icon: "icon-info",
      click: () => {
        redirect({
          message,
          type,
          id
        });
      }
    });

    window.vue.$store.dispatch("addNotification", {
      isRead: false,
      title: "Thông báo",
      message,
      id,
      type,
      icon: "InfoIcon",
      time: new Date().getTime(),
      category: "primary"
    });
  });
}

export default init;
