<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">Sách đang trả</h2>
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
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span
                  class="text-sm font-semibold ml-2"
                  @click="$router.push('/books/confirm')"
                >XÁC NHẬN</span>
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
  computed: {
    wishListitems() {
      return [
        {
          objectID: 4,
          name: "Introduce to Software Engineering",
          description:
            "Introduce to Software Engineering for Introduce to Software Engineering in FPT University",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          time: "4 ngày",
          code: "SWE101"
        }
      ];
    }
  },
  methods: {}
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
