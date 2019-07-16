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
                  <h4 class="mb-4">Thư viện FPTU University</h4>
                  <vs-alert
                    class="mt-2 mb-2"
                    active="true"
                    icon="vpn_key"
                  >Chìa khóa chứng thực hợp lệ</vs-alert>
                </div>
                <form v-on:submit.prevent="doLogin">
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

                  <vs-button class="float-right mb-6" icon="fingerprint" disabled>Đăng nhập</vs-button>
                </form>

                <vs-divider>HOẶC</vs-divider>

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
                      Đăng nhập bằng email @fpt.edu.vn
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </vx-card>
    </div>
  </div>
</template>

<script>
import initSocket from "@core/socket";

export default {
  data() {
    return {
      email: null,
      password: null,
      remember: false
    };
  },
  methods: {
    doLogin() {
      //
    },
    loginWithGoogle: function() {
      const clientId =
        "505153044223-3a6ohprurmp1ih0rr7tbupl8bjqa9qvv.apps.googleusercontent.com";
      const hostname = window.location.href;
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
    }
  },
  mounted: function() {
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
          public_key: this.$localStorage.getItem("publicKey")
        })
        .then(async response => {
          // Set data;
          const data = response.data;
          this.$auth.setAccessToken(data.token);
          this.$localStorage.setItem("picture", data.picture);

          // Get profile
          await this.$store.dispatch("getProfile");
          await this.$store.dispatch("getNumOfBooks");

          // Get notification
          await this.$store.dispatch("getNotification");

          // Socket
          initSocket();

          this.$router.push("/");
        })
        .catch(err => {
          if (
            err &&
            err.response &&
            err.response.data &&
            err.response.data.message
          ) {
            this.$vs.notify({
              title: "Response from system",
              text: err.response.data.message,
              color: "danger",
              position: "top-right",
              fixed: true
            });
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
