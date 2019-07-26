import express from "express";
import controller from "@controllers/remote.controller";

const router = express.Router();

router.route("/pair_update").post(controller.addPairUpdate);

export default router;
