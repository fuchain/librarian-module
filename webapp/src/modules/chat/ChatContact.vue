<template>
  <div
    class="chat__contact flex items-center px-2 py-3"
    :class="{'bg-primary-gradient text-white shadow-lg': isActiveChatUser}"
  >
    <div class="contact__avatar mr-3">
      <vs-avatar class="border-2 border-solid border-white" :src="contact.img" size="42px"></vs-avatar>
    </div>
    <div class="contact__container w-full flex items-center justify-between overflow-hidden">
      <div class="contact__info flex flex-col truncate w-5/6">
        <h5 class="font-semibold" :class="{'text-white': isActiveChatUser}">{{ contact.name }}</h5>
        <span class="truncate">{{ matched ? "Sẽ chuyển sách cho bạn" : lastMessage }}</span>
      </div>

      <div class="chat__contact__meta flex self-start flex-col items-end w-1/6">
        <span class="whitespace-no-wrap">{{ toDate(lastMessaged) }}</span>
        <vs-avatar color="primary" :text="`${unseenMsg}`" size="20px" v-if="unseenMsg" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    contact: {
      type: Object,
      required: true
    },
    lastMessaged: {
      type: String,
      default: ""
    },
    lastMessage: {
      type: String,
      default: "Chưa có tin nhắn"
    },
    unseenMsg: {
      type: Number,
      default: 0
    },
    isActiveChatUser: {
      type: Boolean
    },
    matched: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    toDate(time) {
      if (!time) return "2 Thg 7";
      const locale = "vi-vn";
      const date_obj = new Date(Date.parse(time));
      const monthName = date_obj.toLocaleString(locale, {
        month: "short"
      });
      return date_obj.getDate() + " " + monthName;
    }
  }
};
</script>
