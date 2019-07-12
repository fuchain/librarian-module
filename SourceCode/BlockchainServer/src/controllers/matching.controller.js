import errorHandler from "@core/handlers/error.handler";
import matchingService from "@services/matching.service";

function createRequest(req, res) {
    const body = req.body;

    const sent = matchingService.createMatchingRequest(
        body.public_key,
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
}

export default errorHandler({
    createRequest
});
