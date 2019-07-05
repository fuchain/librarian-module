import express from "express";
import controller from "@controllers/authentication.controller";

const router = express.Router();

router.route("/random").get(controller.newKeypair);

export default router;
