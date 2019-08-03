import express from "express";
import controller from "@controllers/transfer.controller";

const router = express.Router();

router.route("/create").post(controller.createTransfer);
router.route("/sign").post(controller.sendTxSignedToReceiver);
router.route("/done").post(controller.receiverSigned);
router.route("/test").post(controller.giveTest);

export default router;
