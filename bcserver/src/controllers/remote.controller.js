import errorHandler from "@core/handlers/error.handler";
import pairUpdateQueue from "@queues/pair.update.queue";

function addPairUpdate(req, res) {
    const { returner, receiver } = req.body;
    pairUpdateQueue.addJob(returner, receiver);

    res.send({
        message: "Pair update job added"
    });
}

export default errorHandler({
    addPairUpdate
});
