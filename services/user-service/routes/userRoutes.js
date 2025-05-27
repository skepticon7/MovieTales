const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/authMiddleware.js");
const {registerUser , loginUser , getUserSettingsById , updateUserPassowrd ,updateUserSettings ,  deleteUser} = require('../Controllers/userController.js');


router.post("/register", registerUser);
router.post("/login", loginUser);
router.get("/getUserSettings", authMiddleware ,getUserSettingsById);
router.patch("/updateUserPassword" , authMiddleware , updateUserPassowrd);
router.put("/updateUserSettings", authMiddleware , updateUserSettings);
router.delete("/deleteUserAccount", authMiddleware ,deleteUser);

module.exports = router;

