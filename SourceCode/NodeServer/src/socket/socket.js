import { verifyJWT } from "@utils/jwt";
import { getRedisItem, setRedisItem, deleteRedisItem } from "@utils/redis";
import redisAdapter from "socket.io-redis";

export let io;

function initSocketModule(server) {
    io = require("socket.io")(server);
    io.adapter(
        redisAdapter({ host: process.env.REDIS_HOST || "redis", port: 6379 })
    );
    io.origins("*:*");

    io.use(async function(socket, next) {
        if (socket.handshake.query && socket.handshake.query.token) {
            try {
                const payload = verifyJWT(socket.handshake.query.token);
                socket.payload = payload;
                const email = payload.sub;

                const loggedInSocketID = await getRedisItem(email);

                io.to(socket.id).emit("info", {
                    previousConnect: loggedInSocketID || "null"
                });

                if (loggedInSocketID) {
                    io.to(loggedInSocketID).emit("logout", {
                        message: "Another user logged in",
                        type: "logout",
                        id: null
                    });

                    await deleteRedisItem(email);
                    io.sockets.connected[loggedInSocketID].disconnect();
                }

                await setRedisItem(email, socket.id);

                next();
            } catch (err) {
                next(new Error("Authentication error"));
            }
        } else {
            next(new Error("Authentication error"));
        }
    });

    io.on("connection", socket => {
        socket.on("disconnect", async () => {
            const email = socket.payload.sub;

            try {
                await deleteRedisItem(email);
            } catch (err) {
                console.log(err);
            }
        });
    });
}

export default initSocketModule;
