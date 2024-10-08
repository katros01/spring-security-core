# spring-security-core

### Prerequisites

- Java 21 or later
- Maven 
- A database ( PostgreSQL)


### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/katros01/spring-security-core.git
    cd spring-security-core
    ```

2. **Configure the database:**

   Update the `.env` file with your database credentials.

    ```
    POSTGRESQL_PORT=
    POSTGRESQL_USERNAME=
    POSTGRESQL_PASSWORD=
    POSTGRESQL_HOST=
    POSTGRESQL_DATABASE=

    SECRET_KEY=
    ```

3. **Build the project:**

    ```bash
    ./mvnw clean install
    ```

4. **Run the application:**

    ```bash
    ./mvnw spring-boot:run
    ```

### API Endpoints

- **Authentication:**
  - `POST /api/auth/signup` - Register with firstname, lastname, email and password.
  - `POST /api/auth/login` - Login with email and password.

- **Users:**
    - `POST /api/users` - Register a new user.
    - `GET /api/users` - Get all user details.
    - `PATCH /api/users/assign-role/{id}` - Update user role.

- **Products:**
    - `POST /api/products` - Create a new product.
    - `GET /api/products/{id}` - Get product details.
    - `PATCH /api/products/{id}` - Update a product.
    - `DELETE /api/products/{id}` - Delete a product.

### Custom Error Handling

Errors are handled globally and return a consistent JSON response structure:

```json
{
  "message": "Error message",
  "statusCode": 400,
  "data": null
}

```

### Demo Links: 
1. https://www.loom.com/share/ee8995313ca1417d8b8dd02ce61ac33d?sid=ca871df9-e66d-49ff-8ac7-95f911d371bf
2. https://www.loom.com/share/d2c598552b5149979ca80c76efe3616a?sid=7ed78cb3-1936-486c-bc3f-90f036a57f38


