import auth from "@auth";

export default function(numOfBooks, coin = 0) {
  if (auth.isAdmin()) {
    return adminMenu;
  }
  return userMenu(numOfBooks, coin);
}

const adminMenu = [
  {
    url: "/librarian/statistics",
    name: "Thống kê",
    slug: "home",
    icon: "BarChartIcon",
    tag: "beta",
    tagColor: "darkorange"
  },
  {
    url: "/librarian/book-details-manage",
    name: "Quản lí đầu sách",
    slug: "book-manage",
    icon: "BookIcon"
  },
  {
    url: "/librarian/user-manage",
    name: "Quản lí tài khoản",
    slug: "users-manage",
    icon: "UsersIcon"
  },
  {
    url: "/librarian/review-report",
    name: "Đánh giá",
    slug: "review-ux",
    icon: "FileIcon",
    tag: "báo cáo",
    tagColor: "primary"
  }
];

function userMenu(numOfBooks, coin = 0) {
  const menu = [
    {
      url: "/",
      name: "Trang chính",
      slug: "home",
      icon: "HomeIcon"
    },
    {
      url: null,
      name: "Sách của tôi",
      slug: "books",
      icon: "BookIcon",
      tag: numOfBooks.numOfKeepingBooks + numOfBooks.numOfReturningBooks || "0",
      tagColor: "primary",
      submenu: [
        {
          url: "/books/keeping",
          name: "Sách đang giữ",
          slug: "books-keeping",
          tag: numOfBooks.numOfKeepingBooks || "0",
          tagColor: "primary"
        },
        {
          url: "/books/returning",
          name: "Sách đang trả",
          slug: "books-returning",
          tag: numOfBooks.numOfReturningBooks || "0",
          tagColor: "success"
        },
        {
          url: "/books/requesting",
          name: "Sách yêu cầu",
          slug: "books-requesting",
          tag: numOfBooks.numOfRequestingBooks || "0",
          tagColor: "violet"
        }
      ]
    },
    {
      url: "/books/request",
      name: "Tìm mượn sách",
      slug: "book-request",
      icon: "BookOpenIcon"
    },
    {
      url: "/books/pair",
      name: "Nhập mã nhận sách",
      slug: "book-pair",
      icon: "ShoppingBagIcon"
    },
    {
      url: "/coin",
      name: "Ví FUCoin",
      slug: "coin",
      icon: "DollarSignIcon",
      tag: `${coin} FUCoin`,
      tagColor: "darkorange"
    }
  ];

  if (!coin || coin <= 0) {
    menu.push(reviewMenu);
  }

  return menu;
}

const reviewMenu = {
  url: "/review-ux",
  name: "Đánh giá",
  slug: "review-ux",
  icon: "ThumbsUpIcon",
  tag: "tích coin",
  tagColor: "orangered"
};
