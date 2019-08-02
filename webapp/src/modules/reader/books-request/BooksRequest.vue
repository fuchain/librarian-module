<template>
  <div>
    <vx-card title="Tìm mượn sách" noShadow>
      <div class="vx-row mb-6">
        <div class="vx-col sm:w-2/3 w-full">
          <vs-input
            class="w-full"
            v-model="searchText"
            placeholder="Tìm JPD101 hoặc Nihongo Deiku 1"
            v-on:keyup.enter="doSearch"
            autofocus
            id="book-search"
          />
        </div>
        <div class="vx-col sm:w-1/3 w-full">
          <vs-button
            type="relief"
            color="primary"
            class="w-full"
            @click="doSearch"
            icon="search"
          >Tìm sách</vs-button>
        </div>
      </div>
      <div class="items-grid-view vx-row match-height" v-if="listBooks.length" appear>
        <div
          class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
          v-for="item in listBooks"
          :key="item.id"
        >
          <book-card :item="item">
            <template slot="action-buttons">
              <div class="flex flex-wrap">
                <div
                  class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                  @click="submit(item.id)"
                >
                  <feather-icon icon="CheckIcon" svgClasses="h-4 w-4" />

                  <span class="text-sm font-semibold ml-2">YÊU CẦU MƯỢN SÁCH</span>
                </div>
              </div>
            </template>
          </book-card>
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
        <book-card :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                @click="comingSoon"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">YÊU CẦU MƯỢN SÁCH</span>
              </div>
            </div>
          </template>
        </book-card>
      </div>
    </div>
  </div>
</template>

<script>
import BookCard from "@/views/components/BookCard.vue";
import { parseBookSearchItem } from "@http/parse";

export default {
  components: {
    BookCard
  },
  computed: {
    phone() {
      return this.$store.getters.phone;
    }
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
          name: "Code dạo ký sự",
          type: "info"
        },
        {
          id: 2,
          code: "SKC",
          description:
            "Những môn thầy Khánh dạy trong Đại học FPT là: C, C++, Alice, I2SE, OS, Advanced Java, EIT, XML. ",
          image: "/images/khanhkt.jpg",
          name: "Surviving Mr.KhanhKT's Courses for Dummies",
          type: "info"
        }
      ]
    };
  },
  methods: {
    doSearch() {
      if (!this.searchText.trim()) return;

      if (!this.phone) {
        this.$vs.notify({
          title: "Chưa cập nhật số điện thoại",
          text:
            "Vui lòng cập nhật số điện thoại ở mục 'Hồ sơ' của bạn trước khi mượn sách",
          color: "danger",
          position: "top-center",
          fixed: true,
          iconPack: "feather",
          icon: "icon-alert-triangle",
          click: () => {
            this.$router.push("/profile");
          }
        });
        return;
      }

      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/book/search?text=${this.searchText}`)
        .then(response => {
          const data = response.data;

          this.$vs.loading.close();

          this.listBooks = [].concat(parseBookSearchItem(data, "info"));

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
    submit(bookId) {
      if (!this.searchText.trim() || !bookId) return;
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/matching/create_request`, {
          book_detail_id: bookId
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
            text: "Đã xảy ra lỗi",
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
