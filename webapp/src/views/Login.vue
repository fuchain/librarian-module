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
                src="/blockchain.svg"
                style="width: 220px; margin-top: 10px; margin-bottom: 10px;"
                alt="login"
                class="mx-auto"
              />
            </div>
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center bg-white">
              <div class="p-8">
                <div class="vx-card__title mb-8">
                  <h4 class="mb-4" style="color: #7367F0;">Thư viện FPTU University</h4>
                </div>

                <div class="mt-8 mb-4" v-if="!mode" style="text-align: right;">
                  Powered by
                  <span
                    style="background: #7367F0; color: white; padding: 0.5rem; border-radius: 5px;"
                  >FUChain</span>
                </div>

                <form v-on:submit.prevent="doLogin" v-if="false">
                  <vs-input
                    name="email"
                    icon="icon icon-user"
                    icon-pack="feather"
                    label-placeholder="Email"
                    v-model="email"
                    class="w-full no-icon-border"
                  />

                  <vs-input
                    type="password"
                    name="password"
                    icon="icon icon-lock"
                    icon-pack="feather"
                    label-placeholder="Mật khẩu"
                    v-model="password"
                    class="w-full mt-6 no-icon-border"
                  />

                  <div class="flex flex-wrap justify-between my-5">
                    <vs-checkbox v-model="remember" class="mb-3">Lưu mật khẩu</vs-checkbox>
                  </div>
                  <vs-button class="float-right mb-6" icon="fingerprint" disabled="true">Đăng nhập</vs-button>
                </form>

                <div class="social-login flex flex-wrap justify-right">
                  <div class="social-login-buttons flex flex-wrap items-center mt-4">
                    <!-- GOOGLE -->
                    <div
                      class="bg-google pt-3 pb-2 px-4 rounded-lg cursor-pointer mr-4"
                      style="background-color: red; color: white;"
                      @click="loginWithGoogle"
                    >
                      <svg
                        aria-hidden="true"
                        focusable="false"
                        data-prefix="fab"
                        data-icon="google"
                        class="text-white h-4 w-4 svg-inline--fa fa-google fa-w-16"
                        role="img"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 488 512"
                        style="margin-right: 0.5rem;"
                      >
                        <path
                          fill="currentColor"
                          d="M488 261.8C488 403.3 391.1 504 248 504 110.8 504 0 393.2 0 256S110.8 8 248 8c66.8 0 123 24.5 166.3 64.9l-67.5 64.9C258.5 52.6 94.3 116.6 94.3 256c0 86.5 69.1 156.6 153.7 156.6 98.2 0 135-70.4 140.8-106.9H248v-85.3h236.1c2.3 12.7 3.9 24.9 3.9 41.4z"
                        />
                      </svg>
                      {{ loginText }}
                    </div>
                  </div>

                  <div
                    class="mt-8"
                    v-if="!walletEmail && loaded"
                    style="background-color: #7367F0; color: white; padding: 10px; border-radius: 5px;"
                  >
                    <div class="mb-2">Địa chỉ ví</div>
                    <div>{{ publicKey.slice(0, 22) }}</div>
                    <div>{{ publicKey.slice(22, publicKey.length) }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </vx-card>
      <vs-button
        type="relief"
        color="danger"
        class="float-right mt-4 mr-4"
        icon="clear"
        @click="deleteConfirm()"
      >Đăng xuất ví sách</vs-button>
    </div>
  </div>
</template>

<script>
import initSocket from "@core/socket";
import keypair from "@core/crypto/keypair";

export default {
  data() {
    return {
      email: null,
      password: null,
      remember: false,
      walletEmail: null,
      loaded: false,
      publicKey: keypair.get("publicKey")
    };
  },
  computed: {
    loginText() {
      if (!this.loaded) {
        return "Đăng nhập bằng email của bạn";
      }

      if (this.walletEmail) {
        return "Đăng nhập " + this.walletEmail;
      }

      return "Hoàn tất tạo ví, đăng nhập email cuả bạn";
    }
  },
  methods: {
    doLogin() {
      //
    },
    loginWithGoogle: function() {
      const clientId =
        "505153044223-3a6ohprurmp1ih0rr7tbupl8bjqa9qvv.apps.googleusercontent.com";
      const hostname = window.location.href.replace(window.location.hash, "");
      const scope = "profile email openid";

      window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?scope=${scope}&include_granted_scopes=true&state=state_parameter_passthrough_value&redirect_uri=${hostname}&response_type=token&client_id=${clientId}`;
    },
    parseToken: function(fragmentStr) {
      // Parse query string to see if page request is coming from OAuth 2.0 server.
      const params = {};
      const regex = /([^&=]+)=([^&]*)/g;
      let m;
      while ((m = regex.exec(fragmentStr))) {
        params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
      }

      return params["access_token"];
    },
    removeKey() {
      this.$localStorage.removeItem("publicKey");
      this.$localStorage.removeItem("privateKey");

      this.$router.push("/keypair");
    },
    deleteConfirm() {
      this.$vs.dialog({
        type: "confirm",
        color: "danger",
        title: "Xác nhận",
        text: "Bạn có chắc muốn gỡ ví sách trên thiết bị này?",
        accept: this.removeKey,
        acceptText: "Chắc chắn",
        cancelText: "Hủy bỏ"
      });
    },
    redirectToLP() {
      window.location.href = "https://blockchain.fptu.tech";
    }
  },
  mounted() {
    const accessToken = this.parseToken(window.location.hash);

    if (accessToken) {
      this.$vs.loading({
        background: "darkorange",
        color: "white",
        text: "Đang xác thực"
      });

      this.$http
        .post(`${this.$http.baseUrl}/auth/keypair/email`, {
          token: accessToken,
          public_key: keypair.get("publicKey")
        })
        .then(async response => {
          // Set data;
          const data = response.data;
          this.$auth.setAccessToken(data.token);
          this.$localStorage.setItem("picture", data.picture);

          // Get profile
          await this.$store.dispatch("getProfile");
          !this.$auth.isAdmin() && this.$store.dispatch("getNumOfBooks");

          // Get notification
          this.$store.dispatch("getNotification");

          // Socket
          initSocket();

          this.$router.push("/");

          setTimeout(
            function() {
              if (
                this.$auth.isAuthenticated() &&
                !this.$auth.isAdmin() &&
                this.$tours["vuesaxTour"]
              ) {
                this.$tours["vuesaxTour"].start();
              }
            }.bind(this),
            500
          );
        })
        .catch(err => {
          if (
            err &&
            err.response &&
            err.response.data &&
            err.response.data.message
          ) {
            this.$vs.notify({
              title: "Phản hồi từ hệ thống",
              text: err.response.data.message,
              color: "danger",
              position: "top-right",
              fixed: true
            });

            // this.$localStorage.removeItem("publicKey");
            // this.$localStorage.removeItem("privateKey");
            this.$auth.setAccessToken();
            this.$router.push("/");
          }

          this.$vs.notify({
            title: "Không hợp lệ",
            text: "Đăng nhập không hợp lệ",
            color: "warning",
            position: "top-right"
          });
        })
        .finally(() => {
          this.$vs.loading.close();
        });
    } else {
      this.$vs.loading({
        background: "darkorange",
        color: "white",
        text: "Đang xác thực ví sách"
      });
      this.$http
        .post(`${this.$http.baseUrl}/fetch/public_key`)
        .then(res => {
          this.walletEmail = res.data.email;
        })
        .catch(() => {
          this.walletEmail = null;
        })
        .finally(() => {
          this.loaded = true;
          this.$vs.loading.close();
        });
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
