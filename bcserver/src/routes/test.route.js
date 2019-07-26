import express from "express";
import controller from "@controllers/test.controller";

const router = express.Router();

router.route("/test").get(controller.test);
router.route("/random_tx").get(controller.createTestAsset);
router.route("/create").post(controller.create);
router.route("/create_test_book").post(controller.createTestBook);
router.route("/transfer_test_book").post(controller.transferTestBook);
router
    .route("/transfer_test_book_fraud")
    .post(controller.transferTestBookFraud);
router.route("/transfer").post(controller.transfer);
router.route("/sign").post(controller.sign);
router.route("/post").post(controller.post);
router.route("/spent").post(controller.getSpent);
router.route("/unspent").post(controller.getUnspent);
router.route("/search").post(controller.searchBook);
router.route("/db").get(controller.dbTest);

export default router;
