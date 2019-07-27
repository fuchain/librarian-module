const Sentry = require("@sentry/node");

export function globalErrorHandler(err, _, res, next) {
    Sentry.captureException(err);
    console.log("Error: ", err instanceof Error ? err.toString() : err);

    if (err) {
        if (
            err.message &&
            err.message === "HTTP Error: Requested page not reachable" // This is invalid transaction, so the bigchaindb replies this message
        ) {
            res.status(400).send({
                message:
                    "Your transaction is declined by the blockchain validators"
            });

            return;
        }

        res.status(400).send({
            message: err instanceof Error ? err.toString() : err,
            stack: err.stack && typeof err.stack === "string" && err.stack
        });

        return;
    } else {
        next();
    }
}
