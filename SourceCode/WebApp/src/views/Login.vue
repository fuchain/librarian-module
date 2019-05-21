<template>
  <div class="h-screen flex w-full bg-img" id="page-login">
    <div class="vx-col sm:w-1/2 md:w-1/2 lg:w-3/4 xl:w-3/5 mx-auto self-center">
      <vx-card>
        <div slot="no-body" class="full-page-bg-color">
          <div class="vx-row">
            <div class="vx-col hidden sm:hidden md:hidden lg:block lg:w-1/2 mx-auto self-center">
              <img src="@/assets/images/pages/login.png" alt="login" class="mx-auto">
            </div>
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center bg-white bg-dark">
              <div class="p-8">
                <div class="vx-card__title mb-8">
                  <h4 class="mb-4">Thư viện FPTU University</h4>
                  <p>Vui lòng đăng nhập bằng email trường để sử dụng dịch vụ.</p>
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
                    <router-link>Forgot Password?</router-link>
                  </div>
                  <vs-button class="float-right">Đăng nhập</vs-button>
                </form>

                <vs-divider>HOẶC</vs-divider>

                <div class="social-login flex flex-wrap justify-right">
                  <div class="social-login-buttons flex flex-wrap items-center mt-4">
                    <!-- GOOGLE -->
                    <div
                      class="bg-google pt-3 pb-2 px-4 rounded-lg cursor-pointer mr-4"
                      style="background-color: red; color: white;"
                      @click="$vs.notify({title: 'Đăng nhập bằng Google', text: 'Tính năng chưa hỗ trợ', position: 'top-right'})"
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
                        ></path>
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
export default {
    data() {
        return {
            email: null,
            password: null,
            remember: false
        };
    },
    methods: {
        doLogin: function() {
            if (!this.email || !this.password) {
                this.$vs.notify({
                    title: "Không hợp lệ",
                    text: "Email hoặc mật khẩu bị thiếu",
                    color: "warning",
                    position: "top-right"
                });

                return;
            }

            this.$vs.loading({
                type: "corners",
                text: "Đang tải"
            });

            this.$http
                .get("https://jsonplaceholder.typicode.com/todos/1")
                .then(() => {
                    this.$auth.setAccessToken(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTgxMDg3NDIsInVzZXJfaWQiOjEsImlzX2FkbWluIjp0cnVlLCJleHRfaW5mbyI6e30sInJvbGVzIjpbXX0.PKWuvQUG1deq8Bl4D03TVCM-oFnp6yO76NEjaECtjvc"
                    );
                    this.$auth.setRefreshToken(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTgwMjU5NDIsInVzZXJfaWQiOjEsImlzX2FkbWluIjp0cnVlLCJleHRfaW5mbyI6e30sInJvbGVzIjpbXX0.0D6IIl_02a5haj02YPzFYldibEsNMIFa6QXQWHGgbnY"
                    );
                    this.$auth.setAccessTokenExpiresAt("1558108742");

                    this.$vs.loading.close();
                    this.$router.push("/");
                })
                .catch(() => {
                    this.$vs.notify({
                        title: "Không hợp lệ",
                        text: "Email hoặc mật khẩu bị sai",
                        color: "danger",
                        position: "top-right"
                    });

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
</style>
