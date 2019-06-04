<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">Sách đang giữ</h2>
    <div class="items-grid-view vx-row match-height" v-if="listBooks.length" appear>
      <div
        class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
        v-for="item in listBooks"
        :key="item.id"
      >
        <item-grid-view :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
              >
                <feather-icon icon="XIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="doReturnBook(item)">TRẢ SÁCH</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="BookOpenIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2">CHI TIẾT</span>
              </div>
            </div>
          </template>
        </item-grid-view>
      </div>
    </div>

    <vx-card title="Bạn đang không giữ sách nào." v-else>
      <vs-button @click="$router.push('/books/request')">Mượn sách</vs-button>
    </vx-card>
  </div>
</template>

<script>
const ItemGridView = () => import("./ItemGridView.vue");

export default {
  components: {
    ItemGridView
  },
  data() {
    return {
      listBooks: []
    };
  },
  mounted() {
    this.$vs.loading();

    this.$http
      .get(`${this.$http.baseUrl}/users/current_books`)
      .then(response => {
        const data = response.data;

        const books = data.map(e => {
          return {
            id: e.id,
            name: e.bookDetail.name,
            description: `Book ${
              e.bookDetail.name
            } for Software Engineering learning at FPT University`,
            image: "https://i.imgur.com/2j6B1n5.jpg",
            time: e.updateDate,
            code: e.bookDetail.name.substring(0, 3).toUpperCase() + "101"
          };
        });

        this.listBooks = [].concat(books);
        this.$vs.loading.close();
      });
  },
  methods: {
    doReturnBook(book) {
      this.$router.push({
        name: "book-return",
        params: { book }
      });
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
