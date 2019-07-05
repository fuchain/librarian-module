import express from "express";
import logRoutes from "@routes/log.route";

import authenticationRoutes from "@routes/authentication.route";

const router = express.Router();

router.get("/status", (_, res) => res.send("Server is up!"));

router.use("/logs", logRoutes);
router.use("/authentication", authenticationRoutes);

export default router;
