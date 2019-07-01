import express from "express";
import controller from "@controllers/notification.controller";

const router = express.Router();

router.route("/push").post(controller.pushNotification);

export default router;
