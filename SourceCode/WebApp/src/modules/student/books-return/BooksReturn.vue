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
            <vs-input
              type="email"
              size="large"
              label="Sách muốn trả"
              :value="book.name || 'Null'"
              disabled
              class="w-full"
            />
          </div>
          <div class="vx-col w-full mt-5">
            <vs-input
              type="email"
              size="large"
              label="Mã sách"
              :value="'FUHCMLIB' + book.id || '0'"
              disabled
              class="w-full"
            />
          </div>
        </vx-card>
      </div>
    </tab-content>

    <tab-content title="Chọn phương thức trả" class="mb-5" :before-change="loading">
      <!-- tab 2 content -->
      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card class="w-full mb-4" style="font-size: 1.2rem; text-align: center;">
            <vs-radio v-model="transferType" vs-value="auto">Tìm tự động</vs-radio>
          </vx-card>
          <vx-card class="w-full" style="font-size: 1.2rem; text-align: center;">
            <vs-radio v-model="transferType" vs-value="manual">Chọn thủ công</vs-radio>
          </vx-card>
        </div>
      </div>
    </tab-content>

    <tab-content title="Hoàn thành" class="mb-5">
      <!-- tab 3 content -->
      <div class="vx-row">
        <div class="vx-col w-full">
          <vx-card v-if="transferType === 'manual'">
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
          @click="props.nextTab() | runAction(props)"
          class="wizard-btn"
          style="background-color: rgba(var(--vs-primary), 1); border-color: rgba(var(--vs-primary), 1); color: white;"
        >Tiếp theo</button>

        <button
          v-else
          @click="checkDone"
          class="wizard-btn"
          style="background-color: rgba(var(--vs-primary), 1); border-color: rgba(var(--vs-primary), 1); color: white;"
        >{{props.isLastStep ? 'Xong' : 'Tiếp theo'}}</button>
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
      resultText: "Đang tải"
    };
  },
  computed: {
    randomPIN() {
      return Math.floor(100000 + Math.random() * 900000);
    }
  },
  methods: {
    async checkDone() {
      if (this.transferType === "auto") {
        this.$router.push("/books/returning");
      } else {
        await this.fakeLoad();

        this.$vs.notify({
          title: "Lỗi",
          text: "Người nhận chưa xác nhận mã PIN trên ứng dụng",
          color: "warning",
          position: "top-center"
        });
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
      await this.fakeLoad(this.transferType === "manual" ? 3000 : 0);

      this.transferType === "manual" && this.startCount();

      return true;
    },
    startCount() {
      this.remainTime = 300;

      countInterval = setInterval(
        function() {
          this.remainTime = this.remainTime - 1;

          if (this.remainTime <= 0) {
            this.$vs.notify({
              title: "Lỗi",
              text: "Hết hạn xác nhận mã PIN, vui lòng thao tác lại từ đầu",
              color: "warning",
              position: "top-center"
            });

            clearInterval(countInterval);
          }
        }.bind(this),
        1000
      );
    },
    runAction(props) {
      const step = props.activeTabIndex;

      if (step === 1 && this.transferType === "auto") {
        this.isLoading = true;

        this.$http
          .post(`${this.$http.baseUrl}/requests`, {
            type: 2,
            book_id: this.book.id
          })
          .then(() => {
            this.isLoading = false;
            this.resultText =
              "Thông tin trả sách đã được gửi, bạn sẽ nhận được thông báo khi hệ thống tìm được người nhận sách.";
          })
          .catch(err => {
            // Catch
            console.log(err);

            this.isLoading = false;
            this.resultText =
              "Thông tin trả sách không hợp lệ. Lí do: bạn đã có yêu cầu trả sách này rồi.";
          });
      }
    }
  },
  beforeDestroy() {
    clearInterval(countInterval);
  },
  components: {
    FormWizard,
    TabContent
  },
  beforeMount() {
    if (!this.book) {
      this.$router.push({ path: "/books" });
    }
  },
  props: {
    book: Object
  }
};
</script>
