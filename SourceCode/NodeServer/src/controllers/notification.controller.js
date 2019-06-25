import { io } from "../socket/socket";

import { getRedisItem } from "@utils/redis";

const testPushNotification = async (req, res, next) => {
    const { email, message } = req.body;

    try {
        const socketId = await getRedisItem(email);

        if (!socketId) {
            res.status(422);
            res.send({
                error: "That user is not connecting to our service"
            });

            return;
        }

        io.to(socketId).emit("notification", message);

        res.status(201);
        res.send({ message: `Sent to ${socketId}` });
        return;
    } catch (err) {
        res.status(400);
        res.send({
            error: err.toString()
        });
    }
};

export default { testPushNotification };
