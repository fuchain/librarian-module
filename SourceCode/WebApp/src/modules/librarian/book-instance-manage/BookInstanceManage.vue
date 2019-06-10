<template>
  <div id="data-list-list-view" class="data-list-container">
    <h2 class="mb-8 ml-4">
      Quản lí sách của đầu sách #{{ $route.params.id }}
      <vs-button
        size="small"
        class="ml-4"
        @click="$router.push('/librarian/book-details-manage')"
      >Quay lại danh sách đầu sách</vs-button>
    </h2>

    <vs-table ref="table" pagination :max-items="itemsPerPage" search :data="dataList">
      <div slot="header" class="flex flex-wrap-reverse items-center flex-grow justify-between">
        <div class="flex flex-wrap-reverse items-center">
          <!-- ACTION - DROPDOWN -->
          <vs-dropdown vs-trigger-click v-if="false">
            <div
              class="p-4 shadow-drop rounded-lg d-theme-dark-bg cursor-pointer flex items-center justify-center text-lg font-medium w-32"
            >
              <span class="mr-2">Làm</span>
              <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4"/>
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
            <feather-icon icon="ChevronDownIcon" svgClasses="h-4 w-4"/>
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
              <p>{{ tr.status }}</p>
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
      <vs-table :data="historyList" v-if="historyList && historyList.length">
        <template slot="thead">
          <vs-th>Thứ tự</vs-th>
          <vs-th>Người đang giữ sách</vs-th>
          <vs-th>Trạng thái</vs-th>
          <vs-th>Thời gian</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">
            <vs-td :data="indextr">{{indextr + 1}}</vs-td>

            <vs-td :data="data[indextr].current_keeper">{{data[indextr].current_keeper}}</vs-td>

            <vs-td :data="data[indextr].status">{{data[indextr].status}}</vs-td>

            <vs-td
              :data="data[indextr].transaction_timestamp"
            >{{ parseInt(data[indextr].transaction_timestamp) * 1000 | moment("from") }}</vs-td>
          </vs-tr>
        </template>
      </vs-table>
      <p v-else>Thư viện đang giữ sách này, chưa được chuyển đi đâu cả.</p>
    </vs-popup>
  </div>
</template>

<script>
export default {
  data() {
    return {
      itemsPerPage: 10,
      isMounted: false,
      dataList: [],
      historyPopup: false,
      historyList: [],
      historyId: 0
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
          const data = response.data.bcTransactions;

          this.historyList = data;
          this.historyPopup = true;
          this.historyId = item.id;
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
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
        `${this.$http.baseUrl}/librarian/book_details/${
          this.$route.params.id
        }/books`
      )
      .then(response => {
        const data = response.data;

        this.dataList = [].concat(data);
      })
      .finally(() => {
        this.isMounted = true;
        this.$vs.loading.close();
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
