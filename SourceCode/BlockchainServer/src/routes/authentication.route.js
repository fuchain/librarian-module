import express from "express";
import controller from "@controllers/authentication.controller";

const router = express.Router();

router.route("/random").get(controller.newKeypair);
router.route("/email").post(controller.newKeyPairEmail);

export default router;
