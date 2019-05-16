import Vue from "vue";
import Router from "vue-router";

import auth from "@auth";

Vue.use(Router);

const router = new Router({
  mode: "history",
  base: process.env.BASE_URL,
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
  routes: [
    {
      // =============================================================================
      // MAIN LAYOUT ROUTES
      // =============================================================================
      path: "",
      component: () => import("./layouts/main/Main.vue"),
      children: [
        // =============================================================================
        // Theme Routes
        // =============================================================================
        {
          path: "/",
          component: () => import("./views/Home.vue")
        }
      ]
    },
    // =============================================================================
    // FULL PAGE LAYOUTS
    // =============================================================================
    {
      path: "",
      component: () => import("@/layouts/full-page/FullPage.vue"),
      children: [
        // =============================================================================
        // PAGES
        // =============================================================================
        {
          path: "/login",
          component: () => import("@/views/Login.vue")
        }
      ]
    },
    // Redirect to 404 page, if no match found
    {
      path: "*",
      component: () => import("./layouts/main/Main.vue"),
      children: [
        {
          path: "",
          component: () => import("@/views/Error404.vue")
        }
      ]
    }
  ]
});

router.afterEach(() => {
  // Remove initial loading
  const appLoading = document.getElementById("loading-bg");
  if (appLoading) {
    appLoading.style.display = "none";
  }
});

// Authentication
router.beforeEach((to, from, next) => {
  if (to.path === "/login") {
    if (auth.isAuthenticated()) {
      next({ path: "/" });
    } else {
      next();
    }
  } else {
    if (!auth.isAuthenticated()) {
      next({ path: "/login" });
    } else {
      next();
    }
  }
});

export default router;
