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
          path: "/qrscan",
          component: () => import("@/modules/qrscan/QRScan.vue")
        },
        {
          path: "/coupon",
          component: () => import("@/modules/reader/coupon/Coupon.vue")
        },
        {
          path: "profile",
          component: () => import("@/modules/profile/Profile.vue")
        },
        {
          path: "chat",
          component: () => import("@/modules/chat/Chat.vue")
        },
        {
          path: "calendar",
          component: () => import("@/modules/calendar/Calendar.vue")
        },
        {
          path: "notifications",
          component: () => import("@/modules/notification/Notification.vue")
        },
        {
          path: "review-ux",
          component: () => import("@/modules/reader/review-ux/ReviewUX.vue")
        },
        {
          path: "books/keeping",
          component: () =>
            import("@/modules/reader/books-keeping/BooksKeeping.vue")
        },
        {
          path: "books/return",
          name: "book-return",
          component: () =>
            import("@/modules/reader/books-return/BooksReturn.vue"),
          props: true
        },
        {
          path: "books/returning",
          component: () =>
            import("@/modules/reader/books-returning/BooksReturning.vue")
        },
        {
          path: "books/request",
          component: () =>
            import("@/modules/reader/books-request/BooksRequest.vue")
        },
        {
          path: "books/requesting",
          component: () =>
            import("@/modules/reader/books-requesting/BooksRequesting.vue")
        },
        {
          path: "transfer-history",
          component: () =>
            import("@/modules/reader/transfer-history/TransferHistory.vue")
        },
        {
          path: "librarian/statistics",
          component: () =>
            import("@/modules/librarian/statistics/Statistics.vue")
        },
        {
          path: "librarian/review-report",
          component: () =>
            import("@/modules/librarian/review-report/ReviewReport.vue")
        },
        {
          path: "librarian/book-details-manage",
          component: () =>
            import(
              "@/modules/librarian/book-details-manage/BookDetailsManage.vue"
            )
        },
        {
          path: "librarian/book-details-manage/:id",
          component: () =>
            import(
              "@/modules/librarian/book-instance-manage/BookInstanceManage.vue"
            )
        },
        {
          path: "librarian/book-details-manage/:id/:bookid",
          component: () =>
            import(
              "@/modules/librarian/book-instance-manage/BookInstanceManage.vue"
            )
        },
        {
          path: "librarian/user-manage",
          component: () =>
            import("@/modules/librarian/user-manage/UserManage.vue")
        },
        {
          path: "librarian/monitoring/bigchain",
          component: () => import("@/modules/librarian/monitoring/BigChain.vue")
        },
        {
          path: "librarian/monitoring/infrastructure",
          component: () =>
            import("@/modules/librarian/monitoring/Infrastructure.vue")
        },
        {
          path: "librarian/monitoring/transactions",
          component: () =>
            import("@/modules/librarian/monitoring/Transactions.vue")
        },
        {
          path: "librarian/notification/send",
          component: () =>
            import("@/modules/librarian/notification/SendNotification.vue")
        },
        {
          path: "librarian/queue",
          component: () => import("@/modules/librarian/queue/Queue.vue")
        },
        {
          path: "librarian/qrscan",
          component: () => import("@/modules/qrscan/QRScan.vue")
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
        },
        {
          path: "/keypair",
          component: () => import("@/views/KeyPair.vue")
        },
        {
          path: "/error",
          component: () => import("@/views/Error500.vue")
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
  if (to.path === "/keypair") {
    next();
  } else if (to.path === "/login") {
    if (auth.isAuthenticated()) {
      next({ path: "/" });
    } else {
      next();
    }
  } else {
    if (!auth.isAuthenticated()) {
      next({ path: "/login" });
    } else {
      if (!auth.isAdmin()) {
        if (to.path.includes("/librarian")) {
          next({ path: "/" });
        }
      } else {
        if (to.path === "/") {
          next({ path: "/librarian/statistics" });
        } else if (to.path.includes("/books/")) {
          next({ path: "/librarian/statistics" });
        }
      }

      next();
    }
  }
});

export default router;
