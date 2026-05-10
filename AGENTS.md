# AGENTS.md

## Cursor Cloud specific instructions

### Architecture

- **Backend**: Java 21 + Spring Boot 3.2 + Spring Data JPA + H2 (file-based) + JWT auth
- **Frontend**: React 18 + Vite + Ant Design 5

### Running the application

**Backend** (port 8080):
```
cd backend && mvn spring-boot:run
```

**Frontend** (port 5173):
```
cd frontend && npm run dev
```

### Key commands

| Task | Command |
|------|---------|
| Backend compile | `cd backend && mvn compile` |
| Backend test | `cd backend && mvn test` |
| Frontend dev | `cd frontend && npm run dev` |
| Frontend build | `cd frontend && npm run build` |
| Frontend lint | `cd frontend && npx eslint src/` |

### Default credentials

Admin login: `admin` / `admin123`

### Notes

- H2 database is file-based at `backend/data/research_db`. Delete this directory to reset all data.
- CORS is configured for `localhost:5173` and `localhost:3000`.
- The backend seeds sample data (projects, researchers) on first startup via `DataInitializer`.
- Maven is required as a system dependency (`sudo apt-get install maven`).
