import { emitToUser } from "../socket/socket";

const pushEvent = async (req, res) => {
    const { email, message, type } = req.body;

    try {
        const online = await emitToUser(email, "event", {
            message,
            type
        });

        if (!online) {
            res.status(400);
            res.send({
                message: "User is not connecting to our service"
            });

            return;
        }

        res.status(201);
        res.send({
            message: `Sent to ${online.length} client(s) -> ${online}`,
            sessions: online.length
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
