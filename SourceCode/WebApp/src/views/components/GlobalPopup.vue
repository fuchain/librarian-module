<template>
  <vs-popup :title="getTitle" :active.sync="isActive" v-if="tx">
    <vs-alert
      active="true"
      class="mb-4"
      v-if="!signedTx"
    >Bạn vừa {{ tx.operation === 'TRANSFER' ? 'tạo ra' : 'nhận được' }} một giao dịch, kí giao dịch để hoàn tất thực hiện</vs-alert>

    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Mã giao dịch</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full trim-text">{{ tx.id || signedTx && signedTx.id || "--" }}</div>
    </div>

    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Sách được chuyển</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ book && book.name || "--"}}</div>
    </div>

    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Mã sách</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <strong>{{ getAssetId || "--"}}</strong> (10 kí tự cuối)
      </div>
    </div>

    <div class="vx-row mb-6" v-if="book && book.thumbnail">
      <div class="vx-col sm:w-1/3 w-full"></div>
      <div class="vx-col sm:w-2/3 w-full">
        <img :src="book && book.thumbnail" style="max-width: 100px;" />
      </div>
    </div>

    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Người chuyển sách</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ returner || "--"}}</div>
    </div>

    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Người nhận sách</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">{{ receiver || "--"}}</div>
    </div>

    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Chữ kí xác nhận</span>
      </div>
      <div
        class="vx-col sm:w-2/3 w-full trim-text"
      >{{ signedTx && signedTx.inputs[0].fulfillment || "(chưa có chữ kí)" }}</div>
    </div>

    <div class="vx-row" v-if="!signedTx">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          class="mb-2 w-full"
          icon="fingerprint"
          @click="openConfirm"
        >{{ tx.operation === 'TRANSFER' ? 'Kí chuyển sách' : 'Kí nhận sách' }}</vs-button>
      </div>
    </div>

    <vs-popup title="Xác nhận" :active.sync="confirmPopup" class="confirm-tx-popup">
      Bạn có chắc chắn muốn kí vào giao dịch này? Chữ kí là không thể chối cãi.
      <vs-divider border-style="dashed" />
      <div class="float-right">
        <vs-button icon="check" @click="acceptAndSign">Kí xác nhận</vs-button>
      </div>
    </vs-popup>
  </vs-popup>
</template>

<script>
import signTx from "@core/crypto/signTx";

export default {
  data() {
    return {
      confirmPopup: false,
      signedTx: null,
      book: null,
      returner: "",
      receiver: ""
    };
  },
  watch: {
    isActive(val) {
      if (val) {
        this.loadData();
      } else {
        // Clear old value
        this.signedTx = null;
        this.book = null;
      }
    }
  },
  computed: {
    tx: {
      get() {
        return this.$store.state.transactionPopupTx;
      }
    },
    isActive: {
      get() {
        return this.$store.state.transactionPopupOpen;
      },
      set(value) {
        if (value === false) this.$store.dispatch("closeTxPopup");
      }
    },
    getTitle() {
      if (this.tx.operation === "TRANSFER") {
        return "Kí xác nhận chuyển sách";
      } else {
        return "Kí xác nhận đã nhận sách";
      }
    },
    getAssetId() {
      const tx = this.tx;

      if (tx.asset && tx.asset.id) {
        return this.trimStr(tx.asset.id);
      } else {
        return this.trimStr(tx.asset.data.confirm_for_tx.asset.id);
      }
    }
  },
  methods: {
    trimStr(str) {
      if (!str || str.length < 10) {
        return str;
      }

      return str.substring(str.length - 10, str.length);
    },
    openConfirm() {
      this.confirmPopup = true;
    },
    acceptAndSign() {
      const signedTx = signTx(this.tx);

      if (this.tx.operation === "TRANSFER") {
        this.$vs.loading();
        this.$http
          .post(`${this.$http.baseUrl}/transfer/sign`, {
            tx: signedTx
          })
          .then(() => {
            this.$vs.notify({
              title: "Thành công",
              text:
                "Kí xác nhận thành công, bạn sẽ nhận được phản hồi khi có kết quả",
              color: "primary",
              position: "top-center",
              fixed: true,
              icon: "check"
            });

            this.signedTx = signedTx;
            this.confirmPopup = false;

            // Close
            this.$store.dispatch("closeTxPopup", 5000);
            this.$router.push("/");
          })
          .catch(() => {
            this.$vs.notify({
              title: "Thất bại",
              text: "Có lỗi xảy ra",
              color: "warning",
              position: "top-center",
              fixed: true,
              icon: "error"
            });
          })
          .finally(() => {
            this.$vs.loading.close();
          });
      } else {
        this.$vs.loading();
        this.$http
          .post(`${this.$http.baseUrl}/transfer/done`, {
            tx: signedTx
          })
          .then(() => {
            this.$vs.notify({
              title: "Thành công",
              text: "Kí xác nhận thành công",
              color: "primary",
              position: "top-center",
              fixed: true,
              icon: "check"
            });

            this.signedTx = signedTx;
            this.confirmPopup = false;

            // Close
            this.$store.dispatch("closeTxPopup", 5000);
            this.$router.push("/books/keeping");
          })
          .catch(() => {
            this.$vs.notify({
              title: "Thất bại",
              text: "Có lỗi xảy ra",
              color: "warning",
              position: "top-center",
              fixed: true,
              icon: "error"
            });
          })
          .finally(() => {
            this.$vs.loading.close();
          });
      }
    },
    loadData() {
      if (!this.tx) return;

      if (this.tx.operation === "TRANSFER") {
        this.$http
          .post(`${this.$http.baseUrl}/fetch/asset_id`, {
            asset_id: this.tx.asset.id
          })
          .then(res => {
            this.book = res.data;
          });

        this.$http
          .post(`${this.$http.baseUrl}/fetch/public_key`, {
            public_key: this.tx.inputs[0].owners_before[0]
          })
          .then(res => {
            this.returner = res.data.email;
          });

        this.$http
          .post(`${this.$http.baseUrl}/fetch/public_key`, {
            public_key: this.tx.outputs[0].public_keys[0]
          })
          .then(res => {
            this.receiver = res.data.email;
          });
      } else {
        const assetId = this.tx.asset.data.confirm_for_tx.asset.id;
        const returnerPub = this.tx.asset.data.confirm_for_tx.inputs[0]
          .owners_before[0];
        const receiverPub = this.tx.asset.data.confirm_for_tx.outputs[0]
          .public_keys[0];

        this.$http
          .post(`${this.$http.baseUrl}/fetch/asset_id`, {
            asset_id: assetId
          })
          .then(res => {
            this.book = res.data;
          });

        this.$http
          .post(`${this.$http.baseUrl}/fetch/public_key`, {
            public_key: returnerPub
          })
          .then(res => {
            this.returner = res.data.email;
          });

        this.$http
          .post(`${this.$http.baseUrl}/fetch/public_key`, {
            public_key: receiverPub
          })
          .then(res => {
            this.receiver = res.data.email;
          });
      }
    }
  }
};
</script>

<style lang="scss">
.trim-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
