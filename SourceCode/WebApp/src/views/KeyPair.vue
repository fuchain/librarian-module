<template>
  <div class="h-screen flex w-full login-bg-img" id="page-login">
    <div class="vx-col sm:w-1/2 md:w-1/2 lg:w-3/4 xl:w-3/5 mx-auto self-center">
      <vx-card>
        <div slot="no-body" class="full-page-bg-color">
          <div class="vx-row">
            <div class="vx-col hidden sm:hidden md:hidden lg:block lg:w-1/2 mx-auto self-center">
              <img
                src="https://duythanhcse.files.wordpress.com/2018/03/blockchain-logo-blue61.png"
                style="width: 300px;"
                alt="login"
                class="mx-auto"
              />
            </div>
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center bg-white bg-dark">
              <div class="p-8">
                <div class="vx-card__title mb-8">
                  <h4 class="mb-4">Thư viện FPTU University</h4>
                  <p>
                    Bạn đã đăng nhập email nhưng
                    <strong>chìa khóa không hợp lệ</strong> vui lòng cung cấp chìa khóa
                  </p>
                </div>
                <form v-on:submit.prevent="doLogin">
                  <vs-input
                    type="password"
                    name="password"
                    icon="icon icon-lock"
                    icon-pack="feather"
                    label-placeholder="Chuỗi chìa khóa bí mật"
                    v-model="password"
                    class="w-full mt-6 no-icon-border my-5"
                  />

                  <vs-button
                    class="float-right mb-8"
                    icon="fingerprint"
                    :disabled="!email || !password"
                  >Kí xác thực</vs-button>
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
export default {
  mounted() {
    const alice = new BigchainDB.Ed25519Keypair(
      window.bip39.mnemonicToSeed("yourString").slice(0, 32)
    );

    console.log(alice);
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
