import { io } from "../socket/socket";

import { getRedisitem } from "@utils/redis";

const testPushNotification = async (req, res, next) => {
    const { email, message } = req.body;

    try {
        const socketId = await getRedisitem(email);
        io.to(socketId).emit("notification", message);

        res.status(201);
        res.send({ message: `Sent to ${socketId}` });
    } catch (err) {
        res.status(400);
        res.send({
            error: err.toString()
        });
    }
};

export default { testPushNotification };
