import { verifyJWT } from "@utils/jwt";

export let io;
export const socketPool = {};

function initSocketModule(server) {
    io = require("socket.io")(server);
    io.origins("*:*");

    io.use(function(socket, next) {
        if (socket.handshake.query && socket.handshake.query.token) {
            try {
                const payload = verifyJWT(socket.handshake.query.token);
                socket.payload = payload;

                const email = payload.sub;
                socketPool[email] = socket.id;

                next();
            } catch (err) {
                next(new Error("Authentication error"));
            }
        } else {
            next(new Error("Authentication error"));
        }
    });
}

export default initSocketModule;
