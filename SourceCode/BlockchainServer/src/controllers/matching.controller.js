import errorHandler from "@core/handlers/error.handler";
import matchingService from "@services/matching.service";

async function createRequest(req, res) {
    const body = req.body;

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
}

export default errorHandler({
    createRequest
});
