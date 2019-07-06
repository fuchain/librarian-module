import express from "express";
import controller from "@controllers/test.controller";

const router = express.Router();

router.route("/create").post(controller.create);
router.route("/transfer").post(controller.transfer);
router.route("/sign").post(controller.sign);
router.route("/post").post(controller.post);
router.route("/spent").post(controller.getSpent);
router.route("/unspent").post(controller.getUnspent);

export default router;
