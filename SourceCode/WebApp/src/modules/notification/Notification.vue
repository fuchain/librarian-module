<template>
  <vx-card title="Thông báo của bạn">
    <vs-list v-if="notifications.length">
      <vs-list-header icon-pack="feather" icon="icon-calendar" title="Hôm nay"></vs-list-header>
      <span
        style="cursor: pointer;"
        v-for="item in notifications"
        :key="item.time"
        @click="redirect(item.type)"
      >
        <vs-list-item :subtitle="item.message + ' - ' + getTime(item.time)"></vs-list-item>
      </span>
    </vs-list>
    <div v-else>Bạn chưa có thông báo nào.</div>
  </vx-card>
</template>

<script>
import redirect from "@core/socket/redirect";
import moment from "moment";

export default {
  computed: {
    notifications() {
      return this.$store.state.notifications;
    }
  },
  methods: {
    redirect(type) {
      redirect(type);
    },
    getTime(time) {
      return moment(time).fromNow();
    }
  }
};
</script>

<style>
.vs-list--item .list-titles .vs-list--subtitle {
  font-size: 1rem !important;
}
</style>


