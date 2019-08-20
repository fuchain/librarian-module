<template>
  <vs-popup title="Thông tin sách" :active.sync="isActive" v-if="details && details.name">
    <div class="vx-row mb-6" v-if="assetData && assetData.assetId">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Mã sách</span>
      </div>
      <div
        class="vx-col sm:w-2/3 w-full"
        style="font-weight: bold;"
      >{{ assetData.assetId && assetData.assetId.slice(0, 32) }}</div>
      <div class="vx-col sm:w-1/3 w-full">
        <span></span>
      </div>
      <div
        class="vx-col sm:w-2/3 w-full"
        style="font-weight: bold;"
      >{{ assetData.assetId && assetData.assetId.slice(32, assetData.assetId.length) }}</div>
    </div>
    <div class="vx-row mb-6" v-if="assetData && assetData.email">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Người đang giữ</span>
      </div>
      <div
        class="vx-col sm:w-2/3 w-full"
        style="font-weight: bold;"
      >{{ assetData && assetData.email }}</div>
    </div>
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
      <div class="vx-col sm:w-2/3 w-full">
        {{ details.description.length > 100 && !showFullDesc ? textTrim(details.description) : details.description }}
        <span
          v-if="details.description.length > 100 && !showFullDesc"
          style="cursor: pointer; font-weight: bold;"
          @click="showFullDesc = true"
        >Xem thêm</span>
      </div>
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
          class="mr-3 mb-2 w-full"
          @click="goToLink(details.preview_link)"
          icon="pageview"
          v-if="details.preview_link && details.preview_link !== 'DEFAULT_REVIEW_LINK'"
        >Xem chi tiết sách</vs-button>
      </div>
    </div>
    <div class="vx-row" v-if="$auth.isAdmin()">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          color="success"
          class="mr-3 mb-2 w-full"
          @click="openHistory(assetData.assetId)"
          icon="history"
        >Xem lịch sử</vs-button>
      </div>
    </div>

    <vs-popup :title="historyId" :active.sync="historyPopup" :fullscreen="historyList.length">
      <vs-table
        noDataText="Không có dữ liệu"
        :data="historyList"
        v-if="historyList && historyList.length"
      >
        <template slot="thead">
          <vs-th>Thứ tự</vs-th>
          <vs-th>Mã giao dịch</vs-th>
          <vs-th>Loại</vs-th>
          <vs-th>Người trả</vs-th>
          <vs-th>Người nhận</vs-th>
          <vs-th>Thời gian</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">
            <vs-td :data="indextr">{{indextr + 1}}</vs-td>

            <vs-td :data="data[indextr].id">{{data[indextr].id}}</vs-td>

            <vs-td>
              <vs-chip
                :color="data[indextr].type === 'reject' ? 'danger' : 'primary'"
              >{{data[indextr].type === "reject" ? "Từ chối" : "Xác nhận"}}</vs-chip>
            </vs-td>

            <vs-td :data="data[indextr].returner">{{data[indextr].returner}}</vs-td>

            <vs-td :data="data[indextr].receiver">{{data[indextr].receiver}}</vs-td>

            <vs-td
              :data="data[indextr].transfer_date"
            >{{ parseInt(data[indextr].transfer_date) * 1000 | moment("dddd, Do MMMM YYYY, HH:MM") }}</vs-td>
          </vs-tr>
        </template>
      </vs-table>
      <p v-else>Thư viện đang giữ sách này, chưa được chuyển đi đâu cả.</p>
    </vs-popup>
  </vs-popup>
</template>

<script>
import textTrim from "@core/helper/string.helper";

export default {
  data() {
    return {
      showFullDesc: false,
      // This is for history
      historyId: 0,
      historyPopup: false,
      historyList: []
    };
  },
  watch: {
    isActive(val) {
      this.showFullDesc = false;
    }
  },
  computed: {
    details: {
      get() {
        return this.$store.state.detailsPopupData;
      }
    },
    assetData: {
      get() {
        return this.$store.state.detailsPopupAssetData || null;
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
    },
    textTrim(str) {
      return textTrim(str);
    },
    openHistory(assetId) {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/librarian/book_history`, {
          book_id: assetId
        })
        .then(response => {
          const data = response.data;

          data.sort((a, b) => a.transfer_date - b.transfer_date);

          this.historyList = data;
          this.historyPopup = true;
          this.historyId = assetId;
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
  }
};
</script>
