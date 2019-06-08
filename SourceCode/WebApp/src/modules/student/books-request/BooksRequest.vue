<template>
  <div>
    <vx-card title="Yêu cầu mượn sách">
      <div class="vx-row mb-6">
        <div class="vx-col sm:w-2/3 w-full">
          <vs-input class="w-full" v-model="searchText" placeholder="PRC391 hoặc Cloud Computer "/>
        </div>
        <div class="vx-col sm:w-1/3 w-full">
          <vs-button type="relief" color="primary" class="w-full" @click="doSearch">Tìm sách</vs-button>
        </div>
      </div>
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
                  class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                  @click="submit(item.name)"
                >
                  <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                  <span class="text-sm font-semibold ml-2">YÊU CẦU MƯỢN SÁCH</span>
                </div>
              </div>
            </template>
          </item-grid-view>
        </div>
      </div>
    </vx-card>

    <vs-divider border-style="dashed"></vs-divider>

    <vs-alert active="true" class="mb-4">
      <strong>Gợi ý YÊU CẦU MƯỢN SÁCH</strong> cho bạn trong kì học tiếp theo, dựa vào danh sách môn của kì bạn sắp học. Bạn nên sắp xếp mượn và trả sách đúng hạn mỗi kì để thuận tiện hơn trong việc liên lạc mượn và trả sách.
    </vs-alert>

    <div class="items-grid-view vx-row match-height" v-if="suggestedBooks.length" appear>
      <div
        class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
        v-for="item in suggestedBooks"
        :key="item.id"
      >
        <item-grid-view :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2">YÊU CẦU MƯỢN SÁCH</span>
              </div>
            </div>
          </template>
        </item-grid-view>
      </div>
    </div>
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
      searchText: "",
      bookCode: "",
      listBooks: [],
      suggestedBooks: [
        {
          id: 1,
          code: "ISE",
          description:
            "Book Introduction to Software Engineering learning at FPT University",
          image: "/images/book-thumbnail.jpg",
          name: "Introduction to Software Engineering"
        },
        {
          id: 2,
          code: "GLB",
          description: "Golang Basic learning at FPT University",
          image: "/images/book-thumbnail.jpg",
          name: "Golang Basic"
        }
      ]
    };
  },
  methods: {
    doSearch() {
      if (!this.searchText.trim()) return;
      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/bookdetails/search?name=${this.searchText}`)
        .then(response => {
          const data = response.data;

          this.$vs.loading.close();

          this.listBooks = [].concat(
            data.map(e => {
              return {
                id: e.id,
                name: e.name,
                description: `Book ${
                  e.name
                } for Software Engineering learning at FPT University`,
                image: "/images/book-thumbnail.jpg",
                code: e.name.substring(0, 3).toUpperCase() + "101"
              };
            })
          );

          if (!data.length) {
            this.$vs.notify({
              title: "Lỗi",
              text: "Không tìm thấy quyển sách nào, vui lòng đổi từ khóa",
              color: "warning",
              position: "top-center"
            });
          }
        });
    },
    submit(bookName) {
      if (!this.searchText.trim() || !bookName) return;
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/requests`, {
          type: 1,
          book_name: bookName
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Yêu cầu của bạn đã được hệ thống tiếp nhận",
            color: "primary",
            position: "top-center"
          });

          this.$store.dispatch("getNumOfBooks");
        })
        .catch(err => {
          // Catch
          console.log(err);

          this.$vs.loading.close();
          this.$vs.notify({
            title: "Thất bại",
            text: "Bạn đã có yêu cầu mượn sách này rồi",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
  }
};
</script>
