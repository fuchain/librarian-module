<template>
  <vx-card v-if="$localStorage.getItem('fucoin') <= 0">
    <h2 class="mb-4">Đánh giá trải nghiệm</h2>
    <vs-alert active="true" class="mb-4">
      Nhận ngay
      <strong>5</strong> FUCoin về tài khoản
    </vs-alert>

    <star-rating
      :border-width="4"
      border-color="#d8d8d8"
      :rounded-corners="true"
      :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"
      v-model="rating1"
    ></star-rating>
    <h6 class="mt-6">Menu và các chỉ mục của ứng dụng</h6>
    <star-rating
      :border-width="4"
      border-color="#d8d8d8"
      :rounded-corners="true"
      :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"
      v-model="rating2"
    ></star-rating>
    <h6 class="mt-6">Danh sách hiển thị rõ ràng hợp mắt</h6>
    <star-rating
      :border-width="4"
      border-color="#d8d8d8"
      :rounded-corners="true"
      :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"
      v-model="rating3"
    ></star-rating>
    <h6 class="mt-6">Các nút bấm được bố trí phù hợp</h6>
    <star-rating
      :border-width="4"
      border-color="#d8d8d8"
      :rounded-corners="true"
      :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"
      v-model="rating4"
    ></star-rating>
    <h6 class="mt-6">Trải nghiệm khi mượn / trả sách</h6>
    <star-rating
      :border-width="4"
      border-color="#d8d8d8"
      :rounded-corners="true"
      :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"
      v-model="rating5"
    ></star-rating>
    <div class="mt-6">
      <vs-textarea
        label="Đề nghị cải thiện"
        v-model="note"
        placeholder="Ghi thêm đề nghị cải thiện để nhận thêm 2 FUCoin"
      />
    </div>
    <div class="mt-6">
      <vs-button
        color="primary"
        type="filled"
        size="large"
        class="w-full"
        @click="submit"
        icon="check"
      >Đánh giá {{ avgPoint }}/5 điểm!</vs-button>
    </div>
  </vx-card>
  <vx-card v-else>
    <h2 class="mb-4">Xin cảm ơn bạn!</h2>
    <p>Đội ngũ phát triển rất cảm ơn bạn đã đóng góp đánh giá trải nghiệm người dùng để chúng tôi có thể cải thiện sản phẩm tốt hơn.</p>
    <p class="mt-4">
      <strong>Nhiệm vụ tiếp theo:</strong> Thực hiện mượn thành công một quyển sách bất kì để nhận tiếp 5 FUCoin.
    </p>
    <div class="mt-4">
      <vs-button size="large" color="darkorange" icon="money" to="/coin">Đổi quà FUCoin</vs-button>
    </div>
  </vx-card>
</template>

<script>
import StarRating from "vue-star-rating";
import { setTimeout } from "timers";

export default {
  components: {
    StarRating
  },
  data() {
    return {
      rating1: 3,
      rating2: 3,
      rating3: 3,
      rating4: 3,
      rating5: 3,
      note: ""
    };
  },
  computed: {
    avgPoint() {
      return (
        (this.rating1 +
          this.rating2 +
          this.rating3 +
          this.rating4 +
          this.rating5) /
        5
      );
    }
  },
  methods: {
    submit() {
      const data = {
        ratings: [
          this.rating1,
          this.rating2,
          this.rating3,
          this.rating4,
          this.rating5
        ],
        note: this.note
      };

      this.$vs.loading({
        type: "radius",
        text: "Đang đào FUCoin",
        color: "black",
        background: "darkorange"
      });

      setTimeout(
        async function() {
          try {
            await this.$http.post(`${this.$http.betaUrl}/reviews`, data);
          } catch (e) {
            // Catch error
            console.log(e);

            this.$vs.loading.close();

            this.$vs.notify({
              title: "Thất bại",
              text: "Bạn không thể gian lận khi đào coin, rất tiếc",
              color: "danger",
              position: "top-center"
            });

            return;
          }

          this.$vs.loading.close();

          this.$vs.notify({
            title: "Đã gửi",
            text:
              "Cảm ơn bạn đã đánh giá ứng dụng, FUCoin đã được chuyển vào tài khoản của bạn",
            color: "primary",
            position: "top-center"
          });

          this.$store.dispatch("addCoin", this.note.trim() ? 7 : 5);

          this.$router.push("/");
        }.bind(this),
        5000
      );
    }
  }
};
</script>
