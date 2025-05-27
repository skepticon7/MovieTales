const mongoose = require("mongoose");

const UserSchema = new mongoose.Schema({
    username: {
        type: String,
        required: [true, "Username is required"],
      },
      email: {
        type: String,
        required: [true, "Email is required"],
        unique: true,
      },
      password: {
        type: String,
        required: [true, "Password is required"],
      },
      avatarUrl: {
        type: String,
      }
}, { timestamps: true });


const User = mongoose.model('User', UserSchema);
module.exports = User;
