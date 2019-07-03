<template>
  <vx-card :title="'Hàng đợi đang có ' + queue.length + ' đầu sách'">
    <p class="mb-4">
      Đã có
      <strong>{{ matching || 0 }}</strong> yêu cầu đã được hệ thống ghép, đang chờ người đọc chuyển sách.
    </p>
    <div class="items-grid-view vx-row match-height" v-if="queue.length" appear>
      <div class="vx-col lg:w-1/4 md:w-1/3 sm:w-1/2 w-full" v-for="item in queue" :key="item.id">
        <book-card :item="item.bookDetail">
          <template slot="action-buttons">
            <div class="flex flex-wrap">
              <div
                class="item-view-secondary-action-btn bg-primary p-3 flex flex-grow items-center justify-center text-white cursor-pointer"
                @click="viewDetail(item)"
              >
                <feather-icon icon="InfoIcon" svgClasses="h-4 w-4" />

                <span class="text-sm font-semibold ml-2">XEM CHI TIẾT ({{ item.totalRequest }})</span>
              </div>
            </div>
          </template>
        </book-card>
      </div>
    </div>

    <vs-popup title="Chi tiết các yêu cầu" :active.sync="detailPopup">
      <vs-table :data="detailPopupItem.requests" v-if="detailPopup">
        <template slot="thead">
          <vs-th>ID</vs-th>
          <vs-th>Kiểu</vs-th>
          <vs-th>Người yêu cầu</vs-th>
          <vs-th>Đã ghép với</vs-th>
          <vs-th>Cập nhật</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">
            <vs-td :data="data[indextr].id">#{{data[indextr].id}}</vs-td>

            <vs-td :data="data[indextr].type">
              <vs-chip color="primary">{{data[indextr].type === 1 ? "Mượn" : "Trả"}}</vs-chip>
            </vs-td>

            <vs-td :data="data[indextr].user.email">{{data[indextr].user.email}}</vs-td>

            <vs-td
              :data="data[indextr].pairedUser && data[indextr].pairedUser.email"
            >{{ data[indextr].pairedUser && data[indextr].pairedUser.email || "--" }}</vs-td>

            <vs-td
              :data="data[indextr].updateDate"
            >{{ parseInt(data[indextr].updateDate) * 1000 | moment("from") }}</vs-td>
          </vs-tr>
        </template>
      </vs-table>
    </vs-popup>
  </vx-card>
</template>

<script>
import BookCard from "@/views/components/BookCard.vue";
import { parseSingleItem } from "@http/parse";

export default {
  components: {
    BookCard
  },
  data() {
    return {
      queue: [],
      detailPopup: false,
      detailPopupItem: null,
      matching: 0
    };
  },
  methods: {
    viewDetail(item) {
      this.detailPopup = true;
      this.detailPopupItem = item;
    }
  },
  mounted() {
    this.$vs.loading();

    this.$http
      .get(`${this.$http.baseUrl}/librarian/queue/overview`)
      .then(response => {
        const data = response.data.map(e => {
          return {
            bookDetail: parseSingleItem(e.bookDetail),
            requests: e.borrowRequest.concat(e.returnRequest),
            totalRequest: e.borrowRequest.length + e.returnRequest.length
          };
        });

        this.queue = data;
      })
      .finally(() => {
        this.$vs.loading.close();
      });

    this.$http.get(`${this.$http.baseUrl}/requests/overview`).then(response => {
      const data = response.data;

      this.matching = data.totalMatchingRequest;
    });
  }
};
</script>
