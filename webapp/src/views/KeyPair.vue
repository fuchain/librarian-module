<template>
  <div class="h-screen flex w-full login-bg-img" id="page-login">
    <div class="vx-col sm:w-1/2 md:w-1/2 lg:w-3/4 xl:w-3/5 mx-auto self-center">
      <vx-card>
        <div slot="no-body" class="full-page-bg-color">
          <div class="vx-row">
            <div
              class="vx-col lg:block lg:w-1/2 mx-auto self-center"
              @click="redirectToLP()"
              style="cursor: pointer;"
            >
              <img
                src="@/assets/images/logo/logopng.png"
                style="width: 120px; margin-top: 10px; margin-bottom: 10px;"
                alt="login"
                class="mx-auto"
              />
            </div>
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center bg-white bg-dark">
              <div class="p-8">
                <div class="vx-card__title mb-8">
                  <div class="mb-4">
                    <feather-icon
                      class="back-btn rounded-lg mr-4"
                      style="color: #7367F0;"
                      icon="ArrowLeftIcon"
                      @click="reset()"
                      v-if="mode"
                    ></feather-icon>
                    <span style="font-size: 2rem; color: #7367F0;">Ví sách FU</span>
                    <div class="mt-2 mb-4" style="text-align: right;">
                      Powered by
                      <span
                        style="background: #7367F0; color: white; padding: 0.5rem; border-radius: 5px;"
                      >FUChain</span>
                    </div>
                  </div>
                  <p
                    v-if="!mode"
                  >Ứng dụng không tìm thấy chìa khóa ví sách của bạn trên thiết bị. Bạn có phải là người dùng mới?</p>
                  <p v-else-if="mode === 'create'">
                    Vui lòng cất giữ cụm từ bí mật sau một cách cẩn thận bằng cách
                    <strong>ghi ra giấy, cất nơi an toàn</strong>, không để cho bất kì ai khác biết cụm từ này.
                  </p>
                  <p v-else>
                    Nhập vào cụm từ bí mật của bạn để khôi phục ví sách. Chỉ nhập trên
                    <strong>thiết bị mà bạn tin tưởng.</strong>
                  </p>
                </div>
                <div v-if="!mode">
                  <vs-button
                    color="primary"
                    type="border"
                    class="w-full mb-4"
                    icon="create"
                    @click="mode = 'create'"
                  >Tạo ví sách cho tôi</vs-button>
                  <vs-button
                    color="primary"
                    class="w-full"
                    icon="vpn_key"
                    @click="mode = 'verify'"
                  >Tôi đã có ví sách rồi</vs-button>
                </div>
                <form v-on:submit.prevent="submitKey" v-if="mode">
                  <vs-alert v-if="mode !== 'verify'" active="true" class="mb-8">{{ seed }}</vs-alert>

                  <div v-if="mode === 'verify'">
                    <div v-if="style === 'auto'">
                      <vs-input
                        icon="vpn_key"
                        label-placeholder="Cụm từ bí mật lúc tạo ví"
                        v-model="secret"
                        class="w-full mt-6 no-icon-border my-5"
                      />
                    </div>
                    <div v-else>
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
                  </div>

                  <vs-button
                    color="primary"
                    type="border"
                    icon="work"
                    class="mb-4"
                    @click="changeStyle()"
                    v-if="mode === 'verify'"
                  ></vs-button>
                  <vs-button
                    class="float-right mb-8"
                    icon="fingerprint"
                    :disabled="(mode === 'verify' && !secret && (!publicKey && !privateKey))"
                    @click="submitKey"
                  >{{ mode === 'create' ? "Đã cất giữ an toàn cụm từ này" : "Xác nhận khóa bí mật"}}</vs-button>
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
import generateSeed from "@core/crypto/generateSeed";

export default {
  data() {
    return {
      publicKey: "",
      privateKey: "",
      mode: null,
      seed: generateSeed(),
      style: "auto",
      secret: ""
    };
  },
  methods: {
    async submitKey() {
      if (this.mode === "verify") {
        if (this.publicKey && this.privateKey) {
          this.$localStorage.setItem("publicKey", this.publicKey);
          this.$localStorage.setItem("privateKey", this.privateKey);
          this.$router.push("/");
        } else {
          const keypair = await generateKeyPair(this.secret);
          this.$localStorage.setItem("publicKey", keypair.publicKey);
          this.$localStorage.setItem("privateKey", keypair.privateKey);
          this.$router.push("/");
        }
      } else {
        const keypair = await generateKeyPair(this.seed);
        this.$localStorage.setItem("publicKey", keypair.publicKey);
        this.$localStorage.setItem("privateKey", keypair.privateKey);
        this.$router.push("/");
      }
    },
    changeStyle() {
      if (this.style === "auto") {
        this.style = "manual";
      } else {
        this.style = "auto";
      }
    },
    reset() {
      this.publicKey = "";
      this.privateKey = "";
      this.mode = null;
      this.style = "auto";
    },
    redirectToLP() {
      window.location.href = "https://blockchain.fptu.tech";
    }
  },
  mounted() {
    const pub = this.$localStorage.getItem("publicKey");
    const pri = this.$localStorage.getItem("privateKey");

    if (pub && pri) {
      this.$router.push("/login");
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

.back-btn {
  cursor: pointer;
  padding: 0.5rem 0.1rem;
}
</style>
