import { verifyJWT } from "@core/jwt";

export default async function authenticationMiddleware(req, res, next) {
    try {
        const publicKey = req.body.public_key;
        const token = getToken(req);

        const payload = verifyJWT(token);
        const { email, roles } = payload;

        req.email = email;
        req.roles = roles || "reader";
        req.publicKey = publicKey;
        next();
    } catch (err) {
        res.status(401);
        res.send({ message: "Token invalid" });
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
