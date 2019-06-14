<template>
  <form-wizard
    color="rgba(var(--vs-primary), 1)"
    title="Gửi trả sách"
    subtitle="Gửi sách cho sinh viên khác"
  >
    <template slot="step" slot-scope="props">
      <wizard-step
        :tab="props.tab"
        :transition="props.transition"
        :key="props.tab.title"
        :index="props.index"
      ></wizard-step>
    </template>

    <tab-content title="Xác nhận thông tin" class="mb-5" :before-change="loading">
      <!-- tab 1 content -->
      <div class="vx-row">
        <vx-card>
          <div class="vx-col w-full mt">
            <p>Sách muốn trả</p>
            <p style="font-size: 2rem;">{{ book && book.name || "Không có" }}</p>
          </div>
          <div class="vx-col w-full mt-5">
            <p>Mã sách</p>
            <p style="font-size: 2rem;">{{ 'FUHCMBOOK' + (book && book.id) || '0' }}</p>
          </div>
        </vx-card>
      </div>
    </tab-content>

    <tab-content title="Chọn phương thức trả" class="mb-5" :before-change="loadingPrepare">
      <!-- tab 2 content -->
      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card class="w-full mb-4" style="font-size: 1.2rem; text-align: center;">
            <vs-radio v-model="transferType" vs-value="auto">Chưa có người nhận</vs-radio>
          </vx-card>
          <vx-card class="w-full" style="font-size: 1.2rem; text-align: center;">
            <vs-radio v-model="transferType" vs-value="manual">Đã có người nhận</vs-radio>
          </vx-card>
        </div>
      </div>
    </tab-content>

    <tab-content title="Hoàn thành" class="mb-5">
      <!-- tab 3 content -->
      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card v-if="transferType === 'manual' && !isLoading">
            <div style="font-size: 1.5rem; text-align: center;">Mã số PIN xác nhận</div>
            <div style="font-size: 3rem; text-align: center;">{{ randomPIN }}</div>
            <div style="text-align: center;">
              Chỉ tồn tại trong
              <strong>{{ remainTime }} giây</strong>
            </div>
          </vx-card>
          <vx-card v-else>{{ isLoading ? "Đang xử lí..." : resultText }}</vx-card>
        </div>
      </div>
    </tab-content>
    <template slot="footer" slot-scope="props">
      <div class="wizard-footer-right">
        <button
          v-if="!props.isLastStep"
          @click="props.nextTab()"
          class="wizard-btn"
          style="background-color: rgba(var(--vs-primary), 1); border-color: rgba(var(--vs-primary), 1); color: white;"
        >Tiếp theo</button>

        <button
          v-if="props.isLastStep && transferType === 'auto'"
          @click="checkDone"
          class="wizard-btn"
          style="background-color: rgba(var(--vs-primary), 1); border-color: rgba(var(--vs-primary), 1); color: white;"
        >Xong</button>

        <button
          v-if="props.isLastStep && transferType !== 'auto'"
          @click="doCancel"
          class="wizard-btn"
          style="background-color: rgba(var(--vs-primary), 1); border-color: rgba(var(--vs-primary), 1); color: white;"
        >Huỷ bỏ trả sách</button>
      </div>
    </template>
  </form-wizard>
</template>

<script>
import { FormWizard, TabContent } from "vue-form-wizard";
import "vue-form-wizard/dist/vue-form-wizard.min.css";
import { setInterval, clearInterval } from "timers";

let countInterval;

export default {
  data() {
    return {
      transferType: "auto",
      remainTime: 0,
      isLoading: false,
      resultText: "Đang tải",
      randomPIN: "000000",
      matchingId: 0,
      requestId: 0
    };
  },
  methods: {
    async validateConfirm() {
      this.$http
        .get(`${this.$http.baseUrl}/matchings/${this.matchingId}/confirm`)
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Người nhận đã xác nhận mã PIN",
            color: "primary",
            position: "top-center"
          });

          this.$store.dispatch("getNumOfBooks");

          setTimeout(
            function() {
              this.$router.push("/books/keeping");
            }.bind(this),
            500
          );
        })
        .catch(err => {
          // Catch
          console.log(err);
        })
        .finally(() => {});
    },
    async checkDone() {
      if (this.transferType === "auto") {
        this.$router.push("/books/returning");
      } else {
        this.validateConfirm();
      }
    },
    async fakeLoad(customTime) {
      return new Promise((resolve, reject) => {
        this.$vs.loading();
        setTimeout(
          function() {
            this.$vs.loading.close();
            resolve();
          }.bind(this),
          customTime || 500
        );
      });
    },
    async loading() {
      await this.fakeLoad(500);

      return true;
    },
    async loadingPrepare() {
      this.isLoading = true;

      if (this.transferType === "auto") {
        this.$http
          .post(`${this.$http.baseUrl}/requests`, {
            type: 2,
            book_id: this.book.id
          })
          .then(() => {
            this.resultText =
              "Thông tin trả sách đã được gửi, bạn sẽ nhận được thông báo khi hệ thống tìm được người nhận sách.";
            this.$store.dispatch("getNumOfBooks");
          })
          .catch(err => {
            // Catch
            console.log(err);

            this.resultText =
              "Thông tin trả sách không hợp lệ. Lí do: bạn đã có yêu cầu trả sách này rồi.";
          })
          .finally(() => {
            this.isLoading = false;
          });
      } else {
        this.$http
          .post(`${this.$http.baseUrl}/requests/manually`, {
            book_id: this.book.id
          })
          .then(response => {
            this.randomPIN = response.data.pin.toString();
            this.matchingId = response.data.matching_id;
            this.requestId = response.data.request_id;
            this.startCount();
            this.$store.dispatch("getNumOfBooks");
          })
          .catch(err => {
            // Catch
            console.log(err);
            this.resultText =
              "Thông tin trả sách không hợp lệ. Lí do: bạn đã có yêu cầu trả sách này rồi.";
          })
          .finally(() => {
            this.isLoading = false;
          });
      }

      return true;
    },
    startCount() {
      this.remainTime = 300;

      countInterval = setInterval(
        function() {
          this.remainTime = this.remainTime - 1;

          this.validateConfirm();

          if (this.remainTime <= 0) {
            this.$vs.notify({
              title: "Lỗi",
              text: "Hết hạn xác nhận mã PIN, vui lòng thao tác lại từ đầu",
              color: "warning",
              position: "top-center"
            });

            clearInterval(countInterval);
            this.$router.push("/books/keeping");
          }
        }.bind(this),
        1000
      );

      this.validateConfirm();
    },
    doCancel() {
      this.$vs.loading();

      this.$http
        .put(`${this.$http.baseUrl}/requests/cancel`, {
          request_id: this.requestId
        })
        .then(() => {
          this.$vs.notify({
            title: "Thành công",
            text: "Hủy bỏ việc trả sách thành công",
            color: "primary",
            position: "top-center"
          });

          this.requestId = 0;

          this.$store.dispatch("getNumOfBooks");

          this.$router.push("/books/keeping");
        })
        .catch(e => {
          // Catch error
          console.log(e);
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    }
  },
  beforeDestroy() {
    clearInterval(countInterval);
    if (this.requestId) this.doCancel();
  },
  components: {
    FormWizard,
    TabContent
  },
  beforeMount() {
    if (!this.book) {
      this.$router.push({ path: "/books/keeping" });
    }
  },
  props: {
    book: Object
  }
};
</script>
