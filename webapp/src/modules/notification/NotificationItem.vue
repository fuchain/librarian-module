<template>
  <div class="px-8 py-4 list-item-component bg-primary text-white">
    <div class="vx-row" @click="redirect(item)">
      <div class="vx-col w-full sm:w-5/6 flex sm:items-center sm:flex-row flex-col">
        <div class="flex items-center">
          <h6 class="todo-title text-white">Thông báo từ thư viện</h6>
        </div>
      </div>

      <div class="vx-col w-full sm:w-1/6 ml-auto flex sm:justify-end">
        <span style="font-size: 0.8rem;">{{ getTime(item.time) }}</span>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col w-full">
        <p class="mt-2">{{ item.message }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import redirect from "@core/socket/redirect";
import moment from "moment";

export default {
  props: {
    item: Object
  },
  data() {
    return {};
  },
  methods: {
    redirect(item) {
      redirect(item);
    },
    getTime(time) {
      return moment(time).fromNow();
    }
  }
};
</script>
