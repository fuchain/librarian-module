<template>
  <div class="h-screen flex w-full bg-img">
    <div
      class="vx-col flex items-center justify-center flex-col sm:w-1/3 md:w-3/5 lg:w-3/4 xl:w-1/2 mx-auto text-center"
    >
      <img
        src="@/assets/images/pages/404.png"
        alt="graphic-404"
        class="mx-auto mb-4"
        style="max-width: 250px"
      />
      <h1
        class="mb-12 pl-4 pr-4 text-5xl d-theme-heading-color"
        style="color: white;"
      >404 - Không tồn tại!</h1>
      <p
        class="mb-16 d-theme-text-inverse ml-4 mr-8"
        style="background: white; padding: 0.5rem; border-radius: 5px;"
      >Vui lòng báo cho nhà trường nếu bạn nghĩ rằng đây là lỗi của hệ thống.</p>
      <vs-button size="large" to="/">Quay lại thư viện</vs-button>
    </div>
  </div>
</template>
