Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Starting WanderLust Full Stack Travel Planning Suite     " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$PSScriptRoot\backend'; & 'C:\Maven\apache-maven-3.9.6\bin\mvn.cmd' spring-boot:run"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$PSScriptRoot\frontend'; npm run dev"

Write-Host "`nBoth services are starting in dedicated terminal windows!" -ForegroundColor Green
Write-Host "Frontend Application: http://localhost:5173" -ForegroundColor Yellow
Write-Host "Backend REST API:     http://localhost:8080/api" -ForegroundColor Yellow
Write-Host "OpenAPI / Swagger UI: http://localhost:8080/api/swagger-ui.html" -ForegroundColor Yellow
Write-Host "`nLogin Demo Account:   demo@wanderlust.com / password123" -ForegroundColor White
