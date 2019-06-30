<template>
  <vx-card title="Hồ sơ">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Họ tên</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="fullname"/>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Email</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" type="email" :value="email" disabled/>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Số điện thoại</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="phone"/>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button class="mr-3 mb-2" @click="submit" icon="check">Cập nhật thông tin</vs-button>
      </div>
    </div>
  </vx-card>
</template>

<script>
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
    }
  }
};
</script>

<style lang="scss" scoped>
.vx-card {
  max-width: 800px;
}
</style>
