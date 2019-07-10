import jwt from "jsonwebtoken";

export function createJWT(email) {
    return jwt.sign(
        {
            email: email,
            roles: "reader"
        },
        "Capstone",
        { expiresIn: "24h" }
    );
}

export function verifyJWT(token) {
    return jwt.verify(token, "Capstone");
}
