<template>
  <v-tour name="vuesaxTour" :steps="steps" :options="tourOptions" :callbacks="callbacks">
    <template slot-scope="tour">
      <transition name="fade">
        <v-step
          v-if="tour.currentStep === index"
          v-for="(step, index) of tour.steps"
          :key="index"
          :step="step"
          :previous-step="tour.previousStep"
          :next-step="tour.nextStep"
          :stop="tour.stop"
          :is-first="tour.isFirst"
          :is-last="tour.isLast"
          :labels="tour.labels"
        >
          <div slot="actions" class="flex justify-center">
            <vs-button
              size="small"
              class="mr-3"
              @click="endTour()"
              icon-pack="feather"
              icon="icon-x"
              icon-after
              color="#fff"
              type="border"
              v-if="tour.currentStep != tour.steps.length - 1"
            >Tắt</vs-button>

            <vs-button
              size="small"
              @click="tour.previousStep"
              icon-pack="feather"
              icon="icon-chevrons-left"
              color="#fff"
              type="border"
              class="mr-3"
              v-if="tour.currentStep"
            >Trước</vs-button>

            <vs-button
              size="small"
              @click="handleAction(tour.currentStep === 1);"
              icon-pack="feather"
              icon="icon-chevrons-right"
              icon-after
              color="#fff"
              type="border"
              class="btn-tour-next"
              v-if="tour.currentStep != tour.steps.length - 1"
            >Tiếp</vs-button>

            <vs-button
              size="small"
              @click="endTour()"
              icon-pack="feather"
              icon="icon-check-circle"
              icon-after
              color="#fff"
              type="border"
              class="btn-tour-finish"
              v-if="tour.currentStep == tour.steps.length - 1"
            >Cảm ơn bạn</vs-button>
          </div>
        </v-step>
      </transition>
    </template>
  </v-tour>
</template>

<script>
export default {
  name: "vx-tour",
  props: {
    steps: {
      required: true,
      type: Array
    }
  },
  data() {
    return {
      tourOptions: {
        useKeyboardNavigation: true
      },
      callbacks: {}
    };
  },
  methods: {
    handleAction(bool) {
      if (bool) {
        this.$store.dispatch("openSidebar");

        setTimeout(
          function() {
            this.$tours["vuesaxTour"].nextStep();
          }.bind(this),
          10
        );
      } else {
        this.$tours["vuesaxTour"].nextStep();
      }
    },
    endTour() {
      this.$tours["vuesaxTour"].stop();
      this.$localStorage.setItem("tour", true);
    }
  }
};
</script>

<style lang="scss">
.v-tour {
  .v-step {
    z-index: 55000;
    background-color: rgba(var(--vs-primary), 1);
    border-radius: 0.5rem;
    padding: 1.5rem;
    filter: drop-shadow(0 0 7px rgba(0, 0, 0, 0.5));

    .v-step__arrow {
      border-color: rgba(var(--vs-primary), 1);
    }

    .vs-button-border:not(.btn-tour-next):not(.btn-tour-finish) {
      border-color: rgba(255, 255, 255, 0.5) !important;
    }
  }
}
</style>
