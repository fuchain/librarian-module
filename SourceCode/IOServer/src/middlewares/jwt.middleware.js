import { verifyJWT } from "@core/jwt";

export function jwtAuthentication(req, res, next) {
    const token = getToken(req);

    try {
        const payload = verifyJWT(token);
        const { sub } = payload;
        req.email = sub;
        next();
    } catch (err) {
        res.status(401);
        res.send({ message: "Unauthorized" });
    }
}

function getToken(req) {
    // From header or query string
    if (
        req.headers.authorization &&
        req.headers.authorization.split(" ")[0] === "Bearer"
    ) {
        return req.headers.authorization.split(" ")[1];
    } else if (req.query && req.query.token) {
        return req.query.token;
    }

    return null;
}
