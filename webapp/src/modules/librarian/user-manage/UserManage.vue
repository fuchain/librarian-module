<template>
  <data-list :dataList="users" :loaded="loaded" @doReload="getList" />
</template>

<script>
import { mapGetters } from "vuex";
import store from "./store";
import DataList from "./components/DataList.vue";

export const STORE_KEY = "librarian-manage-user";

export default {
  components: {
    DataList
  },
  computed: {
    ...mapGetters({
      loaded: `${STORE_KEY}/loaded`,
      users: `${STORE_KEY}/users`
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
      if (this.users) {
        this.$Progress.start();
      } else {
        this.$vs.loading();
      }
      await this.$store.dispatch(`${STORE_KEY}/getUsers`);

      if (this.users.length) {
        this.$Progress.finish();
      } else {
        this.$vs.loading.close();
      }
    }
  }
};
</script>
