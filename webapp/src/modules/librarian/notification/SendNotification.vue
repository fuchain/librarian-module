<template>
  <vx-card class="md:w-1/2 w-full mb-base" title="Gửi thông báo cho người mượn sách" noShadow>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Email người nhận</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vue-simple-suggest
          pattern="\w+"
          v-model="model"
          :list="getList"
          :max-suggestions="5"
          :min-length="3"
          :debounce="200"
          :filter-by-query="false"
          :prevent-submit="true"
          :controls="{
      selectionUp: [38, 33],
      selectionDown: [40, 34],
      select: [13, 36],
      hideList: [27, 35]
    }"
          :nullable-select="true"
          ref="suggestComponent"
          placeholder="Tìm kiếm..."
          value-attribute="id"
          display-attribute="text"
          @select="onSuggestSelect"
        >
          <div class="g">
            <input type="text" />
          </div>

          <template slot="misc-item-above" slot-scope="{ suggestions, query }">
            <div class="misc-item">
              <span>Bạn đang tìm kiếm '{{ query }}'.</span>
            </div>

            <template v-if="suggestions.length > 0">
              <div class="misc-item">
                <span>{{ suggestions.length }} gợi ý được tìm thấy...</span>
              </div>
              <hr />
            </template>

            <div class="misc-item" v-else-if="!loading">
              <span>Không có kết quả</span>
            </div>
          </template>

          <div slot="suggestion-item" slot-scope="scope" :title="scope.suggestion.description">
            <div class="text">
              <span v-html="boldenSuggestion(scope)"></span>
            </div>
          </div>

          <div class="misc-item" slot="misc-item-below" slot-scope="{ suggestions }" v-if="loading">
            <span>Đang tải...</span>
          </div>
        </vue-simple-suggest>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Nội dung</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-textarea v-model="message" />
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button class="mr-3 mb-2" icon="check" @click="submit">Gửi thông báo đẩy và email</vs-button>
      </div>
    </div>
  </vx-card>
</template>

<script>
import VueSimpleSuggest from "vue-simple-suggest";
import "vue-simple-suggest/dist/styles.css";

export default {
  components: {
    VueSimpleSuggest
  },
  data() {
    return {
      message: "",
      type: "",
      model: null,
      loading: false
    };
  },
  methods: {
    submit() {
      if (!this.model || !this.message) {
        this.$vs.notify({
          title: "Lỗi",
          text: "Bạn phải nhập email và nội dung thông báo",
          color: "warning",
          position: "top-center"
        });

        return;
      }

      this.$vs.loading();
      this.$http
        .post(`${this.$http.nodeUrl}/notifications/push`, {
          email: this.model,
          message: this.message,
          type: null
        })
        .then(response => {
          const status = response.status;
          if (status === 200) {
            this.$vs.notify({
              title: "Đã gửi",
              text:
                "Đã gửi tin nhắn cho người dùng, hiện tại người dùng này không online",
              color: "success",
              position: "top-center"
            });
          } else if (status === 201) {
            this.$vs.notify({
              title: "Đã gửi",
              text:
                "Đã gửi tin nhắn cho người dùng, hiện tại người dùng này đang online",
              color: "success",
              position: "top-center"
            });
          }
        })
        .catch(() => {
          this.$vs.notify({
            title: "Có lỗi",
            text: "Lỗi bất ngờ xảy ra, vui lòng thử lại",
            color: "danger",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },

    getList(inputValue) {
      this.loading = true;
      return new Promise((resolve, reject) => {
        this.$http
          .post(`${this.$http.baseUrl}/librarian/search_user`, {
            text: inputValue
          })
          .then(response => {
            const data = response.data;

            const result = data.map(e => {
              return {
                text: e.email,
                description: e.fullname
              };
            });

            resolve(result);
          })
          .catch(e => {
            reject(e);
          })
          .finally(() => {
            this.loading = false;
          });
      });
    },

    onSuggestSelect(suggest) {
      this.selected = suggest;
    },
    boldenSuggestion(scope) {
      if (!scope) return scope;
      const { suggestion, query } = scope;
      let result = this.$refs.suggestComponent.displayProperty(suggestion);
      if (!query) return result;
      const texts = query.split(/[\s-_/\\|.]/gm).filter(t => !!t) || [""];
      return result.replace(
        new RegExp("(.*?)(" + texts.join("|") + ")(.*?)", "gi"),
        "$1<b>$2</b>$3"
      );
    }
  }
};
</script>
