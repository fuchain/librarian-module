<template>
  <div id="faq-page">
    <!-- JUMBOTRON -->
    <div class="faq-jumbotron">
      <div class="faq-jumbotron-content lg:p-32 md:p-24 sm:p-16 p-8 rounded-lg mb-base">
        <h1 class="mb-1 text-white">Sử dụng App để mượn/trả sách thư viện?</h1>
        <p
          class="text-white"
        >Nếu chưa quen về việc sử dụng app thư viện, các bạn có thể xem hướng dẫn tại đây</p>
        <vs-input
          placeholder="Tìm kiếm"
          v-model="faqSearchQuery"
          icon-pack="feather"
          icon="icon-search"
          size="large"
          class="w-full mt-6"
          icon-no-border
        />
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col w-full md:w-2/5 lg:w-1/4 rounded-lg">
        <vx-card>
          <h4>Bảng nội dung</h4>
          <ul class="faq-topics mt-4">
            <li
              v-for="category in categories"
              :key="category.id"
              class="p-2 font-medium flex items-center"
              @click="faqFilter = category.id"
            >
              <div class="h-3 w-3 rounded-full mr-2" :class="'bg-' + category.color"></div>
              <span class="cursor-pointer">{{ category.name }}</span>
            </li>
          </ul>
        </vx-card>
      </div>

      <!-- FAQ COL -->
      <div class="vx-col w-full md:w-3/5 lg:w-3/4 mt-12 md:mt-0">
        <vs-collapse accordion type="margin" class="p-0">
          <vs-collapse-item
            v-for="(que,index) in filteredFaq"
            class="faq-bg rounded-lg"
            :class="{'mt-0': !index}"
            :key="que.id"
          >
            <div slot="header">
              <h5>{{ que.question }}</h5>
            </div>
            <p>{{ que.ans }}</p>
          </vs-collapse-item>
        </vs-collapse>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      faqSearchQuery: "",
      faqFilter: 1,
      categories: [
        {
          id: 1,
          name: "Tất cả",
          color: "grey"
        },
        {
          id: 2,
          name: "Quy trình",
          color: "primary"
        },
        {
          id: 3,
          name: "Trách nhiệm",
          color: "success"
        },
        {
          id: 4,
          name: "Tranh chấp",
          color: "warning"
        },
        {
          id: 5,
          name: "Quyền hạn",
          color: "danger"
        }
      ],
      faqs: [
        {
          id: 1,
          categoryId: 2,
          question: "Thư viện phân tán là gì?",
          ans:
            "Lorem ipsum dolor sit amet, sed paulo libris ex, at usu zril minimum, intellegat interesset at cum. Eum porro tempor facilis ei, latine ornatus noluisse sit ad. Nam in inermis inimicus. Vel alia inciderint ad, duo denique explicari deterruisset ex."
        },
        {
          id: 2,
          categoryId: 2,
          question: "Quy trình mượn sách",
          ans:
            "Lorem ipsum dolor sit amet, sed paulo libris ex, at usu zril minimum, intellegat interesset at cum. Eum porro tempor facilis ei, latine ornatus noluisse sit ad. Nam in inermis inimicus. Vel alia inciderint ad, duo denique explicari deterruisset ex."
        },
        {
          id: 3,
          categoryId: 2,
          question: "Quy trình trả sách",
          ans:
            "Lorem ipsum dolor sit amet, sed paulo libris ex, at usu zril minimum, intellegat interesset at cum. Eum porro tempor facilis ei, latine ornatus noluisse sit ad. Nam in inermis inimicus. Vel alia inciderint ad, duo denique explicari deterruisset ex."
        },
        {
          id: 4,
          categoryId: 2,
          question: "Quy định bảo quản sách",
          ans:
            "Lorem ipsum dolor sit amet, sed paulo libris ex, at usu zril minimum, intellegat interesset at cum. Eum porro tempor facilis ei, latine ornatus noluisse sit ad. Nam in inermis inimicus. Vel alia inciderint ad, duo denique explicari deterruisset ex."
        },
        {
          id: 5,
          categoryId: 2,
          question: "Trách nhiệm khi trao đổi sách",
          ans:
            "Lorem ipsum dolor sit amet, sed paulo libris ex, at usu zril minimum, intellegat interesset at cum. Eum porro tempor facilis ei, latine ornatus noluisse sit ad. Nam in inermis inimicus. Vel alia inciderint ad, duo denique explicari deterruisset ex."
        }
      ]
    };
  },
  computed: {
    filteredFaq() {
      return this.faqs.filter(faq => {
        if (this.faqFilter === 1) {
          return (
            faq.question
              .toLowerCase()
              .includes(this.faqSearchQuery.toLowerCase()) ||
            faq.ans.toLowerCase().includes(this.faqSearchQuery.toLowerCase())
          );
        } else if (this.faqFilter === 2) {
          return (
            faq.categoryId === 2 &&
            (faq.question
              .toLowerCase()
              .includes(this.faqSearchQuery.toLowerCase()) ||
              faq.ans.toLowerCase().includes(this.faqSearchQuery.toLowerCase()))
          );
        } else if (this.faqFilter === 3) {
          return (
            faq.categoryId === 3 &&
            (faq.question
              .toLowerCase()
              .includes(this.faqSearchQuery.toLowerCase()) ||
              faq.ans.toLowerCase().includes(this.faqSearchQuery.toLowerCase()))
          );
        } else if (this.faqFilter === 4) {
          return (
            faq.categoryId === 4 &&
            (faq.question
              .toLowerCase()
              .includes(this.faqSearchQuery.toLowerCase()) ||
              faq.ans.toLowerCase().includes(this.faqSearchQuery.toLowerCase()))
          );
        } else if (this.faqFilter === 5) {
          return (
            faq.categoryId === 5 &&
            (faq.question
              .toLowerCase()
              .includes(this.faqSearchQuery.toLowerCase()) ||
              faq.ans.toLowerCase().includes(this.faqSearchQuery.toLowerCase()))
          );
        }
      });
    }
  },
  methods: {},
  components: {}
};
</script>

<style lang="scss">
#faq-page {
  .faq-jumbotron-content {
    background-image: url("../assets/images/pages/faq.jpg");
    background-size: cover;
  }

  .faq-bg {
    background-color: #fff;
  }
}
</style>
