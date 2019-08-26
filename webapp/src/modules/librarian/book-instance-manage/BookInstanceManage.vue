<template>
  <div id="data-list-list-view" class="data-list-container">
    <h2 class="mb-8 ml-4" v-if="bookDetail.id">
      <feather-icon
        class="back-btn rounded-lg mr-4"
        style="color: #7367F0;"
        icon="ArrowLeftIcon"
        @click="$router.go(-1)"
      ></feather-icon>
      Quản lí đầu sách #{{ $route.params.id || "--" }} ({{ dataList.length }} cuốn)
    </h2>

    <div class="flex items-center justify-center">
      <book-card :item="bookDetail" v-if="bookDetail.id" style="max-width: 250px;">
        <template slot="action-buttons">
          <div class="flex flex-wrap">
            <div
              class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              @click="verifyReturn(bookDetail.id)"
              v-if="totalRemain || totalRemain > 0"
            >
              <feather-icon icon="CheckIcon" svgClasses="h-4 w-4" />

              <span class="text-sm font-semibold ml-2">CHUYỂN SÁCH (CÒN {{ totalRemain }})</span>
            </div>
            <div
              class="item-view-secondary-action-btn bg-white p-3 flex flex-grow items-center justify-center text-primary"
              v-else
            >
              <span class="text-sm font-semibold ml-2">ĐANG TẢI THÔNG TIN...</span>
            </div>
          </div>
        </template>
      </book-card>
    </div>

    <vs-table
      noDataText="Không có dữ liệu"
      ref="table"
      pagination
      :max-items="itemsPerPage"
      search
      :data="dataList"
    >
      <div slot="header" class="flex flex-wrap-reverse items-center flex-grow justify-between">
        <div class="flex flex-wrap-reverse items-center">
          <!-- ACTION - DROPDOWN -->
          <vs-dropdown vs-trigger-click v-if="false">
            <div
              class="p-4 shadow-drop rounded-lg d-theme-dark-bg cursor-pointer flex items-center justify-center text-lg font-medium w-32"
            >
              <span class="mr-2">Làm</span>
              <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4" />
            </div>

            <vs-dropdown-menu>
              <vs-dropdown-item>
                <span>Xóa</span>
              </vs-dropdown-item>
            </vs-dropdown-menu>
          </vs-dropdown>
        </div>

        <!-- ITEMS PER PAGE -->
        <vs-dropdown vs-trigger-click class="cursor-pointer mb-4 mr-4">
          <div
            class="p-4 border border-solid border-grey-light rounded-full d-theme-dark-bg cursor-pointer flex items-center justify-between font-medium"
          >
            <span
              class="mr-2"
            >{{ currentPage * itemsPerPage - (itemsPerPage - 1) }} - {{ dataList.length - currentPage * itemsPerPage > 0 ? currentPage * itemsPerPage : dataList.length }} trong {{ dataList.length }}</span>
            <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4" />
          </div>
          <vs-dropdown-menu>
            <vs-dropdown-item @click="itemsPerPage=10">
              <span>10</span>
            </vs-dropdown-item>
            <vs-dropdown-item @click="itemsPerPage=15">
              <span>15</span>
            </vs-dropdown-item>
            <vs-dropdown-item @click="itemsPerPage=20">
              <span>20</span>
            </vs-dropdown-item>
          </vs-dropdown-menu>
        </vs-dropdown>
      </div>

      <template slot="thead">
        <vs-th>Mã sách</vs-th>
        <vs-th>Số lần chuyển</vs-th>
        <vs-th>Người đang giữ</vs-th>
        <vs-th></vs-th>
      </template>

      <template slot-scope="{data}">
        <tbody>
          <vs-tr
            :data="tr"
            :key="indextr"
            v-for="(tr, indextr) in data"
            @click="openBookDataList(tr)"
          >
            <vs-td>
              <p>{{ "..." + tr.asset_id.slice(-10) || "--" }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.transaction_count === "Fetching" ? "--" : tr.transaction_count || "Chưa có" }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.current_keeper === "Fetching" ? "--" : tr.current_keeper.includes('librarian') ? "Thư viện" : tr.current_keeper || "--" }}</p>
            </vs-td>

            <vs-td>
              <vs-button
                icon="pageview"
                @click="openHistory(tr.asset_id)"
                v-if="tr.transaction_count && tr.transaction_count !== 'Fetching'"
              >Lịch sử</vs-button>
            </vs-td>
          </vs-tr>
        </tbody>
      </template>
    </vs-table>

    <vs-popup :title="historyId" :active.sync="historyPopup" :fullscreen="historyList.length">
      <div
        class="mb-4"
        style="background: #7367F0; color: white; padding: 1rem 2rem 0.5rem 2rem; border-radius: 10px;"
      >
        <p class="mb-4">
          Người đang giữ sách này:
          <strong>{{ currentKeeperInHistory }}</strong>
        </p>

        <p class="mb-4">
          Số lần bị từ chối:
          <strong>{{ currentRejectCount || 0 }}</strong>
        </p>

        <p class="mb-4" v-if="currentRejectCount && currentRejectCount > 2">
          <vs-button
            @click="confirmRemoveBook(historyId, currentKeeperInHistory)"
            color="danger"
          >Hủy bỏ quyển sách này</vs-button>
        </p>
      </div>

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

    <vs-prompt
      title="Quét QRCode ví sách của người nhận"
      accept-text="Chuyển"
      cancel-text="Hủy bỏ"
      @cancel="email=''"
      @accept="manuallyReturn()"
      :active.sync="emailPrompt"
    >
      <div>
        <vs-input
          v-if="qrError"
          placeholder="Nhập email người nhận sách"
          class="w-full"
          v-model="email"
        />
        <QRScan v-if="!qrError" @printCode="handleQRCode" @onFail="handleQRFail" />
      </div>
    </vs-prompt>
  </div>
</template>

<script>
import BookCard from "@/views/components/BookCard.vue";
import QRScan from "@/views/components/QRScan.vue";

export default {
  components: {
    BookCard,
    QRScan
  },
  data() {
    return {
      itemsPerPage: 10,
      isMounted: false,
      dataList: [],
      historyPopup: false,
      historyList: [],
      metadata: {},
      fraudEmail: "",
      historyId: 0,
      bookDetail: {},
      //
      emailPrompt: false,
      email: "",
      returnBookId: "",
      qrError: false,
      removeId: null
    };
  },
  computed: {
    currentPage() {
      if (this.isMounted) {
        return this.$refs.table.currentx;
      }
      return 0;
    },
    totalRemain() {
      const data = this.dataList.filter(
        e => e.current_keeper === "librarian@fptu.tech"
      );
      return data.length;
    },
    currentKeeperInHistory() {
      if (this.historyList && this.historyList.length) {
        return this.historyList[this.historyList.length - 1].receiver || null;
      }

      return null;
    },
    currentRejectCount() {
      if (this.historyList && this.historyList.length) {
        const historyCopy = [].concat(this.historyList);
        const reversed = historyCopy.reverse();

        let rejectCount = 0;
        for (const item of reversed) {
          if (
            item.type !== "reject" &&
            item.receiver !== "librarian@fptu.tech"
          ) {
            break;
          }

          if (item.receiver !== "librarian@fptu.tech") {
            rejectCount++;
          }
        }

        return rejectCount;
      }

      return 0;
    }
  },
  methods: {
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
    },
    verifyReturn(id) {
      this.returnBookId = id;
      this.emailPrompt = true;
    },
    manuallyReturn() {
      if (this.email.trim() === "librarian@fptu.tech") {
        this.$vs.notify({
          title: "Thất bại",
          text: "Không thể chuyển sách cho chính bạn",
          color: "danger",
          position: "top-center"
        });

        return;
      }

      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/librarian/give_book`, {
          to: {
            email: this.email
          },
          book_detail_id: this.returnBookId
        })
        .then(response => {
          const tx = response.data;

          this.$store.dispatch("openTxPopup", tx);
        })
        .catch(() => {
          this.$vs.notify({
            title: "Thất bại",
            text: "Có lỗi xảy ra, vui lòng thử lại",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    confirmRemoveBook(assetId, currentKeeper) {
      this.historyPopup = false;
      this.removeId = assetId;

      if (currentKeeper !== "librarian@fptu.tech") {
        this.$vs.dialog({
          color: "warning",
          title: "Không thể thực hiện",
          text:
            "Vui lòng yêu cầu người đọc chuyển sách về thư viện trước khi hủy sách",
          acceptText: "Tôi biết rồi"
        });

        return;
      }

      this.$vs.dialog({
        type: "confirm",
        color: "danger",
        title: `Xác nhận`,
        text: "Bạn chắc có muốn hủy bỏ quyển sách này?",
        accept: this.removeBook,
        acceptText: "Chắc chắn",
        cancelText: "Hủy bỏ"
      });
    },
    removeBook() {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/librarian/remove_book`, {
          asset_id: this.removeId
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Hủy sách thành công",
            color: "primary",
            position: "top-center"
          });

          setTimeout(() => {
            window.location.reload(1);
          }, 1000);
        })
        .catch(err => {
          this.$vs.notify({
            title: "Lỗi",
            text: "Xảy ra lỗi bất ngờ, vui lòng thử lại",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    handleQRCode(code) {
      this.email = code;
      this.manuallyReturn();
    },
    handleQRFail(val) {
      if (val) {
        this.qrError = true;
      }
    }
  },
  mounted() {
    this.$Progress.start();

    this.$http
      .get(
        `${this.$http.baseUrl}/librarian/book_details/${this.$route.params.id}/books`
      )
      .then(response => {
        const data = response.data;

        this.dataList = [].concat(data);

        this.$http
          .get(
            `${this.$http.baseUrl}/librarian/book_details/${this.$route.params.id}/books/details`
          )
          .then(response => {
            const data = response.data;

            data.sort((a, b) => b.transaction_count - a.transaction_count);

            this.dataList = [].concat(data);
          })
          .finally(() => {
            this.$Progress.finish();
          });
      })
      .finally(() => {
        this.isMounted = true;
      });

    this.$http
      .get(`${this.$http.baseUrl}/book/book_detail/${this.$route.params.id}`)
      .then(response => {
        const data = response.data;

        this.bookDetail = {
          id: data.id,
          name: data.name,
          description: data.description,
          image: data.thumbnail || "/images/book-thumbnail.jpg",
          code:
            (data.subject_codes &&
              data.subject_codes.length &&
              data.subject_codes[0]) ||
            "N/A",
          type: "info"
        };
      })
      .catch(() => {
        this.$router.push("/librarian/book-details-manage");
      });
  },
  beforeDestroy() {
    this.$Progress.finish();
  }
};
</script>

<style lang="scss">
#data-list-list-view {
  .vs-con-table {
    .vs-table--header {
      display: flex;
      flex-wrap: wrap-reverse;
      margin-left: 1.5rem;
      margin-right: 1.5rem;
      > span {
        display: flex;
        flex-grow: 1;
      }

      .vs-table--search {
        padding-top: 0;

        .vs-table--search-input {
          padding: 0.9rem 2.5rem;
          font-size: 1rem;

          & + i {
            left: 1rem;
          }

          &:focus + i {
            left: 1rem;
          }
        }
      }
    }

    .vs-table {
      border-collapse: separate;
      border-spacing: 0 1.3rem;
      padding: 0 1rem;

      tr {
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.05);
        td {
          padding: 20px;
          &:first-child {
            border-top-left-radius: 0.5rem;
            border-bottom-left-radius: 0.5rem;
          }
          &:last-child {
            border-top-right-radius: 0.5rem;
            border-bottom-right-radius: 0.5rem;
          }
        }
        td.td-check {
          padding: 20px !important;
        }
      }
    }

    .vs-table--thead {
      th {
        padding-top: 0;
        padding-bottom: 0;

        .vs-table-text {
          text-transform: uppercase;
          font-weight: 600;
        }
      }
      th.td-check {
        padding: 0 15px !important;
      }
      tr {
        background: none;
        box-shadow: none;
      }
    }

    .vs-table--pagination {
      justify-content: center;
    }
  }
}
</style>
