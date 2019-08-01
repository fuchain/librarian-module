<template>
  <div id="parentx-demo-2">
    <vs-sidebar
      parent="#chat-app"
      :position-right="!isActiveUser"
      :hidden-background="false"
      v-model="activeLocal"
      id="chat-profile-sidebar"
      class="items-no-padding"
    >
      <div class="header-sidebar relative flex flex-col p-0" slot="header">
        <feather-icon
          icon="XIcon"
          svgClasses="m-2 cursor-pointer absolute pin-t pin-r"
          @click="$emit('closeProfileSidebar', false)"
        ></feather-icon>

        <div class="relative inline-flex mx-auto mb-5 mt-6">
          <vs-avatar class="m-0" :src="userImg" size="70px" />
          <div
            class="h-5 w-5 border-white border-2 border-solid rounded-full absolute pin-r pin-b"
            :class="'bg-' + getStatusColor(isActiveUser)"
          ></div>
        </div>
        <h4 class="mr-2 self-center">{{ userName }}</h4>
      </div>
    </vs-sidebar>
  </div>
</template>

<script>
import contacts from "./contacts.js";

export default {
  props: {
    userId: {
      type: Number,
      required: true
    },
    active: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      contacts: contacts,
      counterDanger: false,
      settings: {
        // perfectscrollbar settings
        maxScrollbarLength: 60,
        wheelSpeed: 0.6
      }
    };
  },
  computed: {
    isActiveUser() {
      return this.contactIndex === -1;
    },
    activeLocal: {
      get() {
        return this.active;
      },
      set(value) {
        this.$emit("closeProfileSidebar", value);
      }
    },
    about: {
      get() {
        if (this.contactIndex === -1) {
          return "Dessert chocolate cake lemon drops jujubes. Biscuit cupcake ice cream bear claw brownie brownie marshmallow.";
        } else {
          return this.contacts[this.contactIndex].about;
        }
      },
      set(value) {
        if (value.length > 120) {
          value = value.substring(0, 120);
        }
        this.$store.dispatch("chat/updateAboutChat", value);
      }
    },
    contactIndex() {
      return contacts.findIndex(contact => contact.id === this.userId);
    },
    status: {
      get() {
        if (this.contactIndex === -1) {
          return "status";
        }

        return null;
      },
      set(value) {
        if (this.contactIndex === -1) {
          this.$store.dispatch("chat/updateStatusChat", value);
        }
      }
    },
    userImg() {
      if (this.contactIndex === -1) {
        return "https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/27067550_1428489907260025_2312900682853459233_n.jpg?_nc_cat=101&_nc_oc=AQmOAkrRIuIzgBzpIlmO50cSi4vwMWMsRt8jxXURwgypfW3AWsrf5bhXNkpcW9Vj6Ao&_nc_ht=scontent.fsgn5-1.fna&oh=c0e288d5e068edcb69b15b50ecb9300c&oe=5DEE07E0";
      } else {
        return this.contacts[this.contactIndex].img;
      }
    },
    userName() {
      if (this.contactIndex === -1) {
        return "Huỳnh Minh Tú";
      } else {
        return this.contacts[this.contactIndex].name;
      }
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
    }
  },
  methods: {
    getUserStatus(isActiveUser) {
      return isActiveUser ? "online" : this.contacts[this.contactIndex].status;
    }
  }
};
</script>
