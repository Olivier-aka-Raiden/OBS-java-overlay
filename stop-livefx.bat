echo Stopping React app...
for /f "tokens=2" %%i in ('tasklist /nh /fi "imagename eq node.exe"') do taskkill /f /pid %%i

echo Stopping Spring Boot app...
for /f "tokens=5" %%i in ('netstat -ano ^| findstr :8080 ^| findstr /i "listening"') do taskkill /f /pid %%i

echo Done.