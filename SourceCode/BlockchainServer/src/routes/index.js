import express from "express";
import testRoutes from "@routes/test.route";

import authenticationRoutes from "@routes/authentication.route";
import userRoutes from "@routes/user.route";
import transferRoutes from "@routes/transfer.route";
import matchingRoutes from "@routes/matching.route";

const router = express.Router();

router.get("/status", (_, res) =>
    res.send({
        message: "Server is up!"
    })
);

router.use("/test", testRoutes);
router.use("/auth", authenticationRoutes);
router.use("/user", userRoutes);
router.use("/transfer", transferRoutes);
router.use("/matching", matchingRoutes);

export default router;
