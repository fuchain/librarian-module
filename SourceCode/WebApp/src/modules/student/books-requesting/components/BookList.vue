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

                <span class="text-sm font-semibold ml-2" @click="beginConfirm">ĐÃ NHẬN SÁCH</span>
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

    <vs-popup title="Xác nhận đã nhận sách" :active.sync="popupActive">
      <div class="mb-4">
        <vs-input size="large" class="w-full" placeholder="Xác nhận mã PIN" v-model="pin"/>
      </div>
      <div>
        <vs-button class="w-full" @click="validateConfirm">Đã nhận sách</vs-button>
      </div>
    </vs-popup>
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
      popupActive: false,
      pin: ""
    };
  },
  computed: {
    wishListitems() {
      return [
        {
          objectID: 7,
          name: "Japanese Elementary 3",
          description:
            "Japanese Elementary 3 for Japanese Elementary 3 in FPT University",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          user: "SE62533",
          code: "JPD131"
        },
        {
          objectID: 8,
          name: "Start Your Business",
          description:
            "Start Your Business for Start Your Business in FPT University",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          code: "SYB301"
        }
      ];
    }
  },
  methods: {
    triggerCall(check) {
      if (!check) return;
      window.location.href = "tel:0796870446";
    },
    async fakeLoad() {
      return new Promise((resolve, reject) => {
        this.$vs.loading();
        setTimeout(
          function() {
            this.$vs.loading.close();
            resolve();
          }.bind(this),
          3000
        );
      });
    },
    async beginConfirm() {
      this.popupActive = true;
    },
    async validateConfirm() {
      await this.fakeLoad();

      this.$vs.notify({
        title: "Lỗi",
        text: "Mã PIN không hợp lệ",
        color: "warning",
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
