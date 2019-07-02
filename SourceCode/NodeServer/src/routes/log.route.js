import express from "express";
import controller from "@controllers/log.controller";

const router = express.Router();

router.route("/").post(controller.create);

export default router;
