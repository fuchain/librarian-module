import express from "express";
import controller from "@controllers/matching.controller";

const router = express.Router();

router.route("/create_request").post(controller.createRequest);
router.route("/cancel_request").post(controller.cancelRequest);

export default router;
