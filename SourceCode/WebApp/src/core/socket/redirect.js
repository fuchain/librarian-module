/* eslint-disable indent */
import $http from "@http";

export default function redirect(notification) {
  if (notification.id) {
    $http
      .put(`${$http.nodeUrl}/notifications/touch/${notification.id}`)
      .then(() => {
        window.vue.$store.dispatch("getNotification", true);
      });
  }

  if (notification.type) {
    switch (notification.type) {
      case "keeping":
        window.vue.$router.push("/books/keeping");
        break;
      case "returning":
        window.vue.$router.push("/books/returning");
        break;
      case "requesting":
        window.vue.$router.push("/books/requesting");
        break;
    }
  }
}
