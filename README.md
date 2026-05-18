# Ai-Question-Paper-Generator

> A Java-based tool for automating the generation of examination question papers.

![GitHub stars](https://img.shields.io/github/stars/Zubair82187/Ai-Question-Paper-Generator?style=for-the-badge&logo=github) ![GitHub forks](https://img.shields.io/github/forks/Zubair82187/Ai-Question-Paper-Generator?style=for-the-badge&logo=github) ![GitHub issues](https://img.shields.io/github/issues/Zubair82187/Ai-Question-Paper-Generator?style=for-the-badge&logo=github) ![Last commit](https://img.shields.io/github/last-commit/Zubair82187/Ai-Question-Paper-Generator?style=for-the-badge&logo=github) ![Java (Maven)](https://img.shields.io/badge/Java%20(Maven)-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

## 🚀 Features

| Feature | Description |
| :--- | :--- |
| 📄 **PDF Processing** | Generate comprehensive question papers directly from uploaded books and PDFs. |
| 🧠 **Mixed Formats** | Create well-structured MCQs, alongside short and long subjective questions. |
| 🎛️ **Customizable** | Tailor generation by difficulty, topic, and question count to suit exact needs. |
| 🔐 **Secure Access** | Protected RESTful API routes backed by standard user authentication protocols. |
| 🌍 **Social Login** | Integrated Google OAuth2 support for frictionless user sign-ups and logins. |
| 💻 **API First** | Built with frontends in mind, delivering clean, structured JSON responses. |
## 📁 Project Structure

```
.
├── .mvn
│   └── wrapper
│       └── maven-wrapper.properties
├── clear
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── ai_question_paper_generator
    │   │           └── configuration
    |   |           └── controller
    |   |           └── model
    |   |           └── dto
    |   |           └── repository
    |   |           └── service
    |   |           └── mapper
    |   |           └── exception
    |   |           └── enums
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── ai_question_paper_generator
                    └── ...
```

## 🧠 AI Processing Pipeline

**1. Document Parsing** 📄  
Automatically extracts raw text and structural data from uploaded books and PDFs.

**2. Intelligent Chunking** ✂️  
Splits large text corpuses into optimized, manageable chunks to maintain context during AI processing.

**3. Vector Embeddings** 🌌  
Leverages **Google Gemini** to generate high-quality vector embeddings for precise semantic understanding.

**4. High-Speed Generation** ⚡  
Utilizes the **Llama** model, powered by the **Groq API**, to rapidly synthesize and generate contextually accurate questions.

**5. Structured Delivery** 📦  
Formats the final output into clean, predictable JSON, ensuring seamless and effortless frontend integration.
## 🛠️ Tech Stack

| Technology | Purpose |
| --- | --- |
| **Spring Boot** | Backend framework for building RESTful APIs |
| **Spring Security** | Provides authentication and authorization mechanisms |
| **JWT** | Secure token-based authentication |
| **OAuth2** | Standard protocol for secure authorization |
| **MySQL** | Relational database for storing books, users, and generated questions |
| **Groq API** | High-performance inference engine for AI-powered question generation |
| **Gemini Embeddings** | Embedding model for semantic understanding of text and chapters |


## 🚀 Live API Base URL

The backend is deployed and accessible via Render:

```text
https://ai-question-paper-generator-server.onrender.com
```

---

## 🔐 How to Use the API (Authentication)

Because the API is secured, you must authenticate using Google OAuth2 to receive a JWT before making requests.

### 1️⃣ Get Your JWT Token

Open your browser and visit:

```text
https://ai-question-paper-generator-server.onrender.com/oauth2/authorization/google
```

### 2️⃣ Login with Google

- Sign in using your Google account
- After successful authentication, the server will return a JWT token

---

## 📮 Using the JWT Token in Postman

### 1️⃣ Open Postman

Create a new request.

### 2️⃣ Enter API Endpoint

Example:

```text
GET https://ai-question-paper-generator-server.onrender.com/api/endpoint
```

### 3️⃣ Add Authorization

- Go to the **Authorization** tab
- Select: Auth type

```text
OAuth2
```

- Paste your JWT token into the token field

### 4️⃣ Send Request

Click:

```text
Send
```

You will receive the JSON response from the API.
# 📡 API Reference

| Endpoint | Method | Body Type | Example Request | Description |
| --- | --- | --- | --- | --- |
| `/api/upload` | `POST` | `multipart/form-data` (File + JSON) | Upload PDF and metadata | Uploads and processes a PDF/book |
| `/api/book` | `GET` | `None` | `GET /api/book` | Fetches all uploaded books of authenticated user |
| `/api/questions/generate_all_type` | `POST` | `application/json` | Generate all question types from book | Generates MCQ, short, and long questions |
| `/api/questions/generate_mcq` | `POST` | `application/json` | Generate MCQs from book | Generates multiple-choice questions |
| `/api/questions/generate_short` | `POST` | `application/json` | Generate short questions from book | Generates short-answer questions |
| `/api/questions/generate_long` | `POST` | `application/json` | Generate long questions from book | Generates descriptive/long-answer questions |
| `/api/questions/generate_mcq_from_chapter` | `POST` | `application/json` | Generate MCQs from chapter | Generates chapter-specific MCQs |
| `/api/questions/generate_short_from_chapter` | `POST` | `application/json` | Generate short questions from chapter | Generates chapter-specific short questions |
| `/api/questions/generate_long_from_chapter` | `POST` | `application/json` | Generate long questions from chapter | Generates chapter-specific long questions |
| `/api/questions/generate_mcq_from_topic` | `POST` | `application/json` | Generate MCQs from topic | Generates topic-specific MCQs |
| `/api/questions/generate_short_from_topic` | `POST` | `application/json` | Generate short questions from topic | Generates topic-specific short questions |
| `/api/questions/generate_long_from_topic` | `POST` | `application/json` | Generate long questions from topic | Generates topic-specific long questions |

---

# 📄 Upload Book API

## Endpoint

```http
POST /api/upload
```

## Body Type

```text
multipart/form-data
```

## Form Data Fields

| Key | Type | Description |
| --- | --- | --- |
| `pdf` | File | PDF/book file |
| `bookDtoBasic` | JSON | Metadata related to the book |

## Example Request

### Form Data

```json
{
"userId":1,
"bookName": "Physics NCERT",
"subject": "physics"
}
```

Attach:
- `pdf` → Select PDF file
- `bookDtoBasic` → JSON metadata

## Example Response

```json
{
  "message": "File uploaded successfully"
}
```

---

# 📚 Get User Books API

## Endpoint

```http
GET /api/book
```

## Headers

```http
Authorization: Bearer <JWT_TOKEN>
```

## Example Response

```json
[
  {
    "id": 1,
    "title": "Operating System",
    "author": "Galvin"
  }
]
```

---

# 🧠 Generate All Question Types

## Endpoint

```http
POST /api/questions/generate_all_type
```

## Body Type

```json
application/json
```

## Example Request

```json
{
    "book_id": 1,
    "question_count": 10,
    "number_of_mcq": 7,
    "number_of_short_question": 2,
    "number_of_long_question":1,
    "difficulty": "HARD"
}
```

## Description

Generates:
- MCQs
- Short questions
- Long questions

from the selected book.

---

# ✅ Generate MCQs

## Endpoint

```http
POST /api/questions/generate_mcq
```

## Example Request

```json
{
  "bookId": 1,
  "difficulty": "easy",
  "count": 10
}
```

## Description

Generates multiple-choice questions from the selected book.

---

# ✏️ Generate Short Questions

## Endpoint

```http
POST /api/questions/generate_short
```

## Example Request

```json
{
    "book_id": 1,
    "question_count": 5,
    "difficulty": "EASY"
}
```

## Description

Generates short-answer questions from the selected book.

---

# 📝 Generate Long Questions

## Endpoint

```http
POST /api/questions/generate_long
```

## Example Request

```json
{
    "book_id": 1,
    "question_count": 5,
    "difficulty": "EASY"
}
```

## Description

Generates descriptive long-answer questions from the selected book.

---

# 📘 Generate Questions From Chapter

## MCQ Endpoint

```http
POST /api/questions/generate_mcq_from_chapter
```

## Short Question Endpoint

```http
POST /api/questions/generate_short_from_chapter
```

## Long Question Endpoint

```http
POST /api/questions/generate_long_from_chapter
```

## Example Request

```json
{
    "book_id": 1,
    "chapterName": "Current",
    "subjectName": "Physics",
    "questionCount": 3,
    "difficulty": "MEDIUM"
}
```

## Description

Generates questions from a specific chapter of the uploaded book.

---

# 🎯 Generate Questions From Topic

## MCQ Endpoint

```http
POST /api/questions/generate_mcq_from_topic
```

## Short Question Endpoint

```http
POST /api/questions/generate_short_from_topic
```

## Long Question Endpoint

```http
POST /api/questions/generate_long_from_topic
```

## Example Request

```json
{
    "book_id": 1,
    "questionCount": 5,
    "subjectName": "Physics",
    "difficulty": "MEDIUM",
    "topic": "Ohm's Law"
}
```

## Description

Generates questions from a specific topic within the book.

---

# 🔐 Authentication

All protected endpoints require JWT authentication.

## Header Format

```http
Authorization: Bearer <JWT_TOKEN>
```


# ⚙️ Local Setup

## 1️⃣ Clone Repository

```bash
git clone https://github.com/Zubair82187/Ai-Question-Paper-Generator

cd ai-question-paper-generator
```

---

## 2️⃣ Setup MySQL Database

Create a MySQL database manually.

Example:

```sql
CREATE DATABASE ai_question_paper_generator;
```

---

## 3️⃣ Configure Environment Variables

Create a `.env` file in the root directory and add:

```env
DB_HOST=your_host_name
DB_PORT=your_port
DB_NAME=your_database_name
DB_USERNAME=your_username
DB_PASSWORD=your_password

MULTIPART_FILE_SIZE_MAX=10MB
MULTIPART_REQUEST_FILE_SIZE_MAX=10MB

GROQ_API_KEY=your_groq_api_key

SPRING_PROFILES_ACTIVE=ollama

GOOGLE_SECRET=your_google_secret
GOOGLE_CLIENT_ID=your_google_client_id

JWT_SECRET=your_jwt_secret
```

---

## 4️⃣ Run the Application

### Linux / Mac

```bash
./mvnw spring-boot:run
```

### Windows

```bash
mvnw.cmd spring-boot:run
```

---

## 5️⃣ Access the Backend

```text
http://localhost:8080
```
