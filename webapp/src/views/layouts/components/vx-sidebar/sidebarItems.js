import auth from "@auth";

export default function(numOfBooks) {
  if (auth.isAdmin()) {
    return adminMenu;
  }
  return userMenu(numOfBooks);
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
    url: "/librarian/queue",
    name: "Sách đang ghép",
    slug: "queue",
    icon: "LayersIcon"
  },
  {
    url: "/librarian/user-manage",
    name: "Quản lí tài khoản",
    slug: "users-manage",
    icon: "UsersIcon"
  },
  {
    url: "/librarian/notification/send",
    name: "Gửi thông báo",
    slug: "notification-send",
    icon: "SendIcon",
    tag: "beta",
    tagColor: "darkorange"
  },
  {
    url: null,
    name: "Theo dõi vận hành",
    icon: "CloudIcon",
    submenu: [
      {
        url: "/librarian/monitoring/infrastructure",
        name: "Hạ tầng Swarm",
        tag: "3",
        tagColor: "#254356"
      },
      {
        url: "/librarian/monitoring/bigchain",
        name: "Mạng Blockchain",
        tag: "8",
        tagColor: "#435261"
      }
    ]
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

function userMenu(numOfBooks) {
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
      url: "/transfer-history",
      name: "Lịch sử nhận sách",
      slug: "transfer-history",
      icon: "FileTextIcon"
    },
    {
      url: "/qrscan",
      name: "Quét mã QR",
      slug: "qrscan",
      icon: "CodeIcon",
      tag: "beta",
      tagColor: "primary"
    },
    {
      url: "/review-ux",
      name: "Đánh giá",
      slug: "review-ux",
      icon: "ThumbsUpIcon",
      tag: "FUCoin",
      tagColor: "orangered"
    }
  ];

  return menu;
}
