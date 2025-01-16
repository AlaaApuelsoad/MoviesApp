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
- [PostgreSQL 14+](https://www.postgresql.org/download/)
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
1. Download the Postman collection from the repository: [MoviesApp Postman Collection](postman/MoviesApp.postman_collection.json).
2. Open Postman.
3. Click **Import** and select the downloaded `.json` file.

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

## Additional Information

### Environment Variables
You can replace sensitive properties in `application.properties` with environment variables:
```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

Set the environment variables in your shell or IDE.

---

## Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Author

Developed by **Alaa Eldin Apuelsoad**.

---

### Need Help?
For any issues, please feel free to create an issue in this repository.

