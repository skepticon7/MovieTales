const Eureka = require("eureka-js-client").Eureka;

const client = new Eureka({
    instance:{
        app : "user-service",
        hostName : "localhost",
        ipAddr : "127.0.0.1",
        port : {
            '$' : process.env.SERVER_PORT || 3000,
            '@enabled' : true
        },
        vipAddress : "user-service",
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
        statusPageUrl: `http://localhost:${process.env.SERVER_PORT}/actuator/info`,
        healthCheckUrl: `http://localhost:${process.env.SERVER_PORT}/actuator/health`, 
        homePageUrl: `http://localhost:${process.env.SERVER_PORT}`, 
      },
      eureka: {
        host: 'localhost', 
        port: 8761, 
        servicePath: '/eureka/apps/', 
      }
});



module.exports = client;