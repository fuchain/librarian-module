<template>
  <vs-popup title="Thông tin sách" :active.sync="isActive" v-if="details && details.name">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Tên sách</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ details.name }}</div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span></span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <img :src="details.thumbnail || '/images/book-thumbnail.jpg'" style="max-width: 150px;" />
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Mô tả</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ details.description }}</div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Môn học</span>
      </div>
      <div
        class="vx-col sm:w-2/3 w-full"
      >{{ details.subject_codes.length && details.subject_codes.join(", ") || "--" }}</div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Danh mục</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ details.categories }}</div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Tác giả</span>
      </div>
      <div
        class="vx-col sm:w-2/3 w-full"
      >{{ details.subject_codes.length && details.authors.join(", ") || "Chưa cập nhật" }}</div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Xuất bản</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ details.published_date }}</div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          class="mr-3 mb-2"
          @click="goToLink(details.preview_link)"
          icon="pageview"
          v-if="details.preview_link"
        >Xem chi tiết sách</vs-button>
      </div>
    </div>
  </vs-popup>
</template>

<script>
import BookCard from "@/views/components/BookCard.vue";

export default {
  computed: {
    details: {
      get() {
        return this.$store.state.detailsPopupData;
      }
    },
    isActive: {
      get() {
        return this.$store.state.detailsPopupOpen;
      },
      set(value) {
        if (value === false) this.$store.dispatch("closeDetailsPopup");
      }
    },
    getTitle() {
      if (this.tx.operation === "TRANSFER") {
        return "Kí xác nhận chuyển sách";
      } else {
        return "Kí xác nhận đã nhận sách";
      }
    }
  },
  methods: {
    goToLink(url) {
      const newTab = window.open(url, "_blank");
      newTab.focus();
    }
  }
};
</script>
