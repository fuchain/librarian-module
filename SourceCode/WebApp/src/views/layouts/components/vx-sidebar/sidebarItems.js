import auth from "@auth";

export default function() {
  if (auth.isAdmin()) {
    return adminMenu;
  }
  return userMenu;
}

const adminMenu = [
  {
    url: "/",
    name: "Trang chính",
    slug: "home",
    icon: "HomeIcon"
  },
  {
    url: "/book-search",
    name: "Tình trạng sách",
    slug: "book-search",
    icon: "BookIcon"
  },
  {
    url: "/book-return-request",
    name: "Thu hồi sách",
    slug: "book-return-request",
    icon: "ArchiveIcon"
  },
  {
    url: "/users-manage",
    name: "Quản lí tài khoản",
    slug: "users-manage",
    icon: "UsersIcon"
  }
];

const userMenu = [
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
    submenu: [
      {
        url: "/books",
        name: "Sách đang giữ",
        slug: "books"
      },
      {
        url: "/books/returning",
        name: "Sách đang trả",
        slug: "returning-books"
      },
      {
        url: "/books/requesting",
        name: "Sách đang yêu cầu",
        slug: "requesting-books"
      }
    ]
  },
  {
    url: "/books/request",
    name: "Yêu cầu mượn sách",
    slug: "request",
    icon: "BookOpenIcon"
  },
  {
    url: "/books/pair",
    name: "Nhập mã nhận sách",
    slug: "request",
    icon: "ShoppingBagIcon"
  },
  {
    url: "/report",
    name: "Báo cáo tình trạng",
    slug: "report",
    icon: "FileIcon"
  }
];
