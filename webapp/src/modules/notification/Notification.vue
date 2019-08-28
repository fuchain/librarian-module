<template>
  <div id="todo-app" class="border border-solid border-grey-light rounded relative overflow-hidden">
    <div class="bg-white'">
      <div class="flex items-center app-search-container">
        <vs-input
          size="large"
          icon-pack="feather"
          icon="icon-search"
          placeholder="Tìm kiếm thông báo..."
          v-model="searchText"
          class="vs-input-no-border vs-input-no-shdow-focus no-icon-border w-full"
        />
      </div>
      <transition-group class="todo-list" name="list-enter-up" tag="ul" appear>
        <li
          class="cursor-pointer todo_todo-item"
          v-for="item in notifications"
          :key="item.time"
          :style="[{transitionDelay: (index * 0.05) + 's'}]"
        >
          <notification-item :item="item"></notification-item>
        </li>
      </transition-group>
    </div>
  </div>
</template>

<script>
import NotificationItem from "./NotificationItem.vue";

export default {
  computed: {
    notifications() {
      if (
        this.$store.state.notifications &&
        this.$store.state.notifications.length &&
        this.searchText &&
        this.searchText.trim()
      ) {
        return this.$store.state.notifications.filter(e =>
          e.message.toLowerCase().includes(this.searchText.toLowerCase())
        );
      }

      return this.$store.state.notifications;
    }
  },
  data() {
    return {
      searchText: ""
    };
  },
  components: {
    NotificationItem
  }
};
</script>

<style lang="scss">
@import "@/assets/scss/vuesax/apps/todo.scss";
</style>
