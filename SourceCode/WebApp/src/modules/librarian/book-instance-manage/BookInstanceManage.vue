<template>
  <div id="data-list-list-view" class="data-list-container">
    <h2 class="mb-8 ml-4" v-if="bookDetail.id">Quản lí đầu sách #{{ $route.params.id || "--" }}</h2>

    <div class="flex items-center justify-center">
      <book-card :item="bookDetail" v-if="bookDetail.id" style="max-width: 250px;">
        <template slot="action-buttons">
          <div class="flex flex-wrap">
            <div
              class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
              @click="doBookTransfer"
            >
              <feather-icon icon="CheckIcon" svgClasses="h-4 w-4" />

              <span class="text-sm font-semibold ml-2">CHUYỂN SÁCH (CÒN {{dataList.length}})</span>
            </div>
          </div>
        </template>
      </book-card>
    </div>

    <vs-table ref="table" pagination :max-items="itemsPerPage" search :data="dataList">
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
            >{{ currentPage * itemsPerPage - (itemsPerPage - 1) }} - {{ dataList.length - currentPage * itemsPerPage > 0 ? currentPage * itemsPerPage : dataList.length }} of {{ dataList.length }}</span>
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
        <vs-th>ID</vs-th>
        <vs-th>Tình trạng</vs-th>
        <vs-th>Trạng thái</vs-th>
        <vs-th>Cập nhật</vs-th>
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
              <p class="font-medium">{{ tr.id }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.status || "transfer failed" }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.transferStatus }}</p>
            </vs-td>

            <vs-td>
              <p>{{ parseInt(tr.updateDate) * 1000 | moment("from") }}</p>
            </vs-td>

            <vs-td>
              <vs-button icon="pageview" @click="openHistory(tr)">Xem lịch sử chuyển sách</vs-button>
            </vs-td>
          </vs-tr>
        </tbody>
      </template>
    </vs-table>

    <vs-popup :title="'Lịch sử chuyển của sách #' + historyId" :active.sync="historyPopup">
      <div class="mb-4" style="color: red;" v-if="fraudEmail">
        <p>Phát hiện thay đổi bất thường, người đang giữ sách trong cơ sở dữ liệu ({{ fraudEmail }}) không khớp với dữ liệu trong Blockchain.</p>
        <vs-button
          color="red"
          class="mt-2 w-full"
          type="relief"
          @click="fraudRecover(historyId)"
        >Khôi phục lại dữ liệu</vs-button>
        <vs-divider border-style="dashed" color="red">phát hiện gian lận bởi hệ thống blockchain</vs-divider>
      </div>
      <vs-table :data="historyList" v-if="historyList && historyList.length">
        <template slot="thead">
          <vs-th>Thứ tự</vs-th>
          <vs-th>Người đang giữ sách</vs-th>
          <vs-th>Trạng thái</vs-th>
          <vs-th>Bị từ chối</vs-th>
          <vs-th>Thời gian</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">
            <vs-td :data="indextr">{{indextr + 1}}</vs-td>

            <vs-td :data="data[indextr].current_keeper">{{data[indextr].current_keeper}}</vs-td>

            <vs-td :data="data[indextr].status">{{data[indextr].status || "rejected"}}</vs-td>

            <vs-td :data="data[indextr].reject_count">
              <div @click="openRejectPopup(data[indextr])" style="cursor: pointer;">
                <vs-chip
                  :color="data[indextr].reject_count === '0' ? 'primary' : 'danger'"
                >{{data[indextr].reject_count || "0"}} lần</vs-chip>
              </div>
            </vs-td>

            <vs-td
              :data="data[indextr].transaction_timestamp"
            >{{ parseInt(data[indextr].transaction_timestamp) * 1000 | moment("dddd, Do MMMM YYYY, HH:MM") }}</vs-td>
          </vs-tr>
        </template>
      </vs-table>
      <p v-else>Thư viện đang giữ sách này, chưa được chuyển đi đâu cả.</p>

      <vs-popup
        :title="'Thông tin từ chối nhận sách của ' + rejectPopup.item.current_keeper"
        :active.sync="rejectPopup.isActive"
      >
        <div class="vx-row">
          <div class="vx-col w-full">
            <vs-textarea
              label="Lí do từ chối"
              :value="rejectPopup.item.reject_reason"
              disabled="true"
            />
          </div>
        </div>
        <div class="vx-row mb-6">
          <div class="vx-col w-full">
            <vs-input
              class="w-full"
              label="Hash ảnh"
              :value="rejectPopup.item.img_hash"
              disabled="true"
            />
          </div>
        </div>

        <div class="vx-row mb-6">
          <div class="vx-col w-full">
            <img :src="rejectPopup.item.img_link" style="max-width: 566.5px;" />
          </div>
        </div>
      </vs-popup>
    </vs-popup>

    <vs-popup title="Người nhận xác nhận" :active.sync="popupActive">
      <div style="font-size: 1.5rem; text-align: center;">Mã số PIN xác nhận</div>
      <div style="font-size: 3rem; text-align: center;">{{ randomPIN }}</div>
      <div style="text-align: center; margin-bottom: 1rem;">
        Chỉ tồn tại trong
        <strong>{{ remainTime }} giây</strong>
      </div>
      <div style="text-align: center; margin-bottom: 1rem;">
        <vs-button @click="doCancel">Hủy bỏ</vs-button>
      </div>
    </vs-popup>
  </div>
</template>

<script>
import BookCard from "@/views/components/BookCard.vue";

let countInterval;

export default {
  components: {
    BookCard
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
      // PIN Flow
      popupActive: false,
      randomPIN: 0,
      remainTime: 0,
      matchingId: 0,
      requestId: 0,
      rejectPopup: {
        isActive: false,
        item: {}
      }
    };
  },
  computed: {
    currentPage() {
      if (this.isMounted) {
        return this.$refs.table.currentx;
      }
      return 0;
    }
  },
  methods: {
    openHistory(item) {
      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/librarian/books/${item.id}`)
        .then(response => {
          const data = response.data;

          this.historyList = data.bcTransactionList;
          this.metadata = data.metadata.data;
          this.historyPopup = true;
          this.historyId = item.id;

          if (
            this.historyList &&
            this.historyList.length &&
            this.historyList[this.historyList.length - 1].current_keeper !==
              this.metadata.current_keeper
          ) {
            this.fraudEmail = this.metadata.current_keeper;
          } else {
            this.fraudEmail = "";
          }
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    startCount() {
      this.remainTime = 300;
      clearInterval(countInterval);

      countInterval = setInterval(
        function() {
          this.remainTime = this.remainTime - 1;
          this.validateConfirm();

          if (this.remainTime <= 0) {
            this.$vs.notify({
              title: "Lỗi",
              text: "Hết hạn xác nhận mã PIN, vui lòng thao tác lại từ đầu",
              color: "warning",
              position: "top-center"
            });

            clearInterval(countInterval);
          }
        }.bind(this),
        1000
      );
    },
    async validateConfirm() {
      this.$http
        .get(`${this.$http.baseUrl}/matchings/${this.matchingId}/confirm`)
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Người nhận đã xác nhận mã PIN",
            color: "primary",
            position: "top-center"
          });

          this.popupActive = false;
          this.requestId = 0;
          clearInterval(countInterval);
        })
        .catch(err => {
          // Catch
          console.log(err);

          const status = err.response.status;
          if (status !== 400) {
            this.$vs.notify({
              title: "Thất bại",
              text: "Người nhận đã từ chối nhận xách",
              color: "warning",
              position: "top-center"
            });

            this.popupActive = false;
            this.requestId = 0;
            clearInterval(countInterval);
          }
        })
        .finally(() => {});
    },
    doBookTransfer() {
      const id = this.$route.params.id;

      this.$vs.loading();
      this.$http
        .post(`${this.$http.baseUrl}/librarian/give_book?book_detail_id=${id}`)
        .then(response => {
          const matchingId = response.data.matching_id;
          this.matchingId = matchingId;
          const requestId = response.data.request_id;
          this.requestId = requestId;

          this.randomPIN = response.data.pin;
          this.startCount();
          this.popupActive = true;
        })
        .catch(err => {
          console.log(err);

          this.$vs.notify({
            title: "Lỗi",
            text: "Xảy ra lỗi, chưa thể thực hiện chuyển sách",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    doCancel() {
      this.$vs.loading();
      clearInterval(countInterval);

      this.$http
        .put(`${this.$http.baseUrl}/requests/cancel`, {
          request_id: this.requestId
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Hủy bỏ việc chuyển sách thành công",
            color: "primary",
            position: "top-center"
          });
          this.popupActive = false;
        })
        .catch(e => {
          this.$vs.notify({
            title: "Lỗi",
            text: "Chưa thể hủy bỏ việc trả sách",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    openRejectPopup(item) {
      if (!item.reject_reason) return;

      this.rejectPopup.isActive = true;
      this.rejectPopup.item = item;
    },
    fraudRecover(id) {
      this.$vs.loading();

      this.$http
        .put(`${this.$http.baseUrl}/librarian/books/${id}/sync_current_keeper`)
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Cập nhật dữ liệu thành công",
            color: "primary",
            position: "top-center"
          });

          this.historyPopup = false;

          setTimeout(function() {
            window.location.reload(true);
          }, 500);
        })
        .catch(() => {
          this.$vs.notify({
            title: "Lỗi",
            text: "Chưa thể cập nhật",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
  },
  beforeDestroy() {
    clearInterval(countInterval);
    if (this.requestId && this.popupActive) this.doCancel();
  },
  created() {
    if (!this.$route.params.id) {
      this.$router.push("/librarian/book-details-manage");
    }
  },
  mounted() {
    this.$vs.loading();

    this.$http
      .get(
        `${this.$http.baseUrl}/librarian/book_details/${this.$route.params.id}/books?size=5000`
      )
      .then(response => {
        const data = response.data;

        // Sort
        data.sort((a, b) => {
          return parseInt(b.updateDate) - parseInt(a.updateDate);
        });

        this.dataList = [].concat(data);
      })
      .finally(() => {
        this.isMounted = true;
        this.$vs.loading.close();
      });

    this.$http
      .get(`${this.$http.baseUrl}/bookdetails/${this.$route.params.id}`)
      .then(response => {
        const data = response.data;

        this.bookDetail = {
          id: data.id,
          name: data.name,
          description: data.description,
          image: data.thumbnail || "/images/book-thumbnail.jpg",
          code:
            (data.parseedSubjectCode &&
              data.parseedSubjectCode.length &&
              data.parseedSubjectCode[0]) ||
            "N/A",
          status: "in use"
        };
      })
      .catch(() => {
        this.$router.push("/librarian/book-details-manage");
      });
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
