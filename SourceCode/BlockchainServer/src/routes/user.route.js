import express from "express";
import controller from "@controllers/user.controller";

const router = express.Router();

router.route("/profile").get(controller.getProfile);

export default router;
