import express from "express";
import controller from "@controllers/notification.controller";
import { jwtAuthentication } from "@middlewares/jwt.middleware";

const router = express.Router();

router.route("/").get(jwtAuthentication, controller.getNotification);
router.route("/touch/:id").put(jwtAuthentication, controller.touchNotification);
router.route("/push").post(controller.pushNotification);
router.route("/online").get(controller.getOnline);

export default router;
