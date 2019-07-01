/* eslint-disable indent */

export default function redirect(type) {
  if (type) {
    switch (type) {
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
