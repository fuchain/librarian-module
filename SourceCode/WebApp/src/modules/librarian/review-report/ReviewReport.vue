<template>
  <div>
    <h2 class="mb-8">Báo cáo đánh giá của người dùng</h2>
    <vx-card
      class="flex justify-center mb-8 w-full lg:w-1/2 mb-base bg-success-gradient"
      style="color: white;"
    >
      <vs-list>
        <vs-list-header icon="supervisor_account" :title="'Điểm trung bình: ' + avgPoint"></vs-list-header>
        <vs-list-item icon="check" :title="detailAvg[0]" subtitle="Tốc độ tải trang và độ mượt"></vs-list-item>
        <vs-list-item
          icon="check"
          :title="detailAvg[1]"
          subtitle="Menu và các chỉ mục của ứng dụng"
        ></vs-list-item>
        <vs-list-item
          icon="check"
          :title="detailAvg[2]"
          subtitle="Danh sách hiển thị rõ ràng hợp mắt"
        ></vs-list-item>
        <vs-list-item icon="check" :title="detailAvg[3]" subtitle="Các nút bấm được bố trí phù hợp"></vs-list-item>
        <vs-list-item icon="check" :title="detailAvg[4]" subtitle="Trải nghiệm khi mượn / trả sách"></vs-list-item>
      </vs-list>
    </vx-card>
    <vs-divider border-style="dashed" color="dark">báo cáo chi tiết</vs-divider>
    <div id="data-list-list-view" class="data-list-container">
      <vs-table
        noDataText="Không có dữ liệu"
        ref="table"
        pagination
        :max-items="itemsPerPage"
        search
        :data="reviews"
      >
        <div slot="header" class="flex flex-wrap-reverse items-center flex-grow justify-between">
          <!-- ITEMS PER PAGE -->
          <vs-dropdown vs-trigger-click class="cursor-pointer mb-4 mr-4">
            <div
              class="p-4 border border-solid border-grey-light rounded-full d-theme-dark-bg cursor-pointer flex items-center justify-between font-medium"
            >
              <span
                class="mr-2"
              >{{ currentPage * itemsPerPage - (itemsPerPage - 1) }} - {{ reviews.length - currentPage * itemsPerPage > 0 ? currentPage * itemsPerPage : reviews.length }} of {{ reviews.length }}</span>
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
          <vs-th>Email</vs-th>
          <vs-th>Tổng quan</vs-th>
          <vs-th>Chi tiết</vs-th>
          <vs-th>Ghi chú</vs-th>
          <vs-th>Thiết bị</vs-th>
          <vs-th>Cập nhật</vs-th>
        </template>

        <template slot-scope="{data}">
          <tbody>
            <vs-tr :data="tr" :key="indextr" v-for="(tr, indextr) in data">
              <vs-td>
                <p class="font-medium">{{ tr.email }}</p>
              </vs-td>

              <vs-td>
                <p>{{ tr.overall }}</p>
              </vs-td>

              <vs-td>
                <p v-for="(item, index) in tr.details" :key="index">{{ item }}</p>
              </vs-td>

              <vs-td>
                <p>{{ tr.note }}</p>
              </vs-td>

              <vs-td>
                <p>{{ tr.userAgent }}</p>
              </vs-td>

              <vs-td>
                <p>{{ tr.updatedAt | moment("from") }}</p>
              </vs-td>
            </vs-tr>
          </tbody>
        </template>
      </vs-table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      reviews: [],
      itemsPerPage: 10,
      isMounted: false
    };
  },
  computed: {
    currentPage() {
      if (this.isMounted) {
        return this.$refs.table.currentx;
      }
      return 0;
    },
    avgPoint() {
      const avg = this.reviews.reduce((accumulator, currentValue) => {
        return accumulator + currentValue.overall;
      }, 0);

      return (avg / this.reviews.length).toFixed(2);
    },
    detailAvg() {
      let result = [];
      for (let i = 0; i < 5; i++) {
        const avg = this.reviews.reduce((accumulator, currentValue) => {
          return accumulator + currentValue.details[i];
        }, 0);

        const detail = (avg / this.reviews.length).toFixed(2);
        result.push(detail);
      }

      return result;
    }
  },
  mounted() {
    this.$vs.loading();

    this.$http
      .get(`${this.$http.nodeUrl}/reviews`)
      .then(response => {
        const data = response.data;

        const reviews = data.reverse().map(e => {
          const total = e.review.ratings.reduce((accumulator, currentValue) => {
            return accumulator + currentValue;
          });
          const overall = total / e.review.ratings.length;

          return {
            email: e.email,
            overall,
            details: e.review.ratings,
            note: e.review.note,
            updatedAt: e.updatedAt,
            userAgent: e.review.userAgent
          };
        });

        this.reviews = [].concat(reviews);
      })
      .finally(() => {
        this.isMounted = true;
        this.$vs.loading.close();
      });
  }
};
</script>

<style lang="scss" scoped>
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

h4 {
  color: white;
}

.vs-list--header {
  color: white;
}
</style>
