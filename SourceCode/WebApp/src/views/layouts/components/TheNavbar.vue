<!-- =========================================================================================
	File Name: TheNavbar.vue
	Description: Navbar component
	Component Name: TheNavbar
	----------------------------------------------------------------------------------------
	Item Name: Vuesax Admin - VueJS Dashboard Admin Template
	Author: Pixinvent
	Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div class="relative">
    <div class="vx-navbar-wrapper">
      <vs-navbar class="vx-navbar navbar-custom" :color="navbarColor" :class="classObj">
        <!-- SM - OPEN SIDEBAR BUTTON -->
        <feather-icon
          class="sm:inline-flex xl:hidden cursor-pointer mr-1"
          icon="MenuIcon"
          @click.stop="showSidebar"
        ></feather-icon>

        <template v-if="breakpoint !== 'md'">
          <!-- STARRED PAGES - FIRST 10 -->
          <ul class="vx-navbar__starred-pages">
            <draggable
              v-model="starredPagesLimited"
              :group="{name: 'pinList'}"
              class="flex cursor-move"
            >
              <li class="starred-page" v-for="page in starredPagesLimited" :key="page.url">
                <vx-tooltip :text="page.label" position="bottom" delay=".3s">
                  <feather-icon
                    svgClasses="h-6 w-6"
                    class="p-2 cursor-pointer"
                    :icon="page.labelIcon"
                    @click="$router.push(page.url)"
                  ></feather-icon>
                </vx-tooltip>
              </li>
            </draggable>
          </ul>

          <!-- STARRED PAGES MORE -->
          <div class="vx-navbar__starred-pages--more-dropdown" v-if="starredPagesMore.length">
            <vs-dropdown vs-custom-content vs-trigger-click>
              <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4" class="cursor-pointer p-2"></feather-icon>
              <vs-dropdown-menu>
                <ul class="vx-navbar__starred-pages-more--list">
                  <draggable
                    v-model="starredPagesMore"
                    :group="{name: 'pinList'}"
                    class="cursor-move"
                  >
                    <li
                      class="starred-page--more flex items-center cursor-pointer"
                      v-for="page in starredPagesMore"
                      :key="page.url"
                      @click="$router.push(page.url)"
                    >
                      <feather-icon svgClasses="h-5 w-5" class="ml-2 mr-1" :icon="page.labelIcon"></feather-icon>
                      <span class="px-2 pt-2 pb-1">{{ page.label }}</span>
                    </li>
                  </draggable>
                </ul>
              </vs-dropdown-menu>
            </vs-dropdown>
          </div>
        </template>

        <vs-spacer></vs-spacer>

        <!-- SEARCHBAR -->
        <div
          class="search-full-container w-full h-full absolute pin-l rounded-lg"
          :class="{'flex': showFullSearch}"
          v-show="showFullSearch"
        >
          <vx-auto-suggest
            :autoFocus="showFullSearch"
            :data="navbarSearchAndPinList"
            @selected="selected"
            ref="navbarSearch"
            @closeSearchbar="showFullSearch = false"
            placeholder="Search..."
            class="w-full"
            inputClassses="w-full vs-input-no-border vs-input-no-shdow-focus no-icon-border"
            icon="SearchIcon"
            background-overlay
          ></vx-auto-suggest>
          <div class="absolute pin-r h-full z-50">
            <feather-icon
              icon="XIcon"
              class="px-4 cursor-pointer h-full close-search-icon"
              @click="showFullSearch = false"
            ></feather-icon>
          </div>
        </div>
        <!-- <feather-icon
          icon="SearchIcon"
          @click="showFullSearch = true"
          class="cursor-pointer navbar-fuzzy-search ml-4"
        ></feather-icon>-->

        <!-- NOTIFICATIONS -->
        <vs-dropdown vs-custom-content vs-trigger-click class="cursor-pointer">
          <feather-icon
            icon="BellIcon"
            class="cursor-pointer mt-1 ml-4"
            :badge="unreadNotifications.length"
          ></feather-icon>
          <vs-dropdown-menu class="notification-dropdown dropdown-custom">
            <div class="notification-top text-center p-5 bg-primary text-white">
              <h3 class="text-white">{{ unreadNotifications.length }} Mới</h3>
              <p class="opacity-75">Thông báo</p>
            </div>

            <VuePerfectScrollbar
              ref="mainSidebarPs"
              class="scroll-area--nofications-dropdown p-0 mb-10"
              :settings="settings"
            >
              <ul class="bordered-items">
                <li
                  v-for="ntf in unreadNotifications"
                  :key="ntf.index"
                  class="flex justify-between px-4 py-4 notification cursor-pointer"
                >
                  <div class="flex items-start">
                    <feather-icon
                      :icon="ntf.icon"
                      :svgClasses="[`text-${ntf.category}`, 'stroke-current mr-1 h-6 w-6']"
                    ></feather-icon>
                    <div class="mx-2">
                      <span
                        class="font-medium block notification-title"
                        :class="[`text-${ntf.category}`]"
                      >{{ ntf.title }}</span>
                      <small>{{ ntf.msg }}</small>
                    </div>
                  </div>
                  <small class="mt-1 whitespace-no-wrap">{{ elapsedTime(ntf.time) }}</small>
                </li>
              </ul>
            </VuePerfectScrollbar>
            <div
              class="checkout-footer fixed pin-b rounded-b-lg text-primary w-full p-2 font-semibold text-center border border-b-0 border-l-0 border-r-0 border-solid border-grey-light cursor-pointer"
            >
              <span>Xem tất cả thông báo</span>
            </div>
          </vs-dropdown-menu>
        </vs-dropdown>

        <!-- USER META -->
        <div class="the-navbar__user-meta flex items-center sm:ml-5 ml-2">
          <div class="text-right leading-tight">
            <p class="font-semibold">{{ $localStorage.getItem("fullname") }}</p>
            <small>{{ $localStorage.getItem("email") }}</small>
          </div>
          <vs-dropdown vs-custom-content vs-trigger-click class="cursor-pointer">
            <div class="con-img ml-3">
              <img
                :src="require(`@/assets/images/avatar/avatar.png`)"
                alt
                width="40"
                height="40"
                class="rounded-full shadow-md cursor-pointer block"
              >
            </div>
            <vs-dropdown-menu>
              <ul style="min-width: 9rem">
                <li
                  class="flex py-2 px-4 cursor-pointer hover:bg-primary hover:text-white"
                  @click="$router.push('/pages/profile')"
                >
                  <feather-icon icon="UserIcon" svgClasses="w-4 h-4"></feather-icon>
                  <span class="ml-2">Hồ sơ</span>
                </li>
                <li
                  class="flex py-2 px-4 cursor-pointer hover:bg-primary hover:text-white"
                  @click="$router.push('/apps/email')"
                >
                  <feather-icon icon="MailIcon" svgClasses="w-4 h-4"></feather-icon>
                  <span class="ml-2">Hộp thư</span>
                </li>
                <li
                  class="flex py-2 px-4 cursor-pointer hover:bg-primary hover:text-white"
                  @click="$router.push('/apps/chat')"
                >
                  <feather-icon icon="MessageSquareIcon" svgClasses="w-4 h-4"></feather-icon>
                  <span class="ml-2">Tin nhắn</span>
                </li>
                <vs-divider class="m-1"></vs-divider>
                <li
                  class="flex py-2 px-4 cursor-pointer hover:bg-primary hover:text-white"
                  @click="doLogout"
                >
                  <feather-icon icon="LogOutIcon" svgClasses="w-4 h-4"></feather-icon>
                  <span class="ml-2">Đăng xuất</span>
                </li>
              </ul>
            </vs-dropdown-menu>
          </vs-dropdown>
        </div>
      </vs-navbar>
    </div>
  </div>
</template>

<script>
import VxAutoSuggest from "@/views/components/vx-auto-suggest/VxAutoSuggest.vue";
import VuePerfectScrollbar from "vue-perfect-scrollbar";
import draggable from "vuedraggable";

import { setTimeout } from "timers";

import auth from "@auth";

export default {
  name: "the-navbar",
  props: {
    navbarColor: {
      type: String,
      default: "#fff"
    }
  },
  data() {
    return {
      navbarSearchAndPinList: this.$store.state.navbarSearchAndPinList,
      searchQuery: "",
      showFullSearch: false,
      unreadNotifications: auth.isAdmin()
        ? []
        : [
            {
              index: 0,
              title: "Japanese Elementary 3",
              msg:
                "Đã tìm thấy sinh viên trả sách Japanese Elementary 3, liên lạc để mượn lại",
              icon: "PackageIcon",
              time: "Thu May 16 2019 08:45:00 GMT+0700 (GMT)",
              category: "success"
            }
          ],
      settings: {
        // perfectscrollbar settings
        maxScrollbarLength: 60,
        wheelSpeed: 0.6
      },
      autoFocusSearch: false,
      showBookmarkPagesDropdown: false
    };
  },
  watch: {
    $route() {
      if (this.showBookmarkPagesDropdown) {
        this.showBookmarkPagesDropdown = false;
      }
    }
  },
  computed: {
    // HELPER
    sidebarWidth() {
      return this.$store.state.sidebarWidth;
    },
    breakpoint() {
      return this.$store.state.breakpoint;
    },

    // NAVBAR STYLE
    classObj() {
      if (this.sidebarWidth === "default") return "navbar-default";
      else if (this.sidebarWidth === "reduced") return "navbar-reduced";
      return "navbar-full";
    },

    // BOOKMARK & SEARCH
    data() {
      return this.$store.state.navbarSearchAndPinList;
    },
    starredPages() {
      return this.$store.state.starredPages;
    },
    starredPagesLimited: {
      get() {
        return this.starredPages.slice(0, 10);
      },
      set(list) {
        this.$store.dispatch("arrangeStarredPagesLimited", list);
      }
    },
    starredPagesMore: {
      get() {
        return this.starredPages.slice(10);
      },
      set(list) {
        this.$store.dispatch("arrangeStarredPagesMore", list);
      }
    },

    // CART DROPDOWN
    cartItems() {
      return this.$store.state.eCommerce.cartItems.slice().reverse();
    },

    // PROFILE
    activeUserInfo() {
      return this.$store.state.AppActiveUser;
    }
  },
  methods: {
    showSidebar() {
      this.$store.commit("TOGGLE_IS_SIDEBAR_ACTIVE", true);
    },
    selected(item) {
      this.$router.push(item.url);
      this.showFullSearch = false;
    },
    actionClicked(item) {
      // e.stopPropogation();
      this.$store.dispatch("updateStarredPage", {
        index: item.index,
        val: !item.highlightAction
      });
    },
    showNavbarSearch() {
      this.showFullSearch = true;
    },
    showSearchbar() {
      this.showFullSearch = true;
    },
    elapsedTime(startTime) {
      let x = new Date(startTime);
      let now = new Date();
      var timeDiff = now - x;
      timeDiff /= 1000;

      var seconds = Math.round(timeDiff);
      timeDiff = Math.floor(timeDiff / 60);

      var minutes = Math.round(timeDiff % 60);
      timeDiff = Math.floor(timeDiff / 60);

      var hours = Math.round(timeDiff % 24);
      timeDiff = Math.floor(timeDiff / 24);

      var days = Math.round(timeDiff % 365);
      timeDiff = Math.floor(timeDiff / 365);

      var years = timeDiff;

      if (years > 0) {
        return years + (years > 1 ? " năm " : " năm ") + "trước";
      } else if (days > 0) {
        return days + (days > 1 ? " ngày " : " ngày ") + "trước";
      } else if (hours > 0) {
        return hours + (hours > 1 ? " giờ " : " giờ ") + "trước";
      } else if (minutes > 0) {
        return minutes + (minutes > 1 ? " phút " : " phút ") + "trước";
      } else if (seconds > 0) {
        return seconds + (seconds > 1 ? `${seconds} giây trước` : "mới đây");
      }

      return "Just Now";
    },
    outside: function() {
      this.showBookmarkPagesDropdown = false;
    },
    doLogout: function() {
      this.$vs.loading({
        type: "corners",
        color: "white",
        background: "primary"
      });

      setTimeout(
        function() {
          this.$auth.clearAuth();
          this.$localStorage.clear();
          this.$router.push("/login");

          this.$vs.loading.close();
        }.bind(this),
        500
      );
    },
    doSwitch: function() {
      this.$router.push("/switch");
    }
  },
  directives: {
    "click-outside": {
      bind: function(el, binding) {
        const bubble = binding.modifiers.bubble;
        const handler = e => {
          if (bubble || (!el.contains(e.target) && el !== e.target)) {
            binding.value(e);
          }
        };
        el.__vueClickOutside__ = handler;
        document.addEventListener("click", handler);
      },

      unbind: function(el) {
        document.removeEventListener("click", el.__vueClickOutside__);
        el.__vueClickOutside__ = null;
      }
    }
  },
  components: {
    VxAutoSuggest,
    VuePerfectScrollbar,
    draggable
  }
};
</script>
