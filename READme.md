# **EduGate API**

This repository contains the Spring Boot backend API for the **EduGate Online Course Catalogue**. It provides a complete, role-based system for managing users, courses, and subscriptions, with a secure JWT-based authentication system.

## **Features**

* **JWT Authentication:** Secure user registration and login using JSON Web Tokens.  
* **Role-Based Access Control:** Three distinct user roles with different permissions:  
  * **ROLE\_USER**: Can browse approved courses, subscribe/unsubscribe, and manage their profile.  
  * **ROLE\_INSTRUCTOR**: Can submit new courses for approval, manage their existing courses, and request course removal.  
  * **ROLE\_ADMIN**: Has full control. Can manage users (promote/demote), approve or reject pending courses, and view admin action logs.  
* **Course Management:**  
  * Instructors can upload courses with names, categories, video links, and image thumbnails.  
  * Admins review all new course submissions and removal requests.  
* **File Uploads:** Handles multipart/form-data for course thumbnails, saves them to the server's file system, and serves them securely.  
* **Subscription System:** Users can subscribe to courses, which are then added to their "My Courses" list.  
* **API Documentation:** Automatically generated, interactive API documentation via SpringDoc (Swagger UI).  
* **Global Error Handling:** Standardized ApiResponse wrapper for all successful and error responses.

## **Technologies Used**

* **Java 21**  
* **Spring Boot 3.3.4**  
  * Spring Web  
  * Spring Security  
  * Spring Data JPA  
* **Database:** PostgreSQL  
* **Authentication:** jjwt (Java JWT) for token generation and validation.  
* **API Docs:** springdoc-openapi-starter-webmvc-ui (Swagger)  
* **Utilities:** Lombok

## **Prerequisites**

Before you begin, ensure you have the following installed:

* **JDK 21** or later  
* **Maven 3.6** or later  
* **PostgreSQL** running on localhost:5432 or as a service.

## **Setup and Installation**

1. **Clone the repository:**  
   git clone \<your-repo-url\>  
   cd edugateapi

2. Create the PostgreSQL Database:  
   Open psql or your preferred database client and create a new database.  
   CREATE DATABASE edugate\_db;

3. Configure the Application:  
   Open src/main/resources/application.properties and update the following sections to match your local environment.  
   \# Server Configuration  
   server.port=8080

   \# PostgreSQL Database Configuration  
   \# Update username and password to match your setup  
   spring.datasource.url=jdbc:postgresql://localhost:5432/edugate\_db  
   spring.datasource.username=postgres  
   spring.datasource.password=

   \# File Upload Configuration  
   \# IMPORTANT: Create this directory on your system or change the path.  
   \# Using a relative path is recommended for development.  
   file.upload-dir=./uploads/thumbnails

   \# JWT Secret Key  
   \# IMPORTANT: Replace this with your own strong, 32-byte (64-char hex) secret key  
   jwt.secret-key=

   * **Note on file.upload-dir**: Make sure the directory you specify (e.g., ./uploads/thumbnails) exists, or the application will fail on file upload.  
4. **Run the application:**  
   mvn spring-boot:run

   The API will start on http://localhost:8080.

## **API Documentation (Swagger)**

This project includes interactive API documentation. Once the application is running, you can access it at:

[**http://localhost:8080/swagger-ui.html**](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)

### **Using the Secured Endpoints:**

1. Use the POST /api/auth/register endpoint to create a new user.  
2. Use the POST /api/auth/login endpoint with your new credentials to get a JWT.  
3. Copy the entire token from the response.  
4. At the top of the Swagger UI, click the **"Authorize"** button.  
5. In the dialog, type Bearer (with a space) followed by your token (e.g., Bearer eyJhbGci...).  
6. You can now successfully test all the secured endpoints.

## **API Endpoint Summary**

### **Authentication (/api/auth)**

* POST /register: Registers a new user (default role: ROLE\_USER).  
* POST /login: Authenticates a user and returns a JWT.

### **User (/api/user)**

* GET /courses: Gets all *approved* courses for browsing.  
* GET /courses/my-subscriptions: Gets all courses the current user is subscribed to.  
* POST /courses/subscribe/{courseId}: Subscribes the user to a course.  
* DELETE /courses/unsubscribe/{courseId}: Unsubscribes the user from a course.  
* GET /profile/me: Gets the current user's profile details.  
* PUT /profile/me: Updates the current user's profile (full name, phone number).

### **Instructor (/api/instructor)**

*(Requires ROLE\_INSTRUCTOR)*

* POST /courses: (Consumes multipart/form-data) Creates a new course and submits it for admin approval.  
* DELETE /courses/{courseId}: Requests removal of a course owned by the instructor.  
* GET /courses/my-courses: Gets all courses submitted by the current instructor.

### **Admin (/api/admin)**

*(Requires ROLE\_ADMIN)*

* GET /users: Gets a list of all users.  
* PUT /users/promote/{userId}: Promotes a ROLE\_USER to ROLE\_INSTRUCTOR.  
* PUT /users/demote/{userId}: Demotes a ROLE\_INSTRUCTOR to ROLE\_USER.  
* GET /courses/pending: Gets all courses pending admin approval (both new additions and removals).  
* POST /courses/approve/{courseId}: Approves a pending course or a course removal.  
* POST /courses/reject/{courseId}: Rejects a pending course or a course removal.  
* GET /logs/me: Gets a paginated list of all actions taken by the current admin.

### **File Controller (/api/images)**

* GET /{filename:.+}: Serves the static image file for a course thumbnail.
