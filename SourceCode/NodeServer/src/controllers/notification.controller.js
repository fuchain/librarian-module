import { io } from "../socket/socket";

import { getRedisItem } from "@utils/redis";

const pushNotification = async (req, res) => {
    const { email, message, type } = req.body;

    try {
        const socketId = await getRedisItem(email);

        // Save to database

        if (!socketId) {
            res.status(200);
            res.send({
                message:
                    "Message sent, but that user is not connecting to our service"
            });

            return;
        }

        io.to(socketId).emit("notification", { message, type });

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

export default { pushNotification };
