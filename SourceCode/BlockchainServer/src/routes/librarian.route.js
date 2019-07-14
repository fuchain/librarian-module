import express from "express";
import controller from "@controllers/librarian.controller";

const router = express.Router();

router.route("/book_details").get(controller.getAllBookDetails);
router.route("/users").get(controller.getAllUsers);
router.route("/users/books").post(controller.getBookByUser);
router.route("/book_details/:book_detail_id/books").get(controller.getBookInstanceList);
router.route("/book_history").post(controller.getHistoryOfBookInstance);
router.route("/overviews").get(controller.getOverview);

export default router;
