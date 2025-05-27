const mongoose = require("mongoose");

const ConnectToDb = async () => {
  try {
    await mongoose.connect(process.env.MONGODB_URL);
    console.log("Connection Successful");
  } catch (error) {
    console.log("Connection Error", error.message);
    process.exit(1);
  }
};

module.exports = ConnectToDb;
