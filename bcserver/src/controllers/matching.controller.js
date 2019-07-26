import errorHandler from "@core/handlers/error.handler";
import matchingService from "@services/matching.service";

async function createRequest(req, res) {
    const body = req.body;

    const success = await matchingService.createMatchingRequest(
        body.public_key,
        body.book_detail_id,
        body.book_id
    );

    if (success) {
        res.send({
            message: "Created request!"
        });
    } else {
        res.staus(422).send({
            message: "Error create request!"
        });
    }
}

async function cancelRequest(req, res) {
    const body = req.body;

    const cancelled = matchingService.cancelMatchingRequest(
        body.public_key,
        body.book_detail_id,
        body.book_id
    );

    if (cancelled) {
        res.send({
            message: "Cancelled request!"
        });
    } else {
        res.staus(422).send({
            message: "Error cancelling request!"
        });
    }
}

async function getMatchings(_, res) {
    const matchings = await matchingService.getMatchings();
    res.send(matchings);
}

export default errorHandler({
    createRequest,
    cancelRequest,
    getMatchings
});
