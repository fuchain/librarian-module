<template>
  <div id="data-list-list-view" class="data-list-container">
    <h2 class="mb-8 ml-4">Quản lí {{ totalBookDetails || "..." }} đầu sách</h2>

    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-2/3">
        <vs-input
          icon-pack="feather"
          icon="icon-search"
          placeholder="Tìm kiếm bằng tên đầu sách, mã môn hoặc ID đầu sách"
          v-model="searchText"
          class="ml-4 w-full"
          size="large"
          v-on:keyup.enter="doSearch"
        />
      </div>
    </div>

    <vs-table noDataText="Không có dữ liệu" ref="table" :data="dataList">
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
      </div>

      <template slot="thead">
        <vs-th>ID</vs-th>
        <vs-th></vs-th>
        <vs-th>Phân loại</vs-th>
        <vs-th>Tên đầu sách</vs-th>
        <vs-th>Mã môn</vs-th>
        <vs-th>Số lượng</vs-th>
        <vs-th></vs-th>
        <vs-th style="width: 20% !important;"></vs-th>
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

            <vs-td class="img-container">
              <img
                :src="tr.thumbnail || '/images/book-thumbnail.jpg'"
                class="product-img"
                @click="$store.dispatch('openDetailsPopup', tr.book_detail)"
              />
            </vs-td>

            <vs-td>
              <p>{{ tr.categories || "--" }}</p>
            </vs-td>

            <vs-td>
              <p class="font-medium">{{ tr.name }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.subject_codes && tr.subject_codes.length && tr.subject_codes[0] || "N/A"}}</p>
            </vs-td>

            <vs-td>
              <p class="font-medium">{{ tr.amount }} cuốn</p>
            </vs-td>

            <vs-td>
              <vs-button
                icon="pageview"
                type="border"
                @click="openBookDataList(tr)"
                v-if="tr.amount > 0"
              >Xem</vs-button>
            </vs-td>

            <vs-td>
              <vs-button
                :color="loadingList[indextr] ? 'warning' : 'primary'"
                icon="file_copy"
                type="relief"
                @click="exportReport(tr)"
                v-if="tr.amount > 0"
              >{{ loadingList[indextr] ? "Đang tải..." : "Xuất báo cáo" }}</vs-button>
            </vs-td>
          </vs-tr>
        </tbody>
      </template>
    </vs-table>
  </div>
</template>

<script>
import { json2excel } from "js2excel";

export default {
  data() {
    return {
      isMounted: false,
      addNewDataSidebar: false,
      searchText: "",
      totalBookDetails: 0
    };
  },
  props: {
    dataList: {
      default: [].concat([]),
      type: Array
    }
  },
  computed: {
    loading() {
      return this.$store.state.loading;
    },
    currentPage() {
      if (this.isMounted) {
        return this.$refs.table.currentx;
      }
      return 0;
    },
    loadingList() {
      return this.dataList.map(e => {
        if (this.loading.includes(e.id)) {
          return true;
        }

        return false;
      });
    }
  },
  methods: {
    exportReport(item) {
      const currentLoading = [].concat(this.loading);
      this.$store.commit("UPDATE_LOADING", currentLoading.concat(item.id));

      this.$http
        .get(
          `${this.$http.baseUrl}/librarian/book_details/${item.id}/books/details`
        )
        .then(response => {
          const data = response.data;

          json2excel({
            data,
            name: `book-report-${item.id}`
          });

          const index = this.loading.indexOf(item.id);
          if (index > -1) {
            const doneLoading = [].concat(this.loading);
            doneLoading.splice(index, 1);

            this.$store.commit("UPDATE_LOADING", doneLoading);
          }
        });
    },
    openBookDataList(item) {
      this.$router.push(`/librarian/book-details-manage/${item.id}`);
    },
    doSearch() {
      if (!isNaN(this.searchText)) {
        this.$vs.loading();

        this.$http
          .get(`${this.$http.baseUrl}/bookdetails/${this.searchText}`)
          .then(() => {
            this.$router.push(
              `/librarian/book-details-manage/${this.searchText}`
            );
          })
          .catch(() => {
            this.$vs.notify({
              title: "Lỗi",
              text: "Không tìm thấy",
              color: "warning",
              position: "top-center"
            });
          })
          .finally(() => {
            this.$vs.loading.close();
          });
      } else {
        this.$emit("doSearch", this.searchText);
      }
    }
  },
  mounted() {
    this.isMounted = true;

    this.$Progress.start();
    this.$http
      .get(`${this.$http.baseUrl}/librarian/overviews`)
      .then(response => {
        const { book_detail_total } = response.data;

        this.totalBookDetails = book_detail_total;
      })
      .finally(() => {
        this.$Progress.finish();
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
