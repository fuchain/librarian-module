<template>
  <data-list :dataList="bookdetails" v-if="loaded" @doReload="getList" @doSearch="doSearch"/>
</template>

<script>
import { mapGetters } from "vuex";
import store from "./store";
import DataList from "./components/DataList.vue";

export const STORE_KEY = "librarian-manage-book";

export default {
  components: {
    DataList
  },
  computed: {
    ...mapGetters({
      loaded: `${STORE_KEY}/loaded`,
      bookdetails: `${STORE_KEY}/bookDetails`
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
      try {
        await this.$store.dispatch(`${STORE_KEY}/getBookDetails`);
      } catch (err) {
        this.$vs.notify({
          title: "Lỗi",
          text: `Lỗi xảy ra khi tải dữ liệu, mã lỗi: ${err.response.status}`,
          color: "danger",
          position: "top-center"
        });

        this.$router.push("/error");
      }

      this.$vs.loading.close();
    },
    async doSearch(text) {
      this.$vs.loading();
      await this.$store.dispatch(`${STORE_KEY}/searchBookDetails`, text);
      this.$vs.loading.close();
    }
  }
};
</script>
