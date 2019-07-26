/* eslint-disable indent */
import $http from "@http";
import $auth from "@auth";

export default function redirect(notification) {
  if (notification.id) {
    $http
      .put(`${$http.nodeUrl}/notifications/touch/${notification.id}`)
      .then(() => {
        window.vue.$store.dispatch("getNotification", true);
      });
  }

  if (notification.type) {
    if (!$auth.isAdmin()) {
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
    } else {
      if (notification.type.includes("bookinstance")) {
        const arr = notification.type.split(":");

        if (!isNaN(arr[1])) {
          $http.get(`${$http.baseUrl}/books/${arr[1]}`).then(response => {
            const id = response.data.bookDetail.id;

            window.vue.$router.push(
              `librarian/book-details-manage/${id}/${arr[1]}`
            );
          });
        }
      }
    }
  }
}
