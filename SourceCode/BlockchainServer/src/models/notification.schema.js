import mongoose from "mongoose";
const Schema = mongoose.Schema;

const notificationSchema = new Schema({
    email: { type: String, index: true },
    message: String,
    type: String,
    isRead: { type: Boolean, default: false },
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now }
});

export default notificationSchema;
