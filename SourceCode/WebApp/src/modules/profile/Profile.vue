<template>
  <vx-card title="Hồ sơ độc giả">
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Họ tên</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="fullname" disabled/>
      </div>
    </div>
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Email</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" type="email" v-model="email" disabled/>
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
    <div class="vx-row mb-6">
      <div class="vx-col sm:w-1/3 w-full">
        <span>Tài khoản Facebook</span>
      </div>
      <div class="vx-col sm:w-2/3 w-full">
        <vs-input class="w-full" v-model="facebook"/>
      </div>
    </div>
    <div class="vx-row">
      <div class="vx-col sm:w-2/3 w-full ml-auto">
        <vs-button class="mr-3 mb-2" @click="submit">Cập nhật thông tin</vs-button>
      </div>
    </div>
  </vx-card>
</template>


<script>
export default {
  data() {
    return {
      fullname: "Huỳnh Minh Tú",
      email: "tuhmse62531@fpt.edu.vn",
      phone: "0796870446",
      facebook: "https://wwww.facebook.com/mr.huynhminhtu"
    };
  },
  methods: {
    submit() {
      if (!this.phone.trim()) return;
      this.$vs.loading();
      setTimeout(
        function() {
          this.$vs.loading.close();

          this.$vs.notify({
            title: "Thành công",
            text: "Cập nhật hồ sơ thành công",
            color: "primary",
            position: "top-center"
          });
        }.bind(this),
        1000
      );
    }
  }
};
</script>

<style lang="scss">
</style>
