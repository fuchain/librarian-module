import jwt from "jsonwebtoken";

export function createJWT(id) {
    return jwt.sign(
        {
            data: id
        },
        "tudeptrai",
        { expiresIn: "24h" }
    );
}

export function verifyJWT(token) {
    return jwt.verify(token, "tudeptrai");
}
