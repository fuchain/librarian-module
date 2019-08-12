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
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center bg-white">
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
                      <a
                        href="https://blockchain.fptu.tech"
                        target="_blank"
                        style="background: #7367F0; color: white; padding: 0.5rem; border-radius: 5px;"
                      >FUChain</a>
                    </div>
                  </div>
                  <p v-if="!mode">Bạn có phải người dùng mới?</p>
                  <p v-else-if="mode === 'create'">
                    Vui lòng cất giữ cụm từ bí mật sau một cách cẩn thận bằng cách
                    <strong>ghi ra giấy, cất nơi an toàn</strong>, không để cho bất kì ai khác biết cụm từ này.
                  </p>
                  <p v-else>
                    Nhập vào cụm 12 từ bí mật của bạn lúc tạo ví để đăng nhập ví sách. Chỉ nhập trên
                    <strong>thiết bị mà bạn tin tưởng.</strong>
                  </p>
                </div>
                <div v-if="!mode">
                  <vs-button
                    color="primary"
                    type="border"
                    class="w-full mb-4"
                    icon="create"
                    @click="createWallet"
                  >Tạo ví sách</vs-button>
                  <vs-button
                    color="primary"
                    type="relief"
                    class="w-full"
                    icon="vpn_key"
                    @click="mode = 'verify'"
                  >Đã có ví sách</vs-button>
                </div>
                <form v-on:submit.prevent="submitKey" v-if="mode">
                  <vs-alert v-if="mode !== 'verify'" active="true" class="mb-2">{{ seed }}</vs-alert>
                  <canvas v-if="mode !== 'verify'" class="float-right mb-2" id="canvas"></canvas>

                  <div v-if="mode === 'verify'">
                    <div v-if="style === 'auto'">
                      <vs-textarea label="Cụm từ bí mật lúc tạo ví" v-model="secret" />
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
                    <vx-card
                      title="Quét QRCode cuả ví"
                      class="mb-4"
                      noShadow
                      cardBorder
                      v-if="!error"
                    >
                      <QRScan class="mb-4" @printCode="notify" @onFail="handleFail" />
                    </vx-card>
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
                    :disabled="(mode === 'verify' && !secret && (!publicKey || !privateKey))"
                    @click="openConfirm"
                  >{{ mode === 'create' ? "Đã cất giữ an toàn cụm từ này" : "Xác thực ví sách"}}</vs-button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </vx-card>
    </div>

    <vs-popup
      class="introducePopup"
      style="color:rgb(255,255,255);"
      background-color="rgba(255,255,255,.6)"
      background-color-popup="#7367F0"
      title="Khoan... Ứng dụng này làm gì? 🤔"
      :active.sync="introducePopup"
    >
      <p>
        Hiện tại thư viện trường thường
        <strong>bị quá tải</strong> mỗi đầu kì do sinh viên phải đến xếp hàng mượn/trả sách, app này sẽ giúp sinh viên cầm sách kì trước có thể trả trực tiếp cho sinh viên kì sau mà
        <strong>không cần đi tới thư viện</strong>. 😆
      </p>
      <p
        class="mt-4"
      >Ứng dụng công nghệ Blockchain, mỗi sinh viên sẽ được tạo một ví sách. Khi thực hiện nhận trả sách sẽ thông qua ví sách để xác thực. Ví sách đảm bảo an toàn tuyệt đối khi mà chỉ có chủ ví mới có quyền thực hiện giao dịch chuyển nhận sách. 😎</p>
    </vs-popup>
  </div>
</template>

<script>
import generateKeyPair from "@core/crypto/generateKeyPair";
import generateSeed from "@core/crypto/generateSeed";
import keypair from "@core/crypto/keypair";
import QRScan from "@/views/components/QRScan.vue";
import QRCode from "qrcode";

export default {
  components: {
    QRScan
  },
  data() {
    return {
      publicKey: "",
      privateKey: "",
      mode: null,
      seed: generateSeed(),
      wallet: "",
      style: "auto",
      secret: "",
      introducePopup: true,
      error: false
    };
  },
  methods: {
    async submitKey() {
      if (this.mode === "verify") {
        if (this.publicKey && this.privateKey) {
          keypair.set("publicKey", this.publicKey);
          keypair.set("privateKey", this.privateKey);
          this.$router.push("/");
        } else {
          const keypairGenerate = await generateKeyPair(this.secret);
          keypair.set("publicKey", keypairGenerate.publicKey);
          keypair.set("privateKey", keypairGenerate.privateKey);
          this.$router.push("/");
        }
      } else {
        const keypairGenerate = await generateKeyPair(this.seed);
        keypair.set("publicKey", keypairGenerate.publicKey);
        keypair.set("privateKey", keypairGenerate.privateKey);
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
    },
    openConfirm() {
      if (this.mode === "verify") {
        this.submitKey();
        return;
      }
      this.$vs.dialog({
        type: "confirm",
        color: "danger",
        title: "Ví của bạn, bạn là người giữ!",
        text:
          "Bạn có chắc là đã cất giữ chuỗi bí mật? Khi bạn làm mất chuỗi này thì ví sách không thể khôi phục, bạn có thể sẽ mất phí để tạo ví sách mới.",
        accept: this.submitKey,
        acceptText: "Tôi biết rồi",
        cancelText: "Để xem đã"
      });
    },
    async notify(secretKey) {
      const keypairGenerate = await generateKeyPair(secretKey);
      keypair.set("publicKey", keypairGenerate.publicKey);
      keypair.set("privateKey", keypairGenerate.privateKey);
      this.$router.push("/");
    },
    handleFail(val) {
      if (val) {
        this.error = true;
      }
    },
    createWallet() {
      this.mode = "create";
      setTimeout(() => {
        const canvas = document.getElementById("canvas");
        QRCode.toCanvas(canvas, `${this.seed}`, { width: 250 }, function(
          error
        ) {
          if (error) console.error(error);
        });
      }, 100);
    }
  },
  async mounted() {
    const publicKey = this.$localStorage.getItem("publicKey");
    const privateKey = this.$localStorage.getItem("privateKey");

    if (publicKey && privateKey) {
      this.$router.push("/login");
    }

    const generated = await generateKeyPair(this.seed);
    this.wallet = generated.publicKey;
    console.log(generated);
  }
};
</script>

<style lang="scss">
.introducePopup {
  h3 {
    color: #fff;
  }
}

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
