import express from "express";
import controller from "@controllers/librarian.controller";
import matchingController from "@controllers/matching.controller";

const router = express.Router();

router.route("/book_details").get(controller.getAllBookDetails);
router.route("/users").get(controller.getAllUsers);
router.route("/users/books").post(controller.getBookByUser);
router
    .route("/book_details/:book_detail_id/books")
    .get(controller.getBookInstanceList);
router
    .route("/book_details/:book_detail_id/books/details")
    .get(controller.getBookInstanceDetailList);
router.route("/book_history").post(controller.getHistoryOfBookInstance);
router.route("/overviews").get(controller.getOverview);
router.route("/book_total").post(controller.getBookTotalByBDID);
router.route("/library_book_total").post(controller.getBookTotalAtLib);
router.route("/give_book").post(controller.giveBook);
router.route("/matchings").get(matchingController.getMatchings);

export default router;
