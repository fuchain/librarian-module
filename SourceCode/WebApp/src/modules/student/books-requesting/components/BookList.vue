<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">Sách đang yêu cầu</h2>
    <div class="items-grid-view vx-row match-height" v-if="wishListitems.length" appear>
      <div
        class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full"
        v-for="item in wishListitems"
        :key="item.objectID"
      >
        <item-grid-view :item="item">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-primary-action-btn p-3 flex flex-grow items-center justify-center cursor-pointer"
                v-if="item.user"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="openConfirm">ĐÃ NHẬN SÁCH</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="BookOpenIcon" svgClasses="h-4 w-4" v-if="!item.user"/>
                <feather-icon icon="PhoneIncomingIcon" svgClasses="h-4 w-4" v-if="item.user"/>

                <span
                  class="text-sm font-semibold ml-2"
                  @click="triggerCall(item.user ? true : false)"
                >{{ item.user ? "LIÊN LẠC" : "CHI TIẾT" }}</span>
              </div>
            </div>
          </template>
        </item-grid-view>
      </div>
    </div>

    <vx-card title="Bạn đang không yêu cầu mượn sách nào." v-else>
      <vs-button @click="$router.push('/books/request')">Yêu cầu mượn sách</vs-button>
    </vx-card>
  </div>
</template>

<script>
const ItemGridView = () => import("./ItemGridView.vue");

export default {
  components: {
    ItemGridView
  },
  computed: {
    wishListitems() {
      return [
        {
          objectID: 4,
          name: "Advance Java (Java Web)",
          description:
            "Best subject in FU. Goodluck with this subject, hope you can pass it!",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          user: "SE62533",
          code: "PRJ321"
        },
        {
          objectID: 7,
          name: "Software Project Management",
          description:
            "Jack Welch knows how to win. During his 40-year career at General Electric, he led the company to year-after-year success around the globe, in multiple markets, against brutal competition. His honest, be-the-best style of management became the gold standard in business, with his relentless focus on people, teamwork and profits.",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          code: "SWM301"
        }
      ];
    }
  },
  methods: {
    triggerCall(check) {
      if (!check) return;
      window.location.href = "tel:0796870446";
    },
    openConfirm() {
      this.$vs.dialog({
        type: "confirm",
        color: "primary",
        title: "Xác nhận",
        text:
          "Bạn có chắc là bạn đã nhận được sách, bạn đã kiểm tra tình trạng quyển sách chưa?",
        accept: this.acceptAlert
      });
    },
    acceptAlert(color) {
      this.$router.push("/books");

      this.$vs.notify({
        color: "primary",
        title: "Thành công",
        text: "Đã xác nhận mượn thành công sách",
        position: "top-center"
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
