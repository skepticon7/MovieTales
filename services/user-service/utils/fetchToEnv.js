const fetchConfig = require("../utils/config");
const fs = require("fs");
require("dotenv").config();
const fetchToEnv = async () => {
    try{
        const config = await fetchConfig();
        const propertySources = config.propertySources[0];
        let envContent = "";
        for(const [key , value] of Object.entries(propertySources.source)){
            envContent += `${key.toUpperCase().replace(/\./g , "_")}=${value}\n`;
        }
        fs.writeFileSync('.env' , envContent);
        console.log("env file updated with config");
    }catch(err){
        console.error("failted to fetch and write config in the env")
        process.exit(1);
    }
  
}
module.exports = fetchToEnv;