const connectDB = require('../DB/connectiondb');
const eurekaClient = require("../utils/eureka.client.js");
const fetchToEnv = require("../utils/fetchToEnv.js");


const startServer = async (PORT) => {
    try {
      await eurekaClient.start(); 
      await fetchToEnv(); 
      await connectDB();        
      console.log(`App running on port ${PORT}`);
    } catch (err) {
      console.error(`Failed to start the application: ${err.message}`);
      process.exit(1); // Exit the application with a failure code
    }
}

module.exports = startServer;


