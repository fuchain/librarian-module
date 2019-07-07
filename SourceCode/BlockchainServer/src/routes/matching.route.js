import express from "express";
import controller from "@controllers/matching.controller";

const router = express.Router();

router.route("/create_request").post(controller.createRequest);

export default router;
