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
                  <h4 class="mb-4">Ví sách FPTU University</h4>
                  <p
                    v-if="!mode"
                  >Hệ thống không phát hiện ra chìa khóa bí mật của bạn trên thiết bị. Bạn đã tạo chìa khóa chưa?</p>
                  <p
                    v-else-if="mode === 'create'"
                  >Vui lòng cất giữ cụm từ bí mật sau một cách cẩn thận (ghi ra giấy, cất nơi an toàn), không để cho bất kì ai khác biết cụm từ này.</p>
                  <p
                    v-else
                  >Nhập vào cụm từ bí mật của bạn để kích hoạt ví sách. Vui lòng chỉ nhập trên thiết bị mà bạn tin tưởng.</p>
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
                    icon="extension"
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
</style>
