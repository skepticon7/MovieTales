require('dotenv').config();
const express = require("express");
const actuator = require("express-actuator");
const cookieParser = require("cookie-parser");
const startServer = require("./utils/serverStart.js")
const UserRouter = require("./routes/userRoutes.js");




const app = express();
app.use(express.json());


//middleware
app.use(express.json());
app.use(cookieParser());
app.use(actuator())

//routes
app.use("/api/users", UserRouter);



//testing
app.get("/home" , (req,res) => {
  return res.status(200).json({message : "Hello"});
})

const PORT = process.env.SERVER_PORT || 3000;

app.listen(PORT, startServer(PORT));
