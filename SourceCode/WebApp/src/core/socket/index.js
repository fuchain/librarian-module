import io from "socket.io-client";
import auth from "@auth";
import $http from "@http";

function init() {
  // Socket
  const socket = io.connect($http.socketUrl, {
    query: "token=" + auth.getAccessToken(),
    transports: ["websocket"]
  });

  window.socket = socket.on("notification", function(msg) {
    window.vue.$vs.notify({
      title: "Thông báo mới",
      text: msg,
      color: "success",
      position: "top-center"
    });

    window.vue.$store.dispatch("addNotification", {
      title: "Kiểm thử thông báo",
      msg,
      icon: "MessageSquareIcon",
      time: new Date().getTime(),
      category: "primary"
    });
  });
}

export default init;
