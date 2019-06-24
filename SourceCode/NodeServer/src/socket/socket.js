import { verifyJWT } from "@utils/jwt";
import { setRedisItem, deleteRedisItem } from "@utils/redis";
import redisAdapter from "socket.io-redis";

export let io;

function initSocketModule(server) {
    io = require("socket.io")(server);
    io.adapter(
        redisAdapter({ host: process.env.REDIS_HOST || "redis", port: 6379 })
    );
    io.origins("*:*");

    io.use(function(socket, next) {
        if (socket.handshake.query && socket.handshake.query.token) {
            try {
                const payload = verifyJWT(socket.handshake.query.token);
                socket.payload = payload;

                const email = payload.sub;
                setRedisItem(email, socket.id);

                next();
            } catch (err) {
                next(new Error("Authentication error"));
            }
        } else {
            next(new Error("Authentication error"));
        }
    });

    io.on("connection", socket => {
        socket.on("disconnect", () => {
            const email = socket.payload.sub;

            deleteRedisItem(email);
        });
    });
}

export default initSocketModule;
