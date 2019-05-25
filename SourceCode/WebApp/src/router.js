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
      component: () => import("@/views/layouts/main/Main.vue"),
      children: [
        // =============================================================================
        // Theme Routes
        // =============================================================================
        {
          path: "/",
          component: () => import("@/views/Home.vue")
        },
        {
          path: "books",
          component: () => import("@/modules/student/books/Books.vue")
        },
        {
          path: "books/return",
          component: () =>
            import("@/modules/student/books-return/BooksReturn.vue")
        },
        {
          path: "books/returning",
          component: () =>
            import("@/modules/student/books-returning/BooksReturning.vue")
        },
        {
          path: "books/request",
          component: () =>
            import("@/modules/student/books-request/BooksRequest.vue")
        },
        {
          path: "books/requesting",
          component: () =>
            import("@/modules/student/books-requesting/BooksRequesting.vue")
        },
        {
          path: "books/pair",
          component: () => import("@/modules/student/books-pair/BooksPair.vue")
        }
      ]
    },
    // =============================================================================
    // FULL PAGE LAYOUTS
    // =============================================================================
    {
      path: "",
      component: () => import("@/views/layouts/full-page/FullPage.vue"),
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
      component: () => import("@/views/layouts/main/Main.vue"),
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
