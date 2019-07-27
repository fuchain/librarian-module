<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">
      Sách đang giữ
      <vs-button color="primary" type="relief" size="small" class="ml-4" @click="callReload">Làm mới</vs-button>
    </h2>
    <vs-input
      size="large"
      icon="search"
      class="w-full mb-4"
      placeholder="Tìm tên sách"
      v-model="searchText"
      v-if="books.length"
    />
    <div class="items-grid-view vx-row match-height" v-if="books.length" appear>
      <div
        class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
        v-for="item in listBooks"
        :key="item.id"
      >
        <book-card :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
                @click="doReturnBook(item)"
              >
                <feather-icon icon="XIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">TRẢ SÁCH</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="BookOpenIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">CHI TIẾT</span>
              </div>
            </div>
          </template>
        </book-card>
      </div>
    </div>

    <vx-card title="Bạn đang không giữ sách nào." v-else>
      <vs-button @click="$router.push('/books/request')" icon="search">Tìm mượn sách</vs-button>
    </vx-card>
  </div>
</template>

<script>
import BookCard from "@/views/components/BookCard.vue";

export default {
  components: {
    BookCard
  },
  data() {
    return {
      searchText: ""
    };
  },
  props: {
    books: {
      type: Array
    }
  },
  computed: {
    listBooks() {
      if (!this.searchText.trim()) return this.books;

      return this.books.filter(e => {
        e.name.toLowerCase().includes(this.searchText.trim().toLowerCase());
      });
    }
  },
  methods: {
    doReturnBook(book) {
      this.$router.push({
        name: "book-return",
        params: { book }
      });
    },
    callReload() {
      this.$emit("doReload");
    }
  }
};
</script>

<style lang="scss" scoped>
#ecommerce-wishlist-demo {
  .item-view-primary-action-btn {
    color: #2c2c2c !important;
    background-color: #f6f6f6;
    min-width: 50%;
  }

  .item-view-secondary-action-btn {
    min-width: 50%;
  }
}
</style>
