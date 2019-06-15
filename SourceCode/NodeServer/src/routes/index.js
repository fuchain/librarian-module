import express from "express";
import logRoutes from "@routes/log.route";
import userRoutes from "@routes/user.route";
import authRoutes from "@routes/auth.route";
import reviewRoutes from "@routes/review.route";
import notificationRoutes from "@routes/notification.route";

const router = express.Router();

router.get("/status", (req, res) => res.send("Server is up!"));

router.use("/logs", logRoutes);
router.use("/users", userRoutes);
router.use("/auth", authRoutes);
router.use("/reviews", reviewRoutes);
router.use("/notifications", notificationRoutes);

export default router;
