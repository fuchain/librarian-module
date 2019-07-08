import express from "express";
import controller from "@controllers/user.controller";
import errorHandler from "@core/handlers/error.handler";

const router = express.Router();

router.route("/profile").post(errorHandler(controller.getProfile));
router.route("/current_book").post(errorHandler(controller.getCurrentBook));
router.route("/returning").post(errorHandler(controller.getReturningBook));
router.route("/requesting").post(errorHandler(controller.getRequestingBook));
router.route("/keeping_amount").post(errorHandler(controller.getKeepingAmount));
router
    .route("/transfer_history")
    .post(errorHandler(controller.getTransferHistory));

export default router;
