import express from "express";
import controller from "@controllers/book.controller";

const router = express.Router();

router.route("/search").get(controller.searchBookDetails);
router.route("/book_detail/:id").get(controller.getBookDetail);

export default router;
