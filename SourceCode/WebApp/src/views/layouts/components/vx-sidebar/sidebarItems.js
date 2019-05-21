export default [
  {
    url: "/",
    name: "Thư viện",
    slug: "home",
    icon: "HomeIcon"
  },
  {
    url: null,
    name: "Kho sách",
    slug: "books",
    icon: "BookIcon",
    submenu: [
      {
        url: "/books",
        name: "Sách đang mượn",
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
    url: "/request",
    name: "Yêu cầu mượn sách",
    slug: "request",
    icon: "BookOpenIcon"
  },
  {
    url: "/report",
    name: "Báo cáo tình trạng",
    slug: "report",
    icon: "FileIcon"
  }
];
