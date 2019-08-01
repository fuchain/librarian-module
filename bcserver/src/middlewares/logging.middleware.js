import * as sentry from "@sentry/node";
import morgan from "morgan";

export function sentryMiddleware() {
    sentry.init({
        dsn: "https://f7058307a8514bb8b0f3b46b25e33596@sentry.io/1509628"
    });
}

export const morganMiddleware = morgan("tiny");
