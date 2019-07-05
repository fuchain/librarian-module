import express from "express";
import logRoutes from "@routes/log.route";

const router = express.Router();

router.get("/status", (_, res) => res.send("Server is up!"));

router.use("/logs", logRoutes);

export default router;
