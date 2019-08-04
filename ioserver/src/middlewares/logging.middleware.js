import * as sentry from "@sentry/node";
import morgan from "morgan";

export function sentryMiddleware() {
    sentry.init({
        dsn: "https://dda82049bb8a4f8fbdb8b627440ef7fc@sentry.io/1521088"
    });
}

export const morganMiddleware = morgan("tiny");
