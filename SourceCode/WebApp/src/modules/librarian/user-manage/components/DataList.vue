<template>
  <div id="data-list-list-view" class="data-list-container">
    <h2 class="mb-8 ml-4">Quản lí người dùng</h2>

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
        <vs-th>Email</vs-th>
        <vs-th>Họ tên</vs-th>
        <vs-th>Số điện thoại</vs-th>
        <vs-th>Trực tuyến</vs-th>
        <vs-th>Vô hiệu hóa</vs-th>
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
              <p>{{ tr.id }}</p>
            </vs-td>

            <vs-td>
              <p class="font-medium">{{ tr.email }}</p>
            </vs-td>

            <vs-td>
              <p class="font-medium">{{ tr.fullName || "Chưa cập nhật" }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.phone || "Chưa cập nhật" }}</p>
            </vs-td>

            <vs-td>
              <p>
                <vs-chip
                  :color="onlineUserState[indextr] ? 'primary' : 'danger'"
                >{{ onlineUserState[indextr] ? "online" : "offline" }}</vs-chip>
              </p>
            </vs-td>

            <vs-td>
              <p>
                <vs-switch value="tr.disabled" />
              </p>
            </vs-td>

            <vs-td>
              <vs-button
                icon="pageview"
                @click="openKeepingBook(tr)"
                v-if="!tr.email.includes('fe.edu.vn')"
              >Xem sách đang giữ</vs-button>
            </vs-td>
          </vs-tr>
        </tbody>
      </template>
    </vs-table>

    <vs-popup
      :fullscreen="keepingList && keepingList.length ? true : false"
      :title="'Sách đang giữ của ' + keepingName"
      :active.sync="keepingPopup"
    >
      <vs-table :data="keepingList" v-if="keepingList && keepingList.length">
        <template slot="thead">
          <vs-th>ID sách</vs-th>
          <vs-th>ID đầu sách</vs-th>
          <vs-th></vs-th>
          <vs-th>Tên sách</vs-th>
          <vs-th>Tình trạng</vs-th>
          <vs-th>Trạng thái</vs-th>
          <vs-th>Cập nhật</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">
            <vs-td :data="data[indextr].id">{{data[indextr].id}}</vs-td>
            <vs-td :data="data[indextr].bookDetail.id">{{data[indextr].bookDetail.id}}</vs-td>
            <vs-td :data="data[indextr].bookDetail.name">
              <img
                :src="data[indextr].bookDetail.thumbnail || '/images/book-thumbnail.jpg'"
                style="max-width: 65px;"
              />
            </vs-td>
            <vs-td :data="data[indextr].bookDetail.name">{{data[indextr].bookDetail.name}}</vs-td>
            <vs-td :data="data[indextr].status">{{data[indextr].status}}</vs-td>
            <vs-td :data="data[indextr].transferStatus">{{data[indextr].transferStatus}}</vs-td>

            <vs-td
              :data="data[indextr].updateDate"
            >{{ parseInt(data[indextr].updateDate) * 1000 | moment("dddd, Do MMMM YYYY, h:mm:ss a")}}</vs-td>
          </vs-tr>
        </template>
      </vs-table>
      <p v-else>Sinh viên này đang không giữ cuốn sách nào cả</p>
    </vs-popup>
  </div>
</template>

<script>
import { socket } from "@core/socket";
export default {
  data() {
    return {
      itemsPerPage: 10,
      isMounted: false,
      keepingPopup: false,
      keepingName: "",
      keepingList: [],
      onlineUsers: [],
      onlineUserState: []
    };
  },
  props: {
    dataList: {
      default: [].concat([]),
      type: Array
    }
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
    openKeepingBook(item) {
      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/librarian/users/${item.id}/books`)
        .then(response => {
          const data = response.data;

          this.keepingList = data;
          this.keepingPopup = true;
          this.keepingName = item.email;
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    checkUserOnline(email) {
      return this.onlineUsers.find(e => e === email) ? true : false;
    },
    trackOnline() {
      const trackArr = this.dataList.map(e => {
        return this.checkUserOnline(e.email);
      });

      this.onlineUserState = trackArr;
    }
  },
  mounted() {
    this.isMounted = true;

    this.$http
      .get(`${this.$http.nodeUrl}/notifications/online`)
      .then(response => {
        const data = response.data;

        this.onlineUsers = data;
        this.trackOnline();
      });
    if (socket) {
      socket.on(
        "online",
        function({ users }) {
          this.onlineUsers = [].concat(users) || [].concat([]);
          this.trackOnline();
        }.bind(this)
      );
    } else {
      console.log("Socket not init!");
    }
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
</style>
