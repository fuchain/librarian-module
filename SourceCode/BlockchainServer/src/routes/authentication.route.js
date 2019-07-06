import express from "express";
import controller from "@controllers/authentication.controller";

const router = express.Router();

router.route("/keypair/random").get(controller.randomKeypair);
router.route("/keypair/email").post(controller.verifyKeyPairEmail);
router.route("/google/token").post(controller.googleAuthentication);

export default router;
