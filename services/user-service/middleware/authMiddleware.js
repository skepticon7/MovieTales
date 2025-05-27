const jwt = require("jsonwebtoken");

const authMiddleware = (req , res , next) => {
    const authHeader = req.headers["authorization"];
    if(!authHeader || !authHeader.startsWith("Bearer "))
        return res.status(401).json({error : "Unauthorized : No Token Provided"})

    const token = authHeader.split(" ")[1];

    try{
        const decoded = jwt.verify(token , process.env.JWT_SECRET)
        req.user = decoded;
        next();
    }catch(error){
        return res.status(403).json({error : "Forbidden : Invalid Token"})
    }

}

module.exports = authMiddleware;