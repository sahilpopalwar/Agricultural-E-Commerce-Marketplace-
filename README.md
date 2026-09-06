 # 🌾 Agricultural E-Commerce Marketplace

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-brightgreen?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Template_Engine-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge&logo=h2&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build_Tool-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A full-stack Java web application bridging the gap between farmers and buyers through a modern digital marketplace.**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Architecture](#-architecture) • [Getting Started](#-getting-started) • [Screenshots](#-screenshots) • [Contact](#-contact)

</div>

---

## 📌 Project Overview

The **Agricultural E-Commerce Marketplace** is a backend-driven web application built with **Spring Boot** and **Thymeleaf**, designed to digitize agricultural supply chains. It empowers farmers to list their produce and enables buyers to purchase directly — eliminating middlemen and ensuring fair pricing.

> 💡 **Problem Solved:** Indian farmers often lose 30–40% of earnings to intermediaries. This platform creates a direct, transparent channel between producers and consumers.

---

## ✨ Features

- 🛒 **Product Listing & Management** — Farmers can add, update, and remove produce listings
- 👤 **User Role Management** — Separate flows for Farmers and Buyers
- 🔍 **Browse & Search** — Buyers can explore available products by category
- 🧾 **Order Management** — End-to-end order placement and tracking
- 🗃️ **Persistent Data Layer** — JPA-based ORM with H2 in-memory database
- 📦 **MVC Architecture** — Clean separation of concerns with Controller-Service-Repository layers
- 🖥️ **Server-side Rendering** — Dynamic pages with Thymeleaf templates
- ✅ **Unit Testing** — Integrated Spring Boot Test suite

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| **Language** | Java 21 | Core application logic |
| **Framework** | Spring Boot 3.2.3 | Application backbone & auto-configuration |
| **Web Layer** | Spring MVC | REST controllers & routing |
| **Template Engine** | Thymeleaf | Server-side HTML rendering |
| **ORM** | Spring Data JPA (Hibernate) | Database abstraction layer |
| **Database** | H2 (In-Memory) | Lightweight embedded database |
| **Build Tool** | Apache Maven | Dependency management & build lifecycle |
| **Boilerplate Reduction** | Lombok | Auto-generated getters/setters/constructors |
| **Testing** | Spring Boot Test (JUnit 5) | Unit & integration testing |
| **IDE Support** | VS Code + IntelliJ IDEA | Development environment configs included |

---

## 🏗️ Architecture

```
Agricultural-E-Commerce-Marketplace/
│
├── src/
│   ├── main/
│   │   ├── java/org/project/
│   │   │   ├── controller/      # Spring MVC Controllers (HTTP Request Handlers)
│   │   │   ├── model/           # JPA Entity classes (Farmer, Buyer, Product, Order)
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── service/         # Business Logic Layer
│   │   │   └── JavaPOSApplication.java  # Main Entry Point
│   │   │
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf HTML Templates
│   │       ├── static/          # CSS, JS, Images
│   │       └── application.properties  # App Configuration
│   │
│   └── test/                    # Unit & Integration Tests
│
├── pom.xml                      # Maven Dependencies
└── README.md
```

### 🔄 Request Flow

```
Browser → Spring MVC Controller → Service Layer → JPA Repository → H2 Database
                ↓
         Thymeleaf Template → HTML Response → Browser
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- ☕ **Java 21+** — [Download here](https://www.oracle.com/java/technologies/downloads/)
- 📦 **Maven 3.8+** — [Download here](https://maven.apache.org/download.cgi)
- 💻 **VS Code** (with Java Extension Pack) or **IntelliJ IDEA**

### Installation & Setup

**1. Clone the Repository**
```bash
git clone https://github.com/sahilpopalwar/Agricultural-E-Commerce-Marketplace-.git
cd Agricultural-E-Commerce-Marketplace-
```

**2. Build the Project**
```bash
mvn clean install
```

**3. Run the Application**
```bash
mvn spring-boot:run
```

**4. Access in Browser**
```
http://localhost:8443
```

> 💡 The H2 database console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`)

### Run Tests
```bash
mvn test
```

### Build JAR
```bash
mvn clean package
java -jar target/oopjava-0.0.1-SNAPSHOT.jar
```

---

## 🖥️ VS Code Setup

This repo includes `.vscode/` configuration. After cloning:

1. Open VS Code → `File > Open Folder` → Select project folder
2. Install **Extension Pack for Java** (Microsoft)
3. VS Code auto-detects Maven — run tasks from the **Spring Boot Dashboard** panel
4. Use `Ctrl + Shift + P` → `Spring Boot: Run` to start the server

---

## 📊 Key Concepts Demonstrated

- ✅ **OOP Principles** — Encapsulation, Inheritance, Polymorphism in Java 21
- ✅ **Spring IoC & DI** — Dependency Injection with `@Autowired`, `@Service`, `@Repository`
- ✅ **JPA Relationships** — One-to-Many, Many-to-One entity mappings
- ✅ **MVC Design Pattern** — Clean separation of Model, View, Controller
- ✅ **RESTful Routing** — `@GetMapping`, `@PostMapping` with Thymeleaf form binding
- ✅ **Lombok Integration** — Reducing boilerplate with `@Data`, `@Builder`, `@NoArgsConstructor`

---

## 🌐 Future Enhancements

- [ ] 🔐 Spring Security — Authentication & role-based access control
- [ ] 🛢️ MySQL / PostgreSQL integration for production
- [ ] 📱 REST API layer for mobile app integration
- [ ] 💳 Payment gateway integration (Razorpay/PayU)
- [ ] 📦 Docker containerization
- [ ] ☁️ AWS / Azure cloud deployment
- [ ] 📊 Admin dashboard with analytics

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

```bash
# 1. Fork the repo
# 2. Create your feature branch
git checkout -b feature/AmazingFeature

# 3. Commit your changes
git commit -m "Add: AmazingFeature"

# 4. Push to branch
git push origin feature/AmazingFeature

# 5. Open a Pull Request
```

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 📬 Contact

**Sahil Popalwar**

[![GitHub](https://img.shields.io/badge/GitHub-sahilpopalwar-black?style=flat-square&logo=github)](https://github.com/sahilpopalwar)

---

<div align="center">

⭐ **If you found this project helpful, please give it a star!** ⭐

*Built with ❤️ using Java & Spring Boot*

</div>
