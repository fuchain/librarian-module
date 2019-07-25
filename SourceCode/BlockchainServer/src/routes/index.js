import os from "os";
import express from "express";
import testRoutes from "@routes/test.route";

import jwtMiddleware from "@middlewares/jwt.middleware";

import authenticationRoutes from "@routes/authentication.route";
import userRoutes from "@routes/user.route";
import librarianRoutes from "@routes/librarian.route";
import transferRoutes from "@routes/transfer.route";
import matchingRoutes from "@routes/matching.route";
import bookRoutes from "@routes/book.route";
import fetchRoutes from "@routes/fetch.route";
import remoteRoutes from "@routes/remote.route";

const router = express.Router();

router.get("/", (_, res) => {
    res.send({
        hostname: os.hostname()
    });
});

router.get("/status", (_, res) =>
    res.send({
        message: "Server is up!",
        uptime: `${os.uptime()} seconds`
    })
);

router.use("/test", testRoutes);
router.use("/auth", authenticationRoutes);
router.use("/user", jwtMiddleware, userRoutes);
router.use("/librarian", librarianRoutes);
router.use("/transfer", transferRoutes);
router.use("/matching", matchingRoutes);
router.use("/book", bookRoutes);
router.use("/fetch", fetchRoutes);
router.use("/remote", remoteRoutes);

export default router;
