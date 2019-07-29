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
        <vx-card noShadow>
          <div class="vx-col w-full mt">
            <p>Sách muốn trả</p>
            <p style="font-size: 2rem;">{{ book && book.name || "Không có" }}</p>
          </div>
          <div class="vx-col w-full mt-5">
            <p>Mã sách</p>
            <p
              style="font-size: 2rem; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; text-align: left; direction: rtl;"
            >{{ (book && book.id) || 'Invalid' }}</p>
          </div>
        </vx-card>
      </div>
    </tab-content>

    <tab-content title="Chọn phương thức trả" class="mb-5" :before-change="loadingPrepare">
      <!-- tab 2 content -->
      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card class="w-full mb-4" style="font-size: 1.2rem; text-align: center;" noShadow>
            <vs-radio v-model="transferType" vs-value="auto">Chưa có người nhận</vs-radio>
          </vx-card>
          <vx-card class="w-full" style="font-size: 1.2rem; text-align: center;" noShadow>
            <vs-radio v-model="transferType" vs-value="manual">Đã có người nhận</vs-radio>
          </vx-card>
        </div>
      </div>
    </tab-content>

    <tab-content title="Hoàn thành" class="mb-5">
      <!-- tab 3 content -->
      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card title="Quét mã QR" v-if="transferType === 'manual' && !isLoading" noShadow>
            <QRScan v-if="!qrError" @printCode="handleQRCode" @onFail="handleQRFail" />
            <vs-input v-else class="w-full" label="Email người nhận" v-model="email" />
          </vx-card>
          <vx-card noShadow cardBorder v-else>{{ isLoading ? "Đang xử lí..." : resultText }}</vx-card>
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
          class="wizard-btn"
          style="background-color: rgba(var(--vs-primary), 1); border-color: rgba(var(--vs-primary), 1); color: white;"
          @click="manuallyReturn()"
          :disabled="!email"
        >Trả sách</button>
      </div>
    </template>
  </form-wizard>
</template>

<script>
import { FormWizard, TabContent } from "vue-form-wizard";
import QRScan from "@/views/components/QRScan.vue";
import "vue-form-wizard/dist/vue-form-wizard.min.css";

export default {
  data() {
    return {
      transferType: "auto",
      remainTime: 0,
      isLoading: false,
      resultText: "Đang tải",
      email: "",
      qrError: false
    };
  },
  methods: {
    async checkDone() {
      if (this.transferType === "auto") {
        this.$router.push("/books/returning");
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
      if (this.transferType === "auto") {
        this.isLoading = true;

        this.$http
          .post(`${this.$http.baseUrl}/matching/create_request`, {
            book_detail_id: this.book.bookDetailId,
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
      }

      return true;
    },
    manuallyReturn() {
      this.$vs.loading();

      this.$http
        .post(`${this.$http.baseUrl}/transfer/create`, {
          to: {
            email: this.email
          },
          asset_id: this.book.id
        })
        .then(response => {
          const tx = response.data;

          this.$store.dispatch("openTxPopup", tx);
        })
        .catch(() => {
          this.$vs.notify({
            title: "Thất bại",
            text: "Có lỗi xảy ra, vui lòng thử lại",
            color: "warning",
            position: "top-center"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    },
    handleQRCode(code) {
      this.email = code;
      this.manuallyReturn();
    },
    handleQRFail(val) {
      if (val) {
        this.qrError = true;
      }
    }
  },
  components: {
    FormWizard,
    TabContent,
    QRScan
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
