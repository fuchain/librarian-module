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
    },
    async mounted() {
        this.$vs.loading({
            container: "#div-with-loading",
            scale: 0.6
        });

        await this.$store.dispatch(`${STORE_KEY}/getBooks`);

        this.$vs.loading.close("#div-with-loading > .con-vs-loading");
    }
};
</script>
