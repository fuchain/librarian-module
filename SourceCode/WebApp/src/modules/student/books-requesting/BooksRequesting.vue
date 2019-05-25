<template>
  <book-list :books="books"/>
</template>

<script>
import { mapGetters } from "vuex";
import store from "./store";
import BookList from "./components/BookList.vue";

export const STORE_KEY = "student-books";

export default {
  components: {
    BookList
  },
  computed: {
    ...mapGetters({
      users: `${STORE_KEY}/users`
    })
  },
  created() {
    if (!(STORE_KEY in this.$store._modules.root._children)) {
      this.$store.registerModule(STORE_KEY, store);
    }
  }
};
</script>
