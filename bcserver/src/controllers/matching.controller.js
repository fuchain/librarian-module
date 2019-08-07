import errorHandler from "@core/handlers/error.handler";
import matchingLogic from "@logics/matching.logic";

async function createRequest(req, res) {
    const body = req.body;

    const success = await matchingLogic.createMatchingRequest(
        req.email,
        body.book_detail_id,
        body.book_id
    );

    success
        ? res.send({
              message: "Created request!"
          })
        : res.staus(422).send({
              message: "Error create request!"
          });
}

async function cancelRequest(req, res) {
    const body = req.body;

    const cancelled = matchingLogic.cancelMatchingRequest(
        req.email,
        body.book_detail_id,
        body.book_id
    );

    cancelled
        ? res.send({
              message: "Cancelled request!"
          })
        : res.staus(422).send({
              message: "Error cancelling request!"
          });
}

async function getMatchings(_, res) {
    const matchings = await matchingLogic.getMatchings();
    res.send(matchings);
}

export default errorHandler({
    createRequest,
    cancelRequest,
    getMatchings
});
