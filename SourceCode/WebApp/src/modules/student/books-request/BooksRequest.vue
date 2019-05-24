<template>
  <vx-card title="Yêu cầu mượn sách">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Mã môn hoặc Tên môn</span>
      </div>
      <div class="vx-col sm:w-1/3 w-full">
        <vs-input class="w-full" v-model="subjectCode"/>
      </div>
      <div class="vx-col sm:w-1/3 w-full">
        <vs-button type="border" class="w-full" @click="searchSubject">Tìm sách</vs-button>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Tên sách</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-select v-model="bookCode" width="100%" :disabled="!listBooks.length || !subjectCode">
          <vs-select-item
            :key="index"
            :value="item.id"
            :text="item.name"
            v-for="item, index in listBooks"
          />
        </vs-select>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          class="mr-3 mb-2"
          @click="submit"
          :disabled="!subjectCode.trim() || !bookCode.trim()"
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
      subjectCode: "",
      bookCode: "",
      listBooks: []
    };
  },
  methods: {
    searchSubject() {
      if (!this.subjectCode.trim()) return;
      this.$vs.loading();
      setTimeout(
        function() {
          this.$vs.loading.close();

          this.listBooks = [].concat([
            {
              id: "FUHCM000000002",
              name: "Japanese Elementary 3"
            },
            {
              id: "FUHCM000000003",
              name: "Start Your Business"
            }
          ]);
        }.bind(this),
        2000
      );
    },
    submit() {
      if (!this.subjectCode.trim() || !this.bookCode) return;
      this.$vs.loading();
      setTimeout(
        function() {
          this.$vs.loading.close();

          this.$vs.notify({
            title: "Thành công",
            text: "Yêu cầu của bạn đã được hệ thống tiếp nhận",
            color: "primary",
            position: "top-center"
          });
          this.$router.push("/books/requesting");
        }.bind(this),
        3000
      );
    }
  }
};
</script>
