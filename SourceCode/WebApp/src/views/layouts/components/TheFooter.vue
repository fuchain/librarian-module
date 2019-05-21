<template>
  <footer class="the-footer flex-wrap justify-between" :class="classes">
    <span>
      BẢN QUYỀN @ {{ new Date().getFullYear() }}
      <a
        href="https://daihoc.fpt.edu.vn"
        target="_blank"
        rel="nofollow"
      >Đại học FPT</a>, Tất cả mọi quyền được bảo lưu
    </span>
    <span class="md:flex hidden items-center">
      <span>
        Được phát triển bởi
        <strong>Nhóm đồ án Đào tạo</strong> nội bộ
      </span>
    </span>
  </footer>
</template>

<script>
export default {
    name: "the-footer",
    props: {
        classes: {
            type: String
        }
    }
};
</script>
