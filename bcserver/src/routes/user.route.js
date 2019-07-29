import express from "express";
import controller from "@controllers/user.controller";

const router = express.Router();

router.route("/profile").post(controller.getProfile);
router.route("/update_profile").post(controller.updateProfile);
router.route("/current_book").post(controller.getCurrentBook);
router.route("/returning").post(controller.getReturningBook);
router.route("/requesting").post(controller.getRequestingBook);
router.route("/keeping_amount").post(controller.getKeepingAmount);
router.route("/transfer_history").post(controller.getTransferHistory);
router.route("/lasttx_time").post(controller.getLastTransactionTime);

export default router;
