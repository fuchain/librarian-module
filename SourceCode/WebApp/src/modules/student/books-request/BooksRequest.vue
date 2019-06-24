<template>
  <div>
    <vx-card title="Yêu cầu mượn sách">
      <div class="vx-row mb-6">
        <div class="vx-col sm:w-2/3 w-full">
          <vs-input
            class="w-full"
            v-model="searchText"
            placeholder="Tìm JPD101 hoặc Nihongo Deiku 1"
          />
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
      <strong>Gợi ý mượn sách</strong> cho bạn trong kì học tiếp theo, dựa vào danh sách môn của kì bạn sắp học. Bạn nên sắp xếp mượn và trả sách đúng hạn mỗi kì để thuận tiện hơn trong việc liên lạc mượn và trả sách.
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

                <span class="text-sm font-semibold ml-2" @click="comingSoon">YÊU CẦU MƯỢN SÁCH</span>
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
          code: "CDK",
          description:
            "Những kĩ năng từ cứng đến mềm mà lập trình viên nào cũng phải biết để thăng tiến và thành công trong sự nghiệp.",
          image: "/images/codedaokysu.png",
          name: "Code dạo ký sự"
        },
        {
          id: 2,
          code: "SKC",
          description:
            "Những môn thầy Khánh dạy trong Đại học FPT là: C, C++, Alice, I2SE, OS, Advanced Java, EIT, XML. ",
          image: "/images/khanhkt.jpg",
          name: "Surviving Mr.KhanhKT's Courses for Dummies"
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
    },
    comingSoon() {
      this.$vs.notify({
        title: "Tính năng này chưa hỗ trợ",
        text: "Chức năng gợi ý sách sẽ sớm ra mắt thôi, đợi nghen",
        color: "warning",
        position: "top-center"
      });
    }
  }
};
</script>
