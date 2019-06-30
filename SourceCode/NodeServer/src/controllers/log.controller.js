import models from "@models";
import axios from "axios";

const list = async (req, res, next) => {
    const listTrackings = await models.log.findAll();
    res.send(listTrackings);
};

const create = async (req, res) => {
    const newItem = req.body;

    try {
        const newTracking = await models.log.create(newItem);

        const trackingStr = JSON.stringify(newTracking);
        axios.post(
            "https://hooks.slack.com/services/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",
            {
                text: trackingStr
            }
        );

        res.send(newTracking);
    } catch (err) {
        res.status(400);
        res.send(err);
    }
};

const read = async (req, res) => {
    const id = req.params.id;

    try {
        const tracking = await models.log.findOne({
            where: {
                id
            }
        });
        if (tracking) {
            res.status(201);
            res.send(tracking);
        } else {
            res.status(404);
            res.send();
        }
    } catch (err) {
        res.status(400);
        res.send(err);
    }
};

const update = async (req, res) => {
    const id = req.params.id;

    const updatingTracking = req.body;

    try {
        await models.log.update(updatingTracking, {
            where: { id }
        });
        res.send();
    } catch (err) {
        res.status(422);
        res.send(err);
    }
};

const destroy = async (req, res) => {
    const id = req.params.id;

    try {
        await models.log.destroy({
            where: {
                id
            }
        });
        res.status(204);
        res.send();
    } catch (err) {
        res.status(422);
        res.send(err);
    }
};

export default { list, create, read, update, destroy };
