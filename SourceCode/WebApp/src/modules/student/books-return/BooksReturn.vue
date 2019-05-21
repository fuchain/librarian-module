<template>
  <form-wizard
    color="rgba(var(--vs-primary), 1)"
    title="Gửi trả sách"
    subtitle="Gửi sách cho sinh viên khác"
    finishButtonText="Xong"
    nextButtonText="Tiếp theo"
    backButtonText="Quay lại"
    @on-complete="formSubmitted"
    @on-change="handleStepChange"
    :custom-buttons-left="false"
  >
    <tab-content title="Xác nhận thông tin" class="mb-5" :before-change="loading">
      <!-- tab 1 content -->
      <div class="vx-row">
        <div class="vx-col w-1/2 mt-5">
          <vs-input
            size="large"
            label="Mã số sinh viên"
            v-model="studentID"
            disabled
            class="w-full"
          />
        </div>
        <div class="vx-col w-1/2 mt-5">
          <vs-input size="large" label="Họ Tên" v-model="fullName" disabled class="w-full"/>
        </div>
        <div class="vx-col w-1/2 mt-5">
          <vs-input
            type="email"
            size="large"
            label="Sách muốn trả"
            v-model="bookName"
            disabled
            class="w-full"
          />
        </div>
        <div class="vx-col w-1/2 mt-5">
          <vs-input
            type="email"
            size="large"
            label="Mã sách"
            v-model="bookID"
            disabled
            class="w-full"
          />
        </div>
      </div>
    </tab-content>

    <tab-content title="Chọn người nhận sách" class="mb-5" :before-change="loading">
      <!-- tab 2 content -->
      <div class="vx-row">
        <div class="vx-col w-1/2">
          <vs-input
            size="large"
            label="Mã số sinh viên"
            v-model="receiveStudentID"
            class="w-full mt-5"
          />
          <vs-button
            class="w-full mt-5"
            type="relief"
            color="primary"
            @click="loadStudentName"
          >Xác nhận tên sinh viên nhận</vs-button>
          <vs-input
            size="large"
            label="Họ tên"
            disabled
            v-model="receiveStudentName"
            class="w-full mt-5"
          />
        </div>
        <div class="vx-col w-1/2 mt-10">
          <vs-textarea v-model="textarea" label="Tin nhắn mô tả" class="h-full"/>
        </div>
      </div>
    </tab-content>

    <tab-content title="Chờ phản hồi" class="mb-5">
      <!-- tab 3 content -->
      <div class="vx-row">
        <vx-card>Thông tin đã được gửi, bạn sẽ nhận được thông báo khi hệ thống xác nhận.</vx-card>
      </div>
    </tab-content>
  </form-wizard>
</template>

<script>
import { FormWizard, TabContent } from "vue-form-wizard";
import "vue-form-wizard/dist/vue-form-wizard.min.css";

export default {
  data() {
    return {
      studentID: "SE62532",
      fullName: "Huỳnh Minh Tú",
      bookID: "FULIBHCM29384",
      bookName: "Introduce to Software Engineering",
      receiveStudentName: ""
    };
  },
  methods: {
    formSubmitted() {
      this.$router.push("/books/returning");
    },
    handleStepChange(id1, id2) {
      console.log(id1);
      console.log(id2);
    },
    async fakeLoad() {
      return new Promise((resolve, reject) => {
        this.$vs.loading();
        setTimeout(
          function() {
            this.$vs.loading.close();
            resolve();
          }.bind(this),
          1000
        );
      });
    },
    async loading() {
      await this.fakeLoad();
      return true;
    },
    async loadStudentName() {
      await this.fakeLoad();

      this.receiveStudentName = "Đoàn Vũ Phong";
    }
  },
  components: {
    FormWizard,
    TabContent
  }
};
</script>
