<template>
  <div id="chat-app" class="border border-solid border-grey-light rounded relative overflow-hidden">
    <vs-sidebar
      class="items-no-padding"
      parent="#chat-app"
      :click-not-close="clickNotClose"
      :hidden-background="clickNotClose"
      v-model="isChatSidebarActive"
      id="chat-list-sidebar"
    >
      <!-- USER PROFILE SIDEBAR -->
      <user-profile
        :active="activeProfileSidebar"
        :userId="userProfileId"
        class="user-profile-container"
        @closeProfileSidebar="closeProfileSidebar"
      ></user-profile>

      <div class="chat__profile-search flex p-4">
        <div class="relative inline-flex">
          <vs-avatar
            class="m-0"
            src="https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/27067550_1428489907260025_2312900682853459233_n.jpg?_nc_cat=101&_nc_oc=AQmOAkrRIuIzgBzpIlmO50cSi4vwMWMsRt8jxXURwgypfW3AWsrf5bhXNkpcW9Vj6Ao&_nc_ht=scontent.fsgn5-1.fna&oh=c0e288d5e068edcb69b15b50ecb9300c&oe=5DEE07E0"
            size="40px"
            @click="showProfileSidebar(activeUserId)"
          />
          <div
            class="h-3 w-3 border-white border border-solid rounded-full absolute pin-r pin-b"
            :class="'bg-' + getStatusColor(true)"
          ></div>
        </div>
        <vs-input
          icon="icon-search"
          icon-pack="feather"
          class="w-full mx-5 input-rounded-full no-icon-border"
          placeholder="Tìm kiếm"
          v-model="searchQuery"
        />
      </div>

      <vs-divider class="border-grey-light m-0" />
      <VuePerfectScrollbar class="chat-scroll-area pt-4" :settings="settings">
        <!-- ACTIVE CHATS LIST -->
        <div class="chat__chats-list mb-8">
          <h3 class="text-primary mb-5 px-4">Trao đổi</h3>
          <ul class="chat__active-chats bordered-items">
            <li
              class="cursor-pointer"
              v-for="(contact, index) in sorted"
              :key="index"
              @click="updateActiveChatUser(contact.id)"
            >
              <chat-contact
                :contact="contact"
                :lastMessaged="chatLastMessaged(contact.id).time"
                :lastMessage="chatLastMessaged(contact.id).textContent"
                :unseenMsg="chatUnseenMessages(contact.id)"
                :isActiveChatUser="isActiveChatUser(contact.id)"
              ></chat-contact>
            </li>
          </ul>
        </div>

        <!-- CONTACTS LIST -->
        <div class="chat__contacts">
          <h3 class="text-primary mb-5 px-4">Đã ghép gần đây</h3>
          <ul class="chat__contacts bordered-items">
            <li
              class="cursor-pointer"
              v-for="contact in chatContacts"
              :key="contact.id"
              @click="updateActiveChatUser(contact.id)"
            >
              <chat-contact :contact="contact" :matched="true"></chat-contact>
            </li>
          </ul>
        </div>
      </VuePerfectScrollbar>
    </vs-sidebar>

    <!-- RIGHT COLUMN -->
    <div
      class="chat__bg app-fixed-height chat-content-area border border-solid border-grey-light border-t-0 border-r-0 border-b-0"
      :class="{'sidebar-spacer--wide': clickNotClose, 'flex items-center justify-center': activeChatUser === null}"
    >
      <template v-if="activeChatUser">
        <div class="chat__navbar">
          <chat-navbar
            :isSidebarCollapsed="!clickNotClose"
            :user-id="activeChatUser"
            :isPinnedProp="isChatPinned"
            @openContactsSidebar="toggleChatSidebar(true)"
            @showProfileSidebar="updateUserProfileId"
            @toggleIsChatPinned="toggleIsChatPinned"
          ></chat-navbar>
        </div>
        <VuePerfectScrollbar
          class="chat-content-scroll-area border border-solid border-grey-light"
          :settings="settings"
          ref="chatLogPS"
        >
          <div class="chat__log" ref="chatLog">
            <chat-log :userId="activeChatUser" v-if="activeChatUser"></chat-log>
          </div>
        </VuePerfectScrollbar>
        <div class="chat__input flex p-4 bg-white">
          <vs-input
            class="flex-1"
            placeholder="Nhập tin nhắn muốn gửi"
            v-model="typedMessage"
            @keyup.enter="sendMsg"
          />
          <vs-button class="bg-primary-gradient ml-4" type="filled" @click="sendMsg">Gửi</vs-button>
        </div>
      </template>
      <template v-else>
        <div class="flex flex-col items-center">
          <feather-icon
            icon="MessageSquareIcon"
            class="mb-4 bg-white p-8 shadow-md rounded-full"
            svgClasses="w-16 h-16"
          ></feather-icon>
          <h4
            class="py-2 px-4 bg-white shadow-md rounded-full cursor-pointer"
            @click.stop="toggleChatSidebar(true)"
          >Bắt đầu một cuộc trò chuyện</h4>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import VuePerfectScrollbar from "vue-perfect-scrollbar";
import contacts from "./contacts";
import ChatContact from "./ChatContact.vue";
import UserProfile from "./UserProfile.vue";
import ChatNavbar from "./ChatNavbar.vue";
import ChatLog from "./ChatLog.vue";

export default {
  name: "vx-sidebar",
  data() {
    return {
      active: true,
      isHidden: false,
      contacts: contacts,
      searchContact: "",
      activeProfileSidebar: false,
      activeChatUser: null,
      userProfileId: -1,
      typedMessage: "",
      isChatPinned: false,
      settings: {
        maxScrollbarLength: 60,
        wheelSpeed: 0.3
      },
      clickNotClose: true,
      isChatSidebarActive: true,
      windowWidth: window.innerWidth
    };
  },
  computed: {
    chatLastMessaged() {
      return userId => this.$store.getters["chat/chatLastMessaged"](userId);
    },
    chatUnseenMessages() {
      return userId => {
        const unseenMsg = this.$store.getters["chat/chatUnseenMessages"](
          userId
        );
        if (unseenMsg) return unseenMsg;
      };
    },
    sorted() {
      return this.chats.slice().sort((x, y) => {
        const xId = x.id;
        const yId = y.id;
        const chatDataX = this.$store.getters["chat/chatDataOfUser"](xId);
        const chatDataY = this.$store.getters["chat/chatDataOfUser"](yId);
        return chatDataY.isPinned - chatDataX.isPinned;
      });
    },
    activeUserId() {
      return 0;
    },
    activeUserStatus() {
      return "online";
    },
    getStatusColor() {
      return isActiveUser => {
        const userStatus = this.getUserStatus(isActiveUser);

        if (userStatus === "online") {
          return "success";
        } else if (userStatus === "do not disturb") {
          return "danger";
        } else if (userStatus === "away") {
          return "warning";
        } else {
          return "grey";
        }
      };
    },
    chats() {
      return this.$store.getters["chat/chats"];
    },
    chatContacts() {
      return this.$store.getters["chat/chatcontacts"];
    },
    searchQuery: {
      get() {
        return this.$store.state.chat.chatSearchQuery;
      },
      set(val) {
        this.$store.dispatch("chat/setChatSearchQuery", val);
      }
    },
    isActiveChatUser() {
      return userId => userId === this.activeChatUser;
    }
  },
  methods: {
    getUserStatus(isActiveUser) {
      return isActiveUser
        ? "online"
        : this.contacts[this.activeChatUser].status;
    },
    closeProfileSidebar(value) {
      this.activeProfileSidebar = value;
    },
    updateUserProfileId(userId) {
      this.userProfileId = userId;
      this.activeProfileSidebar = !this.activeProfileSidebar;
    },
    updateActiveChatUser(contactId) {
      this.activeChatUser = contactId;
      if (this.$store.getters["chat/chatDataOfUser"](this.activeChatUser)) {
        this.$store.dispatch("chat/markSeenAllMessages", contactId);
      }
      let chatData = this.$store.getters["chat/chatDataOfUser"](
        this.activeChatUser
      );
      if (chatData) this.isChatPinned = chatData.isPinned;
      else this.isChatPinned = false;
      this.toggleChatSidebar();
    },
    showProfileSidebar(userId) {
      this.userProfileId = userId;
      this.activeProfileSidebar = !this.activeProfileSidebar;
    },
    sendMsg() {
      if (!this.typedMessage) return;
      const payload = {
        isPinned: this.isChatPinned,
        msg: {
          textContent: this.typedMessage,
          time: String(new Date()),
          isSent: true,
          isSeen: false
        },
        id: this.activeChatUser
      };
      this.$store.dispatch("chat/sendChatMessage", payload);
      this.typedMessage = "";
      setTimeout(() => {
        this.$refs.chatLogPS.$el.scrollTop = this.$refs.chatLog.scrollHeight;
      }, 50);
    },
    toggleIsChatPinned(value) {
      this.isChatPinned = value;
    },
    handleWindowResize(event) {
      this.windowWidth = event.currentTarget.innerWidth;
      this.setSidebarWidth();
    },
    setSidebarWidth() {
      if (this.windowWidth < 1200) {
        this.isChatSidebarActive = this.clickNotClose = false;
      } else {
        this.isChatSidebarActive = this.clickNotClose = true;
      }
    },
    toggleChatSidebar(value = false) {
      if (!value && this.clickNotClose) return;
      this.isChatSidebarActive = value;
    },
    destroyPS() {
      this.$refs.chatLogPS.__uninit();
    }
  },
  components: {
    VuePerfectScrollbar,
    ChatContact,
    UserProfile,
    ChatNavbar,
    ChatLog
  },
  created() {
    this.$nextTick(() => {
      window.addEventListener("resize", this.handleWindowResize);
    });
    this.setSidebarWidth();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  mounted() {
    this.$vs.notify({
      title: "Tính năng sẽ ra mắt sớm",
      text: "Tin nhắn đang được phát triển và sẽ sớm ra mắt",
      color: "warning",
      position: "top-center"
    });
  }
};
</script>

<style lang="scss">
@import "@/assets/scss/vuesax/apps/chat.scss";
</style>
