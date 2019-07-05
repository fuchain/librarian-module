import mongoose from "mongoose";
const Schema = mongoose.Schema;

const reviewSchema = new Schema({
    email: { type: String, index: true },
    review: String,
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now }
});

export default reviewSchema;
