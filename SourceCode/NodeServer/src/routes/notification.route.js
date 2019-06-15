import express from "express";
import controller from "@controllers/notification.controller";

const router = express.Router();

router.route("/test").post(controller.testPushNotification);

export default router;
