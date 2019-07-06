import express from "express";
import controller from "@controllers/user.controller";

const router = express.Router();

router.route("/profile").post(controller.getProfile);
router.route("/current_book").post(controller.getCurrentBook);
router.route("/keeping_amount").post(controller.getKeepingAmount);
router.route("/transfer_history").post(controller.getTransferHistory);

export default router;
