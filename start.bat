@echo off
echo Starting WanderLust Travel Planning Full-Stack Application...
echo.

echo Starting Spring Boot Backend on http://localhost:8080/api ...
start "WanderLust Backend (Spring Boot)" cmd /k "cd backend && C:\Maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run"

echo Starting Vite React Frontend on http://localhost:5173 ...
start "WanderLust Frontend (Vite React)" cmd /k "cd frontend && npm run dev"

echo.
echo ========================================================
echo WanderLust Full Stack Suite is launching!
echo Frontend URL: http://localhost:5173
echo Backend API:  http://localhost:8080/api
echo Swagger UI:   http://localhost:8080/api/swagger-ui.html
echo ========================================================
echo.
pause
