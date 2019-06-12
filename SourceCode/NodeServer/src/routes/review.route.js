import express from "express";
import controller from "@controllers/review.controller";
import { jwtAuthentication } from "@middlewares/jwt.middleware";

const router = express.Router();

router.route("/").get(controller.list);
router.route("/").post(jwtAuthentication, controller.create);

export default router;
