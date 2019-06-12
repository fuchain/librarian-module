import models from "@models";

const list = async (req, res, next) => {
    const listTrackings = await models.review.findAll();
    const jsonList = listTrackings.map(e => {
        return {
            email: e.email,
            review: JSON.parse(e.review),
            updatedAt: e.updatedAt
        };
    });
    res.send(jsonList);
};

const create = async (req, res) => {
    if (!req.email) {
        res.status(401);
        res.send(null);
        return;
    }

    const isExisted = await models.review.findOne({
        where: { email: req.email }
    });

    if (isExisted) {
        res.status(422);
        res.send({
            message: "That user is already have a submited review"
        });
        return;
    }

    const body = req.body;

    try {
        const newReview = await models.review.create({
            email: req.email,
            review: JSON.stringify(body)
        });
        res.send(newReview);
    } catch (err) {
        res.status(400);
        res.send(err);
    }
};

export default { list, create };
