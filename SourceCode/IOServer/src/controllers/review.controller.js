import { Review } from "@models";

const list = async (req, res, next) => {
    const result = await Review.find();

    const reviews = result.map(e => {
        return {
            email: e.email,
            review: JSON.parse(e.review),
            updatedAt: e.updatedAt
        };
    });

    res.send(reviews);
};

const create = async (req, res) => {
    if (!req.email) {
        res.status(401);
        res.send(null);
        return;
    }

    const isExisted = await Review.findOne({
        email: req.email
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
        const newReview = new Review({
            email: req.email,
            review: JSON.stringify(body)
        });

        await newReview.save();

        res.send(newReview);
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

export default { list, create };
