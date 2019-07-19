import { io } from "../socket/socket";

import { getRedisItem } from "@core/redis";

const pushEvent = async (req, res) => {
    const { email, message, type } = req.body;

    try {
        const socketId = await getRedisItem(email);

        if (!socketId) {
            res.status(400);
            res.send({
                message: "User is not connecting to our service"
            });

            return;
        }

        io.to(socketId).emit("event", {
            message,
            type
        });

        res.status(201);
        res.send({
            message: `Sent to ${socketId}`
        });
        return;
    } catch (err) {
        res.status(400);
        res.send({
            message: err.toString()
        });
    }
};

export default {
    pushEvent
};
