# MoviesApp 🎥

MoviesApp is a Spring Boot application for managing movies, user authentication, and other movie-related features. It uses Java 17, PostgreSQL as the database, and provides RESTful APIs for interaction.

## Features

- User account management and JWT-based authentication.
- Movie information integration via OMDB API.
- Email notifications for account verification.
- Configurable settings for admins and general users.

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- [Java 17](https://www.oracle.com/java/technologies/javase-downloads.html)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Maven](https://maven.apache.org/install.html)
- [Postman](https://www.postman.com/downloads/) (for testing APIs)
- A valid Gmail account with an [App Password](https://support.google.com/accounts/answer/185833?hl=en) for sending emails.

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/AlaaApuelsoad/MoviesApp.git
cd MoviesApp
```

---

## Configuration

Update the `application.properties` file located in `src/main/resources`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/movies_app
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

spring.mail.username=your_email@gmail.com
spring.mail.password=your_application_password

app.ombd.api.integration.key=your_omdb_api_key
```

### Database Setup
1. Ensure PostgreSQL is running.
2. Create a database named `movies_app`:
   ```sql
   CREATE DATABASE movies_app;
   ```
3. Update the username and password in `application.properties` to match your database credentials.

---

## Run the Application

1. **Build the Project:**
   ```bash
   mvn clean install
   ```
2. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```
   The application will start at `http://localhost:5055/api/v1`.

---

## API Testing with Postman

1. Import the Postman collection provided in the repository (if available) or create your own requests.
2. Use the following base URL for all endpoints:
   ```
   http://localhost:5055/api/v1
   ```
3. Example endpoints:
   - **Account Verification:** `GET /users/verify/account/{verificationCode}`
   - **Retrieve Movies:** `GET /movies`

---

## Postman Collection

To simplify API testing, a Postman collection is available. You can use this collection to test all the endpoints in the MoviesApp.

### Steps to Import the Collection:
1. you will find the postman collection in the project repo with the name (MovieApp_Fawry.postman_collection.json).
2. Open the file and Download it.
3. Open Postman.
4. Click **Import** and select the downloaded `.json` file.

Once imported, you'll have access to all the pre-configured requests for the MoviesApp APIs.

---

## Email Functionality

- Ensure you have provided a valid Gmail account and an App Password in the `application.properties` file.
- The email templates are located at:
  ```
  src/main/resources/templates/FawryEmailTemplate.html
  ```

---

## Logging

Logs are stored in the `logs/` directory:
- Error logs: `logs/error.log`
- Log retention and rotation are configured in `application.properties`.


---

# Next Steps

## 1. **Enhancing User and Admin Services**
   - **User Service Improvements**:
     - Implement additional features for managing user profiles, such as the ability to update contact details and preferences.
     - Implement **OAuth2** authentication to allow users to authenticate via third-party identity providers (e.g., **Google**, **GitHub**, **Facebook**, or custom OAuth2 providers).
   
   - **Admin Service Improvements**:
     - Add functionality operations (e.g., trashbin for movies and admins to restore deleted movies and deleted admin).

## 2. **Adding New Features**
   - **User Preferences**: Implement a feature to allow users to customize their dashboard and notification settings.
   - **Adding more features for a user by applying a movie wishlist, rating history for his movie rating, make upcoming movie subscriptions.

## 3. **Performance Enhancement through Caching**
   - **Implement Caching**:
     - Apply caching strategies for frequently accessed data (e.g., user profiles, admin dashboards) using **Spring Cache** and popular providers like **Redis** or **EhCache**.
   
   - **Optimize Data Retrieval**: Use caching to store the results of expensive queries and reduce database load, significantly improving the performance of repeated API requests.

## 4. **Logging Improvements**
   - Extend logging with correlation IDs to track a specific request's lifecycle, which will be helpful for debugging and performance analysis.
   - Include more detailed logging at critical points in user and admin services to provide better visibility into operations.
   


## Author

Developed by **Alaa Eldin Apuelsoad**.

---

### Need Help?
Please feel free to contact me on alaaapu135@gmail.com

