# Carpooling Web Application

Carpooling is a web application that enables users to share travel plans or join others' rides. It offers public, private, and administrative functionalities, as well as a REST API for extensibility.

---

## Features

### Public Features
- View platform information and statistics.
- Register and log in (email verification required).

### Private Features
- Manage profile and set profile picture.
- Create, browse, and apply for travel plans.
- Leave feedback for drivers and passengers.

### Admin Features
- Manage users and travels.
- Search, filter, sort, and block/unblock users.

### REST API
- CRUD operations for users and travels.
- Integrate with Microsoft Bing Maps API for route calculations.

---

## Setup Instructions

### Prerequisites
- Node.js/Python/Java (choose your backend).
- PostgreSQL/MySQL database.
- [Bing Maps API Key](https://www.microsoft.com/maps).

### Backend Setup
1. Navigate to the `backend/` directory.
2. Install dependencies: `pip install -r requirements.txt` (or `npm install`).
3. Set environment variables in `.env` file (use `.env.example` as a template).
4. Run the development server.

### Frontend Setup
1. Navigate to the `frontend/` directory.
2. Install dependencies: `npm install`.
3. Set environment variables in `.env` file.
4. Run the development server: `npm start`.

---

## Database

### Schema Diagram
![Database Schema](database/ERD.png)

### Setup
1. Run `schema.sql` to create the database structure.
2. Run `seed.sql` to populate with sample data.

---

## Documentation
- [Swagger API Documentation](link-to-swagger-docs)

---

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md).

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
