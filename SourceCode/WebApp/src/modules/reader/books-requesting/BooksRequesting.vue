<template>
  <book-list :books="books" v-if="loaded" @doReload="getList" />
</template>

<script>
import { mapGetters } from "vuex";
import store from "./store";
import BookList from "./components/BookList.vue";

export const STORE_KEY = "reader-books-requesting";

export default {
  components: {
    BookList
  },
  computed: {
    ...mapGetters({
      loaded: `${STORE_KEY}/loaded`,
      books: `${STORE_KEY}/books`
    })
  },
  created() {
    if (!(STORE_KEY in this.$store._modules.root._children)) {
      this.$store.registerModule(STORE_KEY, store);
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      this.$vs.loading();
      await this.$store.dispatch(`${STORE_KEY}/getBooks`);
      this.$store.dispatch("getNumOfBooks");
      this.$vs.loading.close();
    }
  }
};
</script>
