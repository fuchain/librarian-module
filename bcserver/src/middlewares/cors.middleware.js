import cors from "cors";

const corsOptions = {
    origin: "*",
    optionsSuccessStatus: 200
};

const corsMiddleware = cors(corsOptions);

export default corsMiddleware;
