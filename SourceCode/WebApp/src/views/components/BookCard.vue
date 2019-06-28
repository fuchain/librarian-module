<template>
  <vx-card class="grid-view-item mb-base overflow-hidden">
    <template slot="no-body">
      <!-- ITEM IMAGE -->
      <div class="item-img-container bg-white h-48 flex items-center justify-center mb-4">
        <img :src="item.image" :alt="item.name" class="grid-view-img px-4">
      </div>
      <div class="item-details px-4">
        <!-- RATING & PRICE -->
        <div class="flex justify-between items-center" v-if="!item.requestId">
          <div class="bg-primary flex text-white py-1 px-2 rounded" v-if="item.time">
            <span class="text-sm mr-2" v>Mượn {{ item.time | moment("from") }}</span>
            <feather-icon icon="ClockIcon" svgClasses="h-4 w-4"/>
          </div>
          <div class="flex text-white py-1 px-2 rounded" v-else></div>
          <h6 class="font-bold">{{ item.code }}</h6>
        </div>

        <div class="flex justify-between items-center" v-else>
          <div class="bg-primary flex text-white py-1 px-2 rounded">
            <span
              class="text-sm mr-2"
            >{{ item.user && item.user.email && $auth.getNameFromEmail(item.user.email) || "Đang tìm" }}</span>
            <feather-icon icon="UserIcon" svgClasses="h-4 w-4"/>
          </div>
          <h6 class="font-bold">{{ item.code }}</h6>
        </div>

        <vs-progress
          :indeterminate="!item.user"
          :percent="item.user ? 100 : 0"
          color="primary"
          v-if="item.requestId"
        >primary</vs-progress>

        <!-- TITLE & DESCRIPTION -->
        <div class="my-4">
          <h6 class="truncate font-semibold mb-1">{{ item.name }}</h6>
          <p class="item-description truncate text-sm">{{ item.description }}</p>
        </div>
      </div>

      <!-- SLOT: ACTION BUTTONS -->
      <slot name="action-buttons"/>
    </template>
  </vx-card>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true
    }
  }
};
</script>

<style lang="scss" scoped>
.grid-view-item {
  .grid-view-img {
    max-width: 100%;
    max-height: 100%;
    width: auto;
    transition: 0.35s;
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0px 4px 25px 0px rgba(0, 0, 0, 0.25);

    .grid-view-img {
      opacity: 0.9;
    }
  }
}
</style>
