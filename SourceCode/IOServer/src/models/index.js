import mongoose from "mongoose";
import env from "@core/env";

import reviewSchema from "@models/review.schema";
import notificationSchema from "@models/notification.schema";

const conn = mongoose.createConnection(`mongodb://${env.dbHost}/${env.dbName}`);

function init() {
    conn.on("error", console.error.bind(console, "MongoDB connection error:"));
    conn.once("open", function() {
        console.log("Connected to MongoDB!");
    });
}

export const Review = conn.model("Review", reviewSchema);
export const Notification = conn.model("Notification", notificationSchema);

export default init;
