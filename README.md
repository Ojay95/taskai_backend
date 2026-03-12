#  TaskAI Backend: Production-Ready AI Task Manager

**A Spring Boot 3.x implementation featuring Gemini 3 Native Audio Reminders.**

This backend provides a high-density task management API modeled after Bitrix24's UI structure and Google Tasks' utility, featuring an emotionally intelligent AI voice reminder system.

---

##  🏗 Architecture Overview

This project follows **Domain-Driven Design (DDD)** principles to ensure scalability and isolation of the AI logic.

* **Identity Context:** User management, JWT authentication, and subscription gating ($7/mo logic).
* **Task Context:** Core CRUD operations and priority/tone management.
* **Notification Context:** Orchestration of Gemini Native Audio generation and scheduling.
* **Infrastructure Layer:** Anti-Corruption Layer (ACL) for Gemini API and File Storage.

---

##  🚀 Core API Endpoints

### 1. Identity & Auth (`/api/auth`)

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/signup` | Register a new user (Default: FREE tier). |
| `POST` | `/login` | Returns a JWT Bearer token. |

### 2. Task Management (`/api/tasks`)

*Requires `Authorization: Bearer <token>*`
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/` | Retrieve all tasks for the authenticated user. |
| `POST` | `/` | Create a task with `aiTone` (GENTLE, SERIOUS, PROFESSIONAL). |
| `DELETE` | `/{id}` | Remove a task. |

### 3. Voice Settings (`/api/voice-settings`)

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/` | Fetch preferred Gemini Voice (e.g., Puck, Kore). |
| `POST` | `/` | Update AI assistant persona settings. |

---

##  🤖 AI Voice Logic

When a task reminder is triggered, the system performs the following in the **Infrastructure Layer**:

1. **Context Assembly:** Combines User Name, Task Title, and Description.
2. **Native Generation:** Sends a single request to **Gemini 3 Flash** with `response_modalities: ["AUDIO"]`.
3. **Direct Decoding:** Decodes the Base64 audio stream into binary.
4. **Storage:** Persists the `.wav` file to the `/uploads/audio` directory.
5. **Delivery:** Updates the task's `voiceFileUrl` for frontend playback.

---

##  🛠 Technical Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.2+
* **Security:** Spring Security + JJWT
* **Database:** PostgreSQL
* **AI Engine:** Gemini 3 Flash (Google AI Studio)
* **Documentation:** Swagger UI (OpenAPI 3.0)

---

##  ⚙️ Installation & Setup

1. **Clone the repository:**
```bash
git clone https://github.com/your-username/taskai-backend.git

```


2. **Configure Environment Variables:**
   Create a `.env` file or export the following:
```bash
export GEMINI_API_KEY=your_google_ai_key
export JWT_SECRET=your_32_character_secret_key
export DB_URL=jdbc:postgresql://localhost:5432/taskai_db

```


3. **Build and Run:**
```bash
./mvnw spring-boot:run

```


4. **Access API Documentation:**
   Navigate to `http://localhost:8080/swagger-ui.html` to test endpoints.

---

##  🔒 Security Note

This API uses stateless JWT authentication. Ensure that the `JWT_SECRET` is changed for every environment and never committed to version control. The storage folder `/uploads/audio` should be served over HTTPS in production.

---

**Everything is now documented and ready for deployment.** **Would you like me to generate the `TaskAI-Postman-Collection.json` content so you can import all these requests into Postman for immediate testing?**