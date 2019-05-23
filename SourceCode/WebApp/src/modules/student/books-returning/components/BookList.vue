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
                v-if="item.user"
              >
                <feather-icon icon="CheckIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="beginConfirm">ĐÃ TRẢ</span>
              </div>

              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              >
                <feather-icon icon="PhoneIncomingIcon" svgClasses="h-4 w-4"/>

                <span class="text-sm font-semibold ml-2" @click="triggerCall()">LIÊN LẠC</span>
              </div>
            </div>
          </template>
        </item-grid-view>
      </div>
    </div>

    <vx-card title="Bạn đang không giữ sách nào." v-else>
      <vs-button @click="$router.push('/books/request')">Mượn sách</vs-button>
    </vx-card>

    <vs-popup title="Người nhận xác nhận" :active.sync="popupActive">
      <div style="font-size: 1.5rem; text-align: center;">Mã số PIN xác nhận</div>
      <div style="font-size: 3rem; text-align: center;">123456</div>
      <div style="text-align: center; margin-bottom: 1rem;">
        Chỉ tồn tại trong
        <strong>60 giây</strong>
      </div>
      <div style="text-align: center;">
        <vs-button @click="validateConfirm">Đã xong</vs-button>
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
      wishListitems: [
        {
          objectID: 5,
          name: "Introduction to Software Engineering",
          description:
            "Introduction to Software Engineering for Introduction to Software Engineering in FPT University",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          time: "4 ngày",
          code: "SWE102",
          user: "SE62535"
        },
        {
          objectID: 6,
          name: "Computer Networking",
          description:
            "Computer Networking for Computer Networking in FPT University",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          time: "4 ngày",
          code: "NWC202"
        }
      ],
      popupActive: false
    };
  },
  methods: {
    async fakeLoad() {
      return new Promise((resolve, reject) => {
        this.$vs.loading();
        setTimeout(
          function() {
            this.$vs.loading.close();
            resolve();
          }.bind(this),
          2000
        );
      });
    },
    triggerCall() {
      window.location.href = "tel:0796870446";
    },
    async beginConfirm() {
      await this.fakeLoad();

      this.popupActive = true;
    },
    async validateConfirm() {
      await this.fakeLoad();

      this.$vs.notify({
        title: "Lỗi",
        text: "Người nhận chưa xác nhận mã PIN",
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
