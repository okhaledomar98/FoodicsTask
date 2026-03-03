# 🚀 QA Automation Framework - API & UI

This repository contains a robust, scalable, and maintainable test automation framework built with **Java**. It is designed to handle both Backend API testing (using REST Assured) and Frontend UI testing (using Selenium WebDriver).

## 🛠️ Technology Stack
* **Language:** Java 11+
* **Build Tool:** Maven
* **Test Runner:** TestNG
* **API Automation:** REST Assured
* **UI Automation:** Selenium WebDriver 4.x *(In Progress)*
* **Data Binding (JSON):** Jackson (POJO Serialization/Deserialization)
* **Boilerplate Reduction:** Lombok
* **Design Patterns:** Page Object Model (POM), Singleton Pattern, Builder Pattern.

---

## 📂 Project Architecture

The framework follows a modular structure to separate concerns:
* `src/main/java/config`: Contains `ConfigReader` to manage environment variables centrally.
* `src/main/java/api/models`: Contains POJO classes (`UserRequest`, `UserResponse`) for API payloads.
* `src/main/java/utils`: Contains `TestContext`, a `ThreadLocal` manager for safe data sharing between tests (e.g., sharing User IDs across scenarios).
* `src/main/resources`: Contains `config.properties` for non-sensitive test data and Base URLs.
* `src/test/java/base`: Contains Base setup classes (`BaseApiTest`, `BaseUITest`) to initialize and teardown configurations.
* `src/test/java/apiTests`: Contains the actual API test scenarios.
* `src/test/java/uiTests`: *(To be added - Amazon scenarios)*

---

## ⚙️ How to Install and Run

1. **Clone the repository:**
   ```bash
   git clone <your-repository-url>
   cd FoodicsTask
   
2. **Install Dependencies (Maven):**

    
     mvn clean install -DskipTests 
  
  

3. **Run API Tests:**
   
     
    mvn test -Dtest="apiTests.*"



⚠️ Important Notes (API Tests)
The API tests are executed against https://reqres.in. While implementing the scenarios (Create, Retrieve, Update), two critical server-side behaviors were observed and handled:

1. Cloudflare WAF & JS Challenge (403 Forbidden)
   reqres.in employs strict Cloudflare Web Application Firewall (WAF) protection. During test execution, you might encounter a 403 Forbidden response instead of 201 Created.

Root Cause Analysis: Cross-verification via Postman/cURL reveals that the server occasionally activates a "JS Challenge" (I'm Under Attack mode) requiring JavaScript execution and cookies validation to proceed. Since REST Assured is a programmatic API client and not a browser, it cannot execute this JS challenge, resulting in a 403 block. This is a server-side security constraint, not a framework defect. The code is structurally correct based on the API documentation.

2. The Stateless Nature of Reqres API (404 Not Found on GET)
   The assignment requires creating a user (POST), extracting the ID, and retrieving it (GET). However, reqres.in is a fake/stateless API. The POST request returns 201 Created with a mock ID, but this data is never actually saved to their database.

Framework Handling: Consequently, calling GET /api/users/{new_id} will realistically return a 404 Not Found. To fulfill the assignment's logical requirements, the framework is designed to extract the ID and pass it to the GET/PUT requests using a thread-safe TestContext. The assertions in the code are written assuming a stateful, real-world database behavior.