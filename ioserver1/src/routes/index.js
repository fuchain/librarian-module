import express from "express";
import logRoutes from "@routes/log.route";
import reviewRoutes from "@routes/review.route";
import notificationRoutes from "@routes/notification.route";
import eventRoutes from "@routes/event.route";

const router = express.Router();

router.get("/status", (_, res) => res.send("Server is up!"));

router.use("/logs", logRoutes);
router.use("/reviews", reviewRoutes);
router.use("/notifications", notificationRoutes);
router.use("/events", eventRoutes);

export default router;
