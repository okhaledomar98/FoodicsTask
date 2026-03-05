# QA Automation Framework - API & UI

This repository contains a scalable and maintainable test automation framework built with Java. It covers both backend API testing (REST Assured) and frontend UI testing (Selenium WebDriver).

## Technology Stack
- **Language:** Java 17
- **Build Tool:** Maven
- **Test Runner:** TestNG
- **API Automation:** REST Assured
- **UI Automation:** Selenium WebDriver 4.x
- **Data Binding (JSON):** Jackson (POJO serialization/deserialization)
- **Boilerplate Reduction:** Lombok
- **Design Patterns:** Page Object Model (POM), Singleton, Builder

## Project Architecture
- `src/main/java/config`: `ConfigReader` for centralized configuration access.
- `src/main/java/api/models`: API payload POJOs (`UserRequest`, `UserResponse`).
- `src/main/java/utils`: shared utilities (`TestContext`, waits, driver management).
- `src/main/resources`: `config.properties` for environment URLs and test data.
- `src/test/java/base`: base test setup (`BaseApiTest`, `BaseUITest`).
- `src/test/java/apiTests`: API test scenarios.
- `src/test/java/uiTests`: UI test scenarios (Amazon flow).

## Prerequisites
- JDK 17
- Maven 3.8+
- Chrome browser (latest stable recommended)

## How to Install and Run

1. **Clone the repository**
```bash
git clone <your-repository-url>
cd FoodicsTask
```

2. **Install dependencies**
```bash
mvn clean install -DskipTests
```

3. **Run API tests**
```bash
mvn test -Dtest="apiTests.*"
```

4. **Run Amazon UI task**
```bash
mvn test -Dtest="uiTests.AmazonScenarioTest"
```

## Amazon UI Task

The Amazon scenario automates a safe checkout path and stops before placing an order.

### Covered Flow
1. Login to Amazon using credentials from `config.properties`.
2. Ensure the UI language is English.
3. Navigate to **All > Video Games > All Video Games**.
4. Apply **Free Shipping** and **New** filters, then sort by **High to Low**.
5. Add the first product below **15,000 EGP** (open PDP in new tab, add to cart, close tab).
6. Verify cart item count.
7. Proceed to checkout and handle delivery address creation/selection when required.
8. Reach the checkout payment page.
9. Stop before placing any order.

### End Condition
The UI test intentionally ends after the payment page is reached and prints a success message. It does not select payment options or place an order.

## API Test Notes (Reqres)

API tests are executed against [reqres.in](https://reqres.in).

### 1) Cloudflare WAF / JS Challenge (403)
At times, `reqres.in` may return `403 Forbidden` due to Cloudflare WAF / JavaScript challenge protection.

This behavior can appear with multiple API clients (including Postman and REST Assured), depending on Cloudflare's active security mode, request fingerprint, and session context.

Root cause: the endpoint may require browser-like JavaScript/cookie validation before allowing the request. Non-browser API clients do not execute that browser challenge flow the same way, so requests can be blocked intermittently.

This is an external environment/server-side constraint, not a framework logic defect.

### 2) Stateless Behavior (404 after create)
Reqres is a mock/stateless API. A successful `POST` returns an ID, but this user is not truly persisted. A subsequent `GET /api/users/{id}` can return `404 Not Found`.

The framework still extracts and shares IDs via thread-safe context (`TestContext`) to preserve realistic test-flow structure.

This point is also impacted by point 1 (Cloudflare WAF / JS challenge): if traffic is challenged or blocked (`403`), create/retrieve flow results can be inconsistent. So `404` analysis should be interpreted after ruling out Cloudflare-side interference first.