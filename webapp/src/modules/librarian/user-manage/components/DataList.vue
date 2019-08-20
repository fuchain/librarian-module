<template>
  <div id="data-list-list-view" class="data-list-container">
    <h2 class="mb-8 ml-4">Quản lí người dùng</h2>

    <div class="knowledge-base-jumbotron">
      <div class="knowledge-base-jumbotron-content lg:p-32 md:p-24 sm:p-16 py-8 rounded-lg mb-base">
        <h1 class="mb-1 text-white">Chữ kí số giúp việc chuyển sách an toàn tuyệt đối</h1>
        <p
          class="text-white"
        >Nền tảng cơ sở dữ liệu phi tập trung sẽ cho phép truy xuất, kiểm tra các giao dịch liên quan và quyền sở hữu của các bên tham gia giao dịch</p>
        <vs-input
          v-if="false"
          placeholder="Tìm tên sinh viên, mã số sinh viên hoặc địa chỉ mail"
          v-model="searchText"
          icon-pack="feather"
          icon="icon-search"
          size="large"
          class="w-full no-icon-border mt-6"
        />
      </div>
    </div>

    <h4 v-if="!loaded" style="text-align: center;">Đang tải dữ liệu, sắp hoàn thành</h4>

    <vs-table
      noDataText="Không có dữ liệu"
      ref="table"
      pagination
      :max-items="itemsPerPage"
      search
      :data="dataList"
      v-else
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
        <!-- <vs-dropdown vs-trigger-click class="cursor-pointer mb-4 mr-4">
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
        </vs-dropdown>-->
      </div>

      <template slot="thead">
        <vs-th>#</vs-th>
        <vs-th>Email</vs-th>
        <vs-th>Họ tên</vs-th>
        <vs-th>Số điện thoại</vs-th>
        <vs-th></vs-th>
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
              <p>{{ indextr + 1 }}</p>
            </vs-td>

            <vs-td>
              <p class="font-medium">{{ tr.email }}</p>
            </vs-td>

            <vs-td>
              <p class="font-medium">{{ tr.full_name || "Chưa cập nhật" }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.phone || "Chưa cập nhật" }}</p>
            </vs-td>

            <vs-td>
              <vs-button
                icon="pageview"
                @click="openKeepingBook(tr)"
                v-if="!tr.email.includes('librarian')"
              >Xem</vs-button>
            </vs-td>

            <vs-td>
              <p>
                <!-- <vs-button
                  @click="lockUser(tr)"
                  :icon="tr.inactive ? 'lock_open' : 'lock'"
                  type="relief"
                  :color="tr.inactive ? 'warning' : 'danger'"
                >{{ tr.inactive ? "Mở" : "Khóa" }}</vs-button>-->

                <vs-switch v-model="tr.inactive" @click="lockUser(tr)">
                  <span slot="on">Kích hoạt</span>
                  <span slot="off">Ngưng kích hoạt</span>
                </vs-switch>
              </p>
            </vs-td>
          </vs-tr>
        </tbody>
      </template>
    </vs-table>

    <vs-popup
      :fullscreen="keepingList && keepingList.length ? true : false"
      :title="'Ví sách của ' + keepingName"
      :active.sync="keepingPopup"
    >
      <div class="mb-4">
        <div class="vx-row mb-2">
          <div
            class="ml-4"
            style="background-color:#fff; color: #7367F0; border: 1px; border-color: #7367F0; padding: 10px; padding-left: 10px; border-radius: 5px;"
          >Địa chỉ ví: {{ keepingPublicKey || "Không có" }}</div>
        </div>
        <div class="ml-4">
          <vs-button>Đổi địa chỉ ví</vs-button>
        </div>
      </div>
      <vs-table
        noDataText="Không có dữ liệu"
        :data="keepingList"
        v-if="keepingList && keepingList.length"
      >
        <template slot="thead">
          <vs-th>Mã sách</vs-th>
          <vs-th>ID đầu sách</vs-th>
          <vs-th></vs-th>
          <vs-th>Tên sách</vs-th>
          <vs-th>Ngày nhận</vs-th>
          <vs-th>Bị từ chối</vs-th>
          <vs-th></vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">
            <vs-td :data="data[indextr].asset_id">{{data[indextr].asset_id}}</vs-td>
            <vs-td :data="data[indextr].book_detail.id">{{data[indextr].book_detail.id}}</vs-td>
            <vs-td :data="data[indextr].book_detail.thumbnail">
              <img
                :src="data[indextr].book_detail.thumbnail || '/images/book-thumbnail.jpg'"
                style="max-width: 65px;"
              />
            </vs-td>
            <vs-td :data="data[indextr].book_detail.name">{{data[indextr].book_detail.name}}</vs-td>
            <vs-td
              :data="data[indextr].transfer_date"
            >{{ parseInt(data[indextr].transfer_time) * 1000 | moment("dddd, Do MMMM YYYY, HH:MM") }} ({{ parseInt(data[indextr].transfer_time) * 1000 | moment("from") }})</vs-td>
            <vs-td>{{data[indextr].reject_count || "--"}}</vs-td>
            <vs-td>
              <vs-button icon="pageview" @click="openHistory(data[indextr].asset_id)">Lịch sử</vs-button>
            </vs-td>
          </vs-tr>
        </template>
      </vs-table>
      <!-- <p v-else>Sinh viên này đang không giữ cuốn sách nào cả</p> -->

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
  </div>
</template>

<script>
export default {
  data() {
    return {
      itemsPerPage: 10,
      isMounted: false,
      keepingPopup: false,
      keepingName: "",
      keepingPublicKey: "",
      keepingList: [],
      searchText: "",
      // This is for history
      historyId: 0,
      historyPopup: false,
      historyList: []
    };
  },
  props: {
    dataList: {
      default: [].concat([]),
      type: Array
    },
    loaded: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    currentPage() {
      if (this.isMounted && this.$refs.table && this.$refs.table) {
        return this.$refs.table.currentx;
      }
      return 0;
    }
  },
  methods: {
    openKeepingBook(item) {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/librarian/users/books`, {
          user: {
            public_key: item.public_key
          }
        })
        .then(response => {
          const data = response.data;

          this.keepingList = data;
          this.keepingPopup = true;
          this.keepingName = item.email;
          this.keepingPublicKey = item.public_key;
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    openHistory(assetId) {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/librarian/book_history`, {
          book_id: assetId
        })
        .then(response => {
          const data = response.data;

          this.historyList = data;
          this.historyPopup = true;
          this.historyId = assetId;
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    openConfirm(user) {
      const inactive = user.inactive || false;

      this.$vs.dialog({
        type: "confirm",
        color: "danger",
        title: `Xác nhận`,
        text: `Bạn có chắc muốn ${
          inactive ? "mở khóa" : "khóa"
        } tài khoản ${user.email || null}`,
        accept: this.lockUser(user),
        acceptText: "Chắc chắn",
        cancelText: "Hủy bỏ"
      });
    },
    lockUser(user) {
      const inactive = user.inactive || false;

      this.$vs.loading();
      this.$http
        .post(`${this.$http.baseUrl}/librarian/lock_account`, {
          email: user.email || null
        })
        .then(response => {
          const data = response.data;

          if (data.status === "inactive") {
            this.$vs.notify({
              title: "Thành công",
              text: `Khóa tài khoản ${user.email} thành công`,
              color: "primary",
              position: "top-center"
            });
          } else {
            this.$vs.notify({
              title: "Thành công",
              text: `Mở khóa tài khoản  ${user.email} thành công`,
              color: "primary",
              position: "top-center"
            });
          }
        })
        .catch(() => {
          this.$emit("doReload");
          this.$vs.notify({
            title: "Lỗi",
            text: "Lỗi bất ngờ xảy ra, vui lòng thử lại sau",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
  },
  mounted() {
    this.isMounted = true;
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
          &.img-container {
            // width: 1rem;
            // background: #fff;

            span {
              display: flex;
              justify-content: center;
            }

            .product-img {
              height: 110px;
            }
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

.knowledge-base-jumbotron-content {
  background-image: url("../../../../assets/images/pages/knowledge-base-cover.jpg");
  background-size: cover;
}
</style>
