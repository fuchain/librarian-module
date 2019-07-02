import { io } from "../socket/socket";

import { getRedisItem } from "@utils/redis";
import { Notification } from "@models";

const pushNotification = async (req, res) => {
    const { email, message, type } = req.body;

    try {
        const socketId = await getRedisItem(email);

        // Save to database
        const newNotification = new Notification({
            email,
            message,
            type
        });
        await newNotification.save();

        if (!socketId) {
            res.status(200);
            res.send({
                message:
                    "Message sent, but that user is not connecting to our service"
            });

            return;
        }

        io.to(socketId).emit("notification", {
            message,
            type,
            id: newNotification._id
        });

        res.status(201);
        res.send({ message: `Sent to ${socketId}` });
        return;
    } catch (err) {
        res.status(400);
        res.send({
            message: err.toString()
        });
    }
};

const getNotification = async (req, res) => {
    const notifications = await Notification.find({
        email: req.email
    });

    res.send(notifications);
};

const touchNotification = async (req, res) => {
    if (!req.params.id) {
        res.status(400);
        res.send({
            message: "Not a valid id!"
        });

        return;
    }

    try {
        const touched = await Notification.findOneAndUpdate(
            {
                _id: req.params.id,
                email: req.email
            },
            {
                isRead: true
            }
        );

        if (touched) {
            res.send({
                message: "Touched!"
            });
        } else {
            res.status(400);
            res.send({
                message: "Unable to touch!"
            });
        }
    } catch (err) {
        res.status(400);
        res.send({
            message: err.toString()
        });
    }
};

export default { pushNotification, getNotification, touchNotification };
