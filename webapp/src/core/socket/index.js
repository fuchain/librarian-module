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

  socket.on("notification", function({ message, type, id }) {
    window.vue.$store.dispatch("getNumOfBooks");

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

    setTimeout(function() {
      if (window.reloadList) {
        window.reloadList();
      }
    }, 100);
  });

  socket.on("event", function({ message, type }) {
    if (type === "transaction") {
      window.vue.$store.dispatch("openTxPopup", message);
    }

    if (type === "success") {
      window.vue.$store.dispatch("getNumOfBooks");

      window.vue.$vs.notify({
        title: "Thành công",
        text: message,
        color: "primary",
        position: "top-center",
        iconPack: "feather",
        icon: "icon-check"
      });
    }

    if (type === "notify") {
      window.vue.$vs.notify({
        title: "Thông báo",
        text: message,
        color: "success",
        position: "top-center",
        iconPack: "feather",
        icon: "icon-info",
        fixed: true
      });
    }

    if (type === "fail") {
      window.vue.$vs.notify({
        title: "Thất bại",
        text: message,
        color: "danger",
        position: "top-center",
        iconPack: "feather",
        icon: "icon-error"
      });
    }

    setTimeout(function() {
      if (window.reloadList) {
        window.reloadList();
      }
    }, 100);
  });
}

export default init;
