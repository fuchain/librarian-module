<template>
  <vx-card title="Yêu cầu mượn sách">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Mã môn hoặc Tên sách</span>
      </div>
      <div class="vx-col sm:w-1/3 w-full">
        <vs-input class="w-full" v-model="searchText"/>
      </div>
      <div class="vx-col sm:w-1/3 w-full">
        <vs-button type="border" class="w-full" @click="doSearch">Tìm sách</vs-button>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Tên sách</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-select v-model="bookCode" width="100%" :disabled="!listBooks.length || !searchText">
          <vs-select-item
            :key="item.id"
            :value="item.name"
            :text="item.name"
            v-for="item in listBooks"
          />
        </vs-select>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          class="mr-3 mb-2"
          @click="submit"
          :disabled="!searchText.trim() || !bookCode"
          icon="done"
        >Đăng kí</vs-button>
      </div>
    </div>
  </vx-card>
</template>

<script>
export default {
  data() {
    return {
      searchText: "",
      bookCode: "",
      listBooks: []
    };
  },
  methods: {
    doSearch() {
      if (!this.searchText.trim()) return;
      this.$vs.loading();

      this.$http
        .get(`${this.$http.baseUrl}/bookdetails/search?name=${this.searchText}`)
        .then(response => {
          const data = response.data;

          this.$vs.loading.close();

          this.listBooks = [].concat(data);

          if (!data.length) {
            this.$vs.notify({
              title: "Lỗi",
              text: "Không tìm thấy quyển sách nào, vui lòng đổi từ khóa",
              color: "warning",
              position: "top-center"
            });
          }
        });
    },
    submit() {
      if (!this.searchText.trim() || !this.bookCode) return;
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/requests/create`, {
          type: 1,
          book_name: this.bookCode
        })
        .then(() => {
          this.$vs.loading.close();
          this.$vs.notify({
            title: "Thành công",
            text: "Yêu cầu của bạn đã được hệ thống tiếp nhận",
            color: "primary",
            position: "top-center"
          });
        })
        .catch(err => {
          // Catch
          console.log(err);

          this.$vs.loading.close();
          this.$vs.notify({
            title: "Thất bại",
            text: "Bạn đã có yêu cầu mượn sách này rồi",
            color: "warning",
            position: "top-center"
          });
        });
    }
  }
};
</script>
