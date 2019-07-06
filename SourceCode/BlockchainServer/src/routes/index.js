import express from "express";
import testRoutes from "@routes/test.route";

import authenticationRoutes from "@routes/authentication.route";

const router = express.Router();

router.get("/status", (_, res) => res.send("Server is up!"));

router.use("/test", testRoutes);
router.use("/authentication", authenticationRoutes);

export default router;
