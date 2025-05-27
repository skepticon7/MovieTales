const User = require("../models/user.model.js");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const { default: mongoose } = require("mongoose");

//helper for server error
const handleError = (res, error, statusCode = 500) => {
  console.error(error.message || error);
  res.status(statusCode).json({ error: error.message || "Server error" });
};

//register user controller
const registerUser = async (req, res) => {
  const { username , email , password , avatarUrl } = req.body;

  if (!username || !email || !password || !avatarUrl) {
    return res.status(400).json({ error: "All fields are required" });
  }


  try {
    const existingUsername = await User.findOne({ username });
    const existingEmail = await User.findOne({email});
    if (existingUsername) 
      return res.status(400).json({ error: "User already exists with that username" });
    
    if(existingEmail) 
      return res.status(400).json({error : "User already exists with that email"});


    const hashedPassword = await bcrypt.hash(password,10)

    const newUser = new User({
      username,
      email,
      password : hashedPassword.toString(),
      avatarUrl
    });

    await newUser.save();

    res.status(201).json({ message: "User created successfully", user: newUser });
  } catch (error) {
    handleError(res , error);
  }
};

// Login user
const loginUser = async (req, res) => {
  const { username, password } = req.body;

  try {
    const existingUser = await User.findOne({ username });
    if (!existingUser) {
      return res.status(400).json({ error: "Invalid username" });
    }
    
    const isPasswordValid = await bcrypt.compare(password , existingUser.password);
    
    if(!isPasswordValid)
      return res.status(400).json({error : "Invalid password"})

    const token = jwt.sign(
      {id : existingUser.id , user : existingUser.username},
      process.env.JWT_SECRET,
      {expiresIn : "1h" , algorithm: "HS256" , subject : existingUser.username}
    );
    const {password : _ , ...user} = existingUser.toObject();
    return res.status(200).json({ message: "Login successful", token , user });
  } catch (error) {
    handleError(res , error);
  }
};



// Get a single user by ID
const getUserSettingsById = async (req, res) => {
  try {
    const userId = req.query.userId;
    const user = await User.findById(userId);
    if (!user) return res.status(404).json({ message: "User not found" });
    user._id.toString();
    const {password : _ , ...userData} = user.toObject();
    return res.status(200).json({UserDTO : userData});
  } catch (error) {
    handleError(res, error);
  }
};

// Update a user by ID
const updateUserSettings = async (req, res) => {
  try {
    const userId = req.query.userId;
    const user = await User.findByIdAndUpdate(userId, req.body, { new: true }).select("-password");
    if (!user) return res.status(404).json({ message: "User not found" });
    return res.status(200).json({message : "User updated successfully" , user})
  } catch (error) {
    handleError(res , error);
  }
};

const updateUserPassowrd = async (req, res) => {
  try {
    const userId = req.query.userId;
    const { newPassword, oldPassword } = req.body;

    if (!newPassword || !oldPassword) {
      return res.status(400).json({ error: "Both old and new passwords are required" });
    }

    const user = await User.findById(userId).select("+password");
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    const isOldPasswordValid = await bcrypt.compare(oldPassword, user.password);
    if (!isOldPasswordValid) {
      return res.status(400).json({ error: "Wrong old password" });
    }

    const newHashedPassword = await bcrypt.hash(newPassword, 10);
    user.password = newHashedPassword;
    await user.save();

    return res.status(200).json({ message: "Password successfully updated" });
  } catch (error) {
    handleError(res, error);
  }
};

// Delete a user by ID
const axios = require("axios");
// ...existing code...

const deleteUser = async (req, res) => {
  const session = await mongoose.startSession();
  session.startTransaction();
  try {
    const user = await User.findById(req.query.userId);
    if (!user) {
      await session.abortTransaction();
      session.endSession();
      return res.status(404).json({ message: "User not found" });
    }
    const { password } = req.body;
    const comparePassword = await bcrypt.compare(password, user.password);

    if (!comparePassword) {
      await session.abortTransaction();
      session.endSession();
      return res.status(400).json({ error: "Wrong credentials" });
    }

    let deletedStuff;
    let authHeader = req.headers['authorization'];
    try {
      deletedStuff = await axios.delete(`${process.env.MOVIE_SERVICE_URL}/deleteUserStuff?userId=${req.query.userId}`, {
        headers : {
          Authorization : authHeader
        }
      });
    } catch (movieServiceError) {
      console.log(movieServiceError);
      await session.abortTransaction();
      session.endSession();
      return res.status(500).json({ error: "Failed to delete user movie trackers and reviews" });
    }

    let deletedUser = await user.deleteOne();
    let deletedUserData = {
      user : deletedUser , 
      movieTrackers : deletedStuff.movieTrackers,
      reviews : deletedStuff.reviews
    }
    await session.commitTransaction();
    session.endSession();
    return res.status(200).json({ message: "User deleted" , userData : deletedUserData });
  } catch (error) {
    await session.abortTransaction();
    session.endSession();
    handleError(res, error);
  }
};

// Export all controllers
module.exports = {
  registerUser,
  loginUser,
  getUserSettingsById,
  updateUserSettings,
  updateUserPassowrd,
  deleteUser,
};
