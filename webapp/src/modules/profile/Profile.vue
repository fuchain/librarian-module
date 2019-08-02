<template>
  <vx-card title="Hồ sơ" noShadow>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Họ tên</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="fullname" />
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Email</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <div
          style="background-color:#7367F0; color: #fff; padding: 10px; padding-left: 10px; border-radius: 5px;"
        >{{email}}</div>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Số điện thoại</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="phone" />
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Chế độ "Dark Mode"</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-switch v-model="darkmode" />
      </div>
    </div>
    <div class="vx-row mb-6" v-if="!$auth.isAdmin()">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Chuyển sách cho tôi</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <canvas id="canvas"></canvas>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          class="mr-3 mb-2"
          @click="submit"
          icon="check"
          v-if="!$auth.isAdmin()"
        >Cập nhật thông tin</vs-button>
      </div>
    </div>

    <!-- <vs-divider border-style="dashed" color="dark">Kiểm thử tính năng</vs-divider>

    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          color="success"
          class="mr-3 mb-2"
          @click="test()"
          icon="build"
        >Kiểm thử giao dịch trả</vs-button>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button
          color="warning"
          class="mr-3 mb-2"
          @click="test(true)"
          icon="build"
        >Kiểm thử giao dịch nhận</vs-button>
      </div>
    </div>-->
  </vx-card>
</template>

<script>
import QRCode from "qrcode";
import transferTx from "./transfer_tx.json";
import createTx from "./create_tx.json";

export default {
  computed: {
    email: {
      get() {
        return this.$store.getters.email;
      },
      set(value) {
        this.$store.commit("UPDATE_PROFILE_EMAIL", value);
      }
    },
    fullname: {
      get() {
        return this.$store.getters.fullname;
      },
      set(value) {
        this.$store.commit("UPDATE_PROFILE_FULLNAME", value);
      }
    },
    phone: {
      get() {
        return this.$store.getters.phone;
      },
      set(value) {
        this.$store.commit("UPDATE_PROFILE_PHONE", value);
      }
    }
  },
  watch: {
    darkmode(val) {
      this.$store.dispatch("updateTheme", val === true ? "dark" : "light");
      this.$localStorage.setItem("darkmode", val ? "dark" : "light");
    },
    email(val) {
      const canvas = document.getElementById("canvas");

      QRCode.toCanvas(canvas, `${val}`, { width: 200 }, function(error) {
        if (error) console.error(error);
      });
    }
  },
  data() {
    return {
      darkmode: this.$localStorage.getItem("darkmode") === "dark"
    };
  },
  methods: {
    async submit() {
      if (!this.phone || !this.email) {
        this.$vs.notify({
          title: "Không hợp lệ",
          text: "Bạn phải nhập đủ cả tên và số điện thoại",
          color: "warning",
          position: "top-center"
        });

        return;
      }

      this.$vs.loading();

      try {
        await this.$store.dispatch("updateProfile", {
          fullname: this.fullname,
          phone: this.phone
        });
      } catch (e) {
        this.$vs.loading.close();

        // Catch
        console.log(e);

        this.$vs.notify({
          title: "Thất bại",
          text: "Cập nhật hồ sơ thất bại",
          color: "danger",
          position: "top-center"
        });

        return;
      }

      this.$vs.loading.close();

      this.$vs.notify({
        title: "Thành công",
        text: "Cập nhật hồ sơ thành công",
        color: "primary",
        position: "top-center"
      });
    },
    test(bool = false) {
      if (!bool) {
        this.$store.dispatch("openTxPopup", transferTx);
      } else {
        this.$store.dispatch("openTxPopup", createTx);
      }
    }
  },
  mounted() {
    const canvas = document.getElementById("canvas");
    this.email &&
      QRCode.toCanvas(canvas, `${this.email}`, { width: 200 }, function(error) {
        if (error) console.error(error);
      });
  }
};
</script>

<style lang="scss" scoped>
.vx-card {
  max-width: 600px;
}
</style>
