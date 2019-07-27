<template>
  <vx-card title="Thông báo của bạn">
    <div
      id="todo-app"
      class="border border-solid border-grey-light rounded relative overflow-hidden"
    >
      <div :class="sidebar-spacer + ' bg-white'">
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
  </vx-card>
</template>

<script>
import NotificationItem from "./NotificationItem.vue";

export default {
  computed: {
    notifications() {
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