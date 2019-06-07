import express from "express";
import logRoutes from "@routes/log.route";
import userRoutes from "@routes/user.route";
import authRoutes from "@routes/auth.route";

const router = express.Router();

router.get("/status", (req, res) => res.send("Server is up!"));

router.use("/logs", logRoutes);
router.use("/users", userRoutes);
router.use("/auth", authRoutes);

export default router;
