import { getOnlineUsers, emitToUser } from "../socket/socket";

import { Notification } from "@models";
import { sendEmail } from "@core/sendgrid";

const pushNotification = async (req, res) => {
    const { email, message, type, noemail, nosave } = req.body;

    try {
        const newNotification = new Notification({
            email,
            message,
            type
        });

        if (!nosave) {
            // Save to database
            await newNotification.save();
        }

        sendEmail({
            to: email,
            text: message,
            html: message
        });

        const online = await emitToUser(email, "notification", {
            message,
            type,
            id: newNotification._id
        });

        if (!online) {
            res.status(200);
            res.send({
                message:
                    "Message sent, but that user is not connecting to our service",
                save: nosave ? "Not saved to DB" : "Saved"
            });

            return;
        }

        res.status(201);
        res.send({
            message: `Sent to ${online.length} client(s) -> ${online}`,
            sessions: online.length,
            save: nosave ? "Not saved to DB" : "Saved"
        });
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

const getOnline = async (_, res) => {
    try {
        const onlineSession = await getOnlineUsers();
        res.send(onlineSession);
    } catch (err) {
        res.status(400);
        res.send({
            message: err.toString()
        });
    }
};

export default {
    pushNotification,
    getNotification,
    touchNotification,
    getOnline
};
