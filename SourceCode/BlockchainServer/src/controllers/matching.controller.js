import matchingService from "@services/matching.service";

async function createRequest(req, res) {
    const body = req.body;

    try {
        const sent = matchingService.createMatchingRequest(
            body.email,
            body.book_detail_id,
            body.book_id
        );

        if (sent) {
            res.send({
                message: "Created request!"
            });
        } else {
            res.staus(422).send({
                message: "Error create request!"
            });
        }
    } catch (err) {
        res.status(422).send({
            message: err
        });
    }
}

export default {
    createRequest
};
