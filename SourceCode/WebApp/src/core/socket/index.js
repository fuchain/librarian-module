import io from "socket.io-client";
import auth from "@auth";
import $http from "@http";
import redirect from "@core/socket/redirect";

function init() {
  // Socket
  const socket = io.connect($http.socketUrl, {
    query: "token=" + auth.getAccessToken(),
    transports: ["websocket"]
  });

  window.socket = socket.on("notification", function({ message, type, id }) {
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
