import { verifyJWT } from "@core/jwt";
import { getRedisItem, setRedisItem } from "@core/redis";

import { io } from "../server";

export async function getOnlineUsers() {
    return new Promise((resolve, reject) => {
        io.of("/").adapter.clients((err, clients) => {
            if (err) reject(err);

            resolve({ array: clients || [], session: clients.length || 0 });
        });
    });
}

function extractPool(listSockets) {
    if (typeof listSockets !== "string") {
        return [];
    }
    listSockets = JSON.parse(listSockets);

    if (Array.isArray(listSockets)) {
        return listSockets;
    }

    return [];
}

function zipPool(listSockets) {
    return JSON.stringify(listSockets);
}

async function userNewConnect(email, socketId) {
    try {
        const listSockets = await getRedisItem(email);
        const arr = extractPool(listSockets);
        arr.push(socketId);
        await setRedisItem(email, zipPool(arr));
    } catch (err) {
        console.log(err);
    }
}

async function userDisconnect(email, socketId) {
    const listSockets = await getRedisItem(email);
    const arr = extractPool(listSockets);
    const index = arr.indexOf(socketId);
    index > -1 && arr.splice(index, 1);
    await setRedisItem(email, zipPool(arr));
}

export async function emitToUser(email, type, metadata) {
    const listSockets = await getRedisItem(email);
    const arr = extractPool(listSockets);

    if (!arr || !arr.length) {
        return false;
    }

    const { array } = await getOnlineUsers();

    const filtered = arr.filter(socketId => {
        return array.indexOf(socketId) > -1 ? true : false;
    });

    filtered.forEach(socketId => {
        io.to(socketId).emit(type, metadata);
    });

    return filtered.length ? filtered : false;
}

function initSocketModule(server) {
    io.use(async function(socket, next) {
        if (socket.handshake.query && socket.handshake.query.token) {
            try {
                const payload = verifyJWT(socket.handshake.query.token);
                socket.payload = payload;
                const email = payload.email;

                await userNewConnect(email, socket.id);

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
            const email = socket.payload.email;

            try {
                await userDisconnect(email, socket.id);
            } catch (err) {
                console.log(err);
            }
        });
    });
}

export default initSocketModule;
