import express from "express";
import controller from "@controllers/authentication.controller";

const router = express.Router();

router.route("/keypair/random").get(controller.randomKeypair);
router.route("/keypair/email").post(controller.verifyKeyPairEmail);

export default router;
