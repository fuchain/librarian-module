<!-- hasSentPreviousMsg -->
<template>
  <div id="component-chat-log" class="m-8" v-if="chatData">
    <div v-for="(msg, index) in chatData.msg" class="msg-grp-container" :key="index">
      <!-- If previouse msg is older than current time -->
      <template v-if="chatData.msg[index-1]">
        <vs-divider v-if="!isSameDay(msg.time, chatData.msg[index-1].time)">
          <span>{{ toDate(msg.time) }}</span>
        </vs-divider>
        <div
          class="spacer mt-8"
          v-if="!hasSentPreviousMsg(chatData.msg[index-1].isSent, msg.isSent)"
        ></div>
      </template>

      <div class="flex items-start" :class="[{'flex-row-reverse' : msg.isSent}]">
        <template v-if="chatData.msg[index-1]">
          <template
            v-if="(!hasSentPreviousMsg(chatData.msg[index-1].isSent, msg.isSent) || !isSameDay(msg.time, chatData.msg[index-1].time))"
          >
            <vs-avatar
              size="40px"
              class="m-0 flex-no-shrink"
              :class="msg.isSent ? 'sm:ml-5 ml-3' : 'sm:mr-5 mr-3'"
              :src="senderImg(msg.isSent)"
            ></vs-avatar>
          </template>
        </template>

        <template v-if="index == 0">
          <vs-avatar
            size="40px"
            class="m-0 flex-no-shrink"
            :class="msg.isSent ? 'sm:ml-5 ml-3' : 'sm:mr-5 mr-3'"
            :src="senderImg(msg.isSent)"
          ></vs-avatar>
        </template>

        <template v-if="chatData.msg[index-1]">
          <div
            class="mr-16"
            v-if="!(!hasSentPreviousMsg(chatData.msg[index-1].isSent, msg.isSent) || !isSameDay(msg.time, chatData.msg[index-1].time))"
          ></div>
        </template>

        <div
          class="msg break-words relative shadow-md rounded py-3 px-4 mb-2 rounded-lg max-w-sm"
          :class="{'bg-primary-gradient text-white': msg.isSent, 'border border-solid border-grey-light bg-white': !msg.isSent}"
        >
          <span>{{ msg.textContent }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import contacts from "./contacts";

export default {
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      contacts: contacts
    };
  },
  computed: {
    chatData() {
      return this.$store.getters["chat/chatDataOfUser"](this.userId);
    },
    contactIndex() {
      return contacts.findIndex(contact => contact.id == this.userId);
    },
    userImg() {
      if (this.contactIndex !== -1) return this.contacts[this.contactIndex].img;
    },
    senderImg() {
      return isSentByActiveUser => {
        if (isSentByActiveUser) { return "https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/27067550_1428489907260025_2312900682853459233_n.jpg?_nc_cat=101&_nc_oc=AQmOAkrRIuIzgBzpIlmO50cSi4vwMWMsRt8jxXURwgypfW3AWsrf5bhXNkpcW9Vj6Ao&_nc_ht=scontent.fsgn5-1.fna&oh=c0e288d5e068edcb69b15b50ecb9300c&oe=5DEE07E0"; } else return this.contacts[this.contactIndex].img;
      };
    },
    hasSentPreviousMsg() {
      return (last_sender, current_sender) => last_sender == current_sender;
    }
  },
  methods: {
    isSameDay(time_to, time_from) {
      const date_time_to = new Date(Date.parse(time_to));
      const date_time_from = new Date(Date.parse(time_from));
      return (
        date_time_to.getFullYear() === date_time_from.getFullYear() &&
        date_time_to.getMonth() === date_time_from.getMonth() &&
        date_time_to.getDate() === date_time_from.getDate()
      );
    },
    toDate(time) {
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
