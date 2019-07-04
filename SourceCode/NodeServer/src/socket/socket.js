import { verifyJWT } from "@utils/jwt";
import { getRedisItem, setRedisItem, deleteRedisItem } from "@utils/redis";
import redisAdapter from "socket.io-redis";

export let io;

async function addUser(email) {
    try {
        const users = await getRedisItem("users");
        if (users) {
            const arr = JSON.parse(users);
            if (!arr.find(e => e === email)) {
                arr.push(email);

                const arrStr = JSON.stringify(arr);
                await setRedisItem("users", arrStr);
            }
        }
    } catch (err) {
        throw err;
    }
}

async function removeUser(email) {
    try {
        const users = await getRedisItem("users");
        if (users) {
            const arr = JSON.parse(users);
            if (!arr.find(e => e === email)) {
                const filteredArr = arr.filter(e => e !== email);

                const arrStr = JSON.stringify(filteredArr);
                await setRedisItem("users", arrStr);
            }
        }
    } catch (err) {
        throw err;
    }
}

export async function getOnlineUsers() {
    try {
        const users = await getRedisItem("users");
        if (users) {
            const usersArr = JSON.parse(users);

            return usersArr;
        } else {
            return [];
        }
    } catch (err) {
        throw err;
    }
}

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
                    await removeUser(email);
                }

                await setRedisItem(email, socket.id);
                await addUser(email);

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
                await removeUser(email);
            } catch (err) {
                console.log(err);
            }
        });
    });
}

export default initSocketModule;
