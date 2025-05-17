# 🎥 MovieLand - Microservices Architecture

Welcome to **MovieLand**! This project is a microservices-based movie application that offers a seamless experience for users. The architecture leverages modern technologies to provide a robust and scalable platform.

## 🌟 Features

* User management with **Express.js** and **MongoDB**
* Movie data handling with **Spring Boot** and **PostgreSQL**
* **Service Discovery** with **Eureka**
* **API Gateway** with **Spring Cloud Gateway**
* **Configuration Management** with **Config Server**
* **Frontend** built with **Angular**

## 🗃️ Project Structure
movieLand/
├── frontend/
│   └── movieLand-app
├── services/
│   ├── discovery-service
│   ├── movie-service
│   ├── config-service
│   ├── user-service
│   └── gateway-service


## 🚀 Getting Started

### ✅ Requirements

* 🐋 Docker Engine
* 🐘 PostgreSQL
* 🍃 MongoDB
* 📝 Maven
* 🟩 Node.js

### 📂 Cloning the Repository

First, **fork** the repository and then clone it to your local machine:

```bash
git clone https://github.com/yourusername/movieLand.git
cd movieLand
```

## ⚙️ Installation
### 🌐 Frontend

Navigate to the frontend directory and install the dependencies:

```bash
cd frontend/movieLand-app
npm install
```

### 🧩 Spring Boot Services

Navigate to each Spring Boot service and build the project:

```bash
cd services/{service-name}
mvn clean install
```

Services:

* discovery-service
* movie-service
* config-service
* gateway-service

### 📝 User Service (Express.js)

```bash
cd services/user-service
npm install
```

### 🏃 Running the Services
Recommended Order:

🌍 Discovery Service
🔧 Config Service
🚪 Gateway Service
👥 User Service
🎬 Movie Service

### 🌱 Run Each Service
For Spring Boot services:
```bash
mvn spring-boot:run
```
For the User Service:
```bash
npm start
```
For the Angular frontend:
```bash
ng serve
```

## 📫 Contributing

Feel free to open issues and make pull requests. Your contributions are welcome!
You can copy this entire code block and paste it directly into your GitHub repository as README.md. The formatting will render properly with all the headings, code blocks, emojis, and styling when viewed on GitHub.
