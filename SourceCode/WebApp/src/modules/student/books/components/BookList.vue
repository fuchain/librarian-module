<template>
  <div id="ecommerce-wishlist-demo">
    <h2 class="mb-6">Sách đang giữ</h2>
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
                <feather-icon icon="XIcon" svgClasses="h-4 w-4"/>

                <span
                  class="text-sm font-semibold ml-2"
                  @click="$router.push('/books/return')"
                >TRẢ SÁCH</span>
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
          objectID: 1,
          name: "Never Split The Difference",
          description:
            "A field-tested, game-changing approach to high-stakes negotiations-whether in the boardroom or at home.",
          image:
            "https://salt.tikicdn.com/cache/w1200/ts/product/08/8b/39/211e3c14160851aa742f59a342476892.jpg",
          time: "12 ngày",
          code: "NWC101"
        },
        {
          objectID: 2,
          name: "Mark Zuckerberg: In His Own Words",
          description:
            "Mark Zuckerberg: In His Own Words details the visionary thoughts and opinions of Facebook's founder entirely through direct quotations from Zuckerberg himself. It is an intimate and authoritative look at the man behind Facebook's once-in-a-generation success. This book serves up his most thought-provoking insights, as researched and chosen by George Beahm, the New York Times bestselling editor of I, Steve: Steve Jobs In His Own Words. Mark Zuckerberg: In His Own Words provides crucial illumination of Zuckerberg and the company he's created, emphasizing insights, business strategies, and lessons learned. It is essential reading for people who seek innovative solutions applicable to their business, regardless of size, and makes an ideal gift or reference item for anyone interested in this American business icon.",
          image:
            "https://salt.tikicdn.com/cache/w1200/ts/product/65/b3/10/efcc9cba254c26baed7cb202eba745fd.jpg",
          time: "12 ngày",
          code: "NWC101"
        },
        {
          objectID: 3,
          name: "Jeff Bezos: In His Own Words",
          description:
            "Jeff Bezos started Amazon in 1994 as an online bookstore based out of his garage. Since then, the ever-expanding enterprise has revolutionized shopping and, in many important ways, invented e-commerce as we know it. Today, Amazon is the third-most valuable company in the world, and Bezos's vast customer-oriented empire has mushroomed to include everything from cloud computing and fresh food delivery to movie production and consumer electronics. In recent years, Bezos also has invested in rocket technology, newspaper publishing, and artificial intelligence. Every arm of Bezos's business, however, is guided by a fundamental goal: to give customers what they want before they even think to ask for it. Jeff Bezos: In His Own Words offers a unique look into the mind of one of the world's most successful entrepreneurs by collecting more than 500 of Bezos's quotes on business, technology, customer service, e-commerce, innovation, entrepreneurship, and more. Meticulously curated from interviews, speeches, shareholder letters, press releases, and other sources, this book creates a comprehensive picture of Jeff Bezos, his obsessions, and what makes his ventures thrive.",
          image:
            "https://salt.tikicdn.com/cache/w1200/ts/product/b1/e6/a3/cd891bd031eb3503f375048414cf0843.jpg",
          time: "12 ngày",
          code: "NWC101"
        },
        {
          objectID: 4,
          name: "Computer Networking",
          description:
            "Computer Networking for Computer Networking in FPT University",
          image: "https://i.imgur.com/2j6B1n5.jpg",
          time: "12 ngày",
          code: "NWC101"
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
