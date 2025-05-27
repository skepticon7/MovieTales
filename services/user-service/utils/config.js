const axios = require("axios");
const fetchConfig = async () => {
    try{
        const response = await axios.get("http://localhost:8888/user-service/dev");
        console.log("hello world");
        return response.data;
    }catch(err){
        console.log(`error fetching config from server ${err}`);
        process.exit(1);
    }
}


module.exports = fetchConfig;