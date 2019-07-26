import axios from "axios";

const create = async (req, res) => {
    const newTracking = req.body;

    try {
        const trackingStr = JSON.stringify(newTracking.metadata);
        axios.post(
            "https://hooks.slack.com/services/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",
            {
                text:
                    "We have new " +
                    newTracking.type +
                    " report for service " +
                    (newTracking.source || "unknown") +
                    " :tired_face:. \n\n ```" +
                    trackingStr +
                    "```"
            }
        );

        res.send();
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
};

export default { create };
