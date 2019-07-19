import express from "express";
import controller from "@controllers/event.controller";

const router = express.Router();

router.route("/push").post(controller.pushEvent);

export default router;
