@echo off

set APP_DIR=D:/Workspace/java/livefx/

echo Starting Spring Boot app...
start /B mvn clean install -f %APP_DIR%pom.xml -Pstart-app

echo Starting React app...
start /B mvn clean install -f %APP_DIR%pom.xml -Pstart-react-app