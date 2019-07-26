<template>
  <vx-card :title="'Hàng đợi đang có ' + queues.length + ' yêu cầu'">
    <p class="mb-8">
      Đã có
      <strong>{{ matched || 0 }}</strong> yêu cầu đã được hệ thống ghép, đang chờ người đọc chuyển sách.
    </p>

    <vs-table
      noDataText="Không có dữ liệu"
      ref="table"
      pagination
      :max-items="itemsPerPage"
      :data="queues"
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
      </div>

      <template slot="thead">
        <vs-th></vs-th>
        <vs-th>Tên đầu sách</vs-th>
        <vs-th>Email</vs-th>
        <vs-th>Loại</vs-th>
        <vs-th>Mượn lúc</vs-th>
        <vs-th></vs-th>
      </template>

      <template slot-scope="{data}">
        <tbody>
          <vs-tr :data="tr" :key="indextr" v-for="(tr, indextr) in data">
            <vs-td>
              <p>
                <img
                  :src="tr.bookInfo.thumbnail || '/images/book-thumbnail.jpg'"
                  style="max-width: 100px;"
                />
              </p>
            </vs-td>

            <vs-td>
              <p>{{ tr.bookInfo.name || "--" }}</p>
            </vs-td>

            <vs-td>
              <p>{{ tr.email || "--" }}</p>
            </vs-td>

            <vs-td>
              <p>
                <vs-chip>{{ tr.bookId ? "Trả" : "Mượn" }}</vs-chip>
              </p>
            </vs-td>

            <vs-td>
              <p>{{ tr.time * 1000 | moment("dddd, Do MMMM YYYY, HH:MM") }}</p>
            </vs-td>

            <vs-td>{{ tr.matched ? "Đã ghép" : "" }}</vs-td>
          </vs-tr>
        </tbody>
      </template>
    </vs-table>
  </vx-card>
</template>

<script>
export default {
  data() {
    return {
      queues: []
    };
  },
  methods: {
    viewDetail(item) {
      this.detailPopup = true;
      this.detailPopupItem = item;
    }
  },
  computed: {
    matched() {
      if (!this.queues || !this.queues.length) {
        return 0;
      }

      const matched = this.queues.filter(e => e.matched);
      return matched.length;
    }
  },
  mounted() {
    this.$vs.loading();

    this.$http
      .get(`${this.$http.baseUrl}/librarian/matchings`)
      .then(response => {
        const data = response.data;

        this.queues = data;
      })
      .finally(() => {
        this.$vs.loading.close();
      });
  }
};
</script>
