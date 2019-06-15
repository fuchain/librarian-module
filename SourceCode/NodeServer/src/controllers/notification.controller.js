import { socketPool, io } from "../socket/socket";

const testPushNotification = async (req, res, next) => {
    const { email, message } = req.body;

    try {
        io.to(`${socketPool[email]}`).emit("notification", message);
    } catch (err) {
        res.status(400);
        res.send({
            error: err
        });
    }

    res.status(201);
    res.send({ message: `Sent to ${socketPool[email]}` });
};

export default { testPushNotification };
