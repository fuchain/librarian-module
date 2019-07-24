<template>
  <div class="h-screen flex w-full login-bg-img" id="page-login">
    <div class="vx-col sm:w-1/2 md:w-1/2 lg:w-3/4 xl:w-3/5 mx-auto self-center">
      <vx-card>
        <div slot="no-body" class="full-page-bg-color">
          <div class="vx-row">
            <div class="vx-col hidden sm:hidden md:hidden lg:block lg:w-1/2 mx-auto self-center">
              <img
                src="@/assets/images/logo/logopng.png"
                style="width: 150px;"
                alt="login"
                class="mx-auto"
              />
            </div>
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center bg-white bg-dark">
              <div class="p-8">
                <div class="vx-card__title mb-8">
                  <h4 class="mb-4">Ví sách FPTU University</h4>
                  <p
                    v-if="!mode"
                  >Hệ thống không phát hiện ra chìa khóa bí mật của bạn trên thiết bị. Bạn đã tạo chìa khóa chưa?</p>
                  <p
                    v-else-if="mode === 'create'"
                  >Vui lòng chọn khóa an toàn (32 kí tự bí mật), trong đó có bao gồm mã số sinh viên của bạn, sau khi tạo nhớ lưu giữ khóa cẩn thận.</p>
                  <p
                    v-else
                  >Nhập vào khóa đã tạo của bạn (32 kí tự bí mật). Vui lòng chỉ nhận khóa này trên thiết bị của bạn.</p>
                </div>
                <div v-if="!mode">
                  <vs-button
                    color="red"
                    class="w-full mb-4"
                    icon="create"
                    @click="mode = 'create'"
                  >Bây giờ tạo</vs-button>
                  <vs-button
                    color="primary"
                    class="w-full"
                    icon="vpn_key"
                    @click="mode = 'verify'"
                  >Đã tạo rồi</vs-button>
                </div>
                <form v-on:submit.prevent="submitKey" v-if="mode">
                  <vs-input
                    type="password"
                    name="password"
                    icon="icon icon-lock"
                    icon-pack="feather"
                    label-placeholder="Chuỗi chìa khóa bí mật"
                    v-model="bip39"
                    maxlength="32"
                    class="w-full mt-6 no-icon-border my-5"
                  />

                  <div v-if="mode === 'verify'">
                    <vs-divider>Hoặc nhập chuỗi khóa</vs-divider>

                    <vs-input
                      icon="vpn_key"
                      label-placeholder="Chìa khóa công khai"
                      v-model="publicKey"
                      class="w-full mt-6 no-icon-border my-5"
                    />

                    <vs-input
                      icon="vpn_key"
                      label-placeholder="Chìa khóa bí mật"
                      v-model="privateKey"
                      class="w-full mt-6 no-icon-border my-5"
                    />
                  </div>

                  <vs-button
                    class="float-right mb-8"
                    icon="fingerprint"
                    :disabled="bip39.length < 32 && (!publicKey && !privateKey)"
                    @click="submitKey"
                  >{{ mode === 'create' ? "Tạo khóa bí mật mới" : "Xác nhận khóa bí mật"}}</vs-button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </vx-card>
    </div>
  </div>
</template>

<script>
import generateKeyPair from "@core/crypto/generateKeyPair";

export default {
  data() {
    return {
      bip39: "",
      publicKey: "",
      privateKey: "",
      mode: null
    };
  },
  methods: {
    async submitKey() {
      if (!this.bip39) {
        if (this.publicKey && this.privateKey) {
          this.$localStorage.setItem("publicKey", this.publicKey);
          this.$localStorage.setItem("privateKey", this.privateKey);

          this.$router.push("/");
        }

        return;
      }

      const keypair = await generateKeyPair(this.bip39);

      this.$localStorage.setItem("publicKey", keypair.publicKey);
      this.$localStorage.setItem("privateKey", keypair.privateKey);

      this.$router.push("/");
    }
  }
};
</script>

<style lang="scss">
#page-login {
  .social-login {
    .bg-facebook {
      background-color: #1551b1;
    }
    .bg-twitter {
      background-color: #00aaff;
    }
    .bg-google {
      background-color: #1551b1;
    }
    .bg-github {
      background-color: #333;
    }
  }
}

.title-loading {
  color: white;
}
</style>
