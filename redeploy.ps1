Write-Host "Building..."
mvn clean package

Write-Host "Clearing old deployment..."
Remove-Item "D:\Apps\Tomcat Server\apache-tomcat-9.0.120\webapps\sunrise-dental-clinic.war" -Force -ErrorAction SilentlyContinue
Remove-Item "D:\Apps\Tomcat Server\apache-tomcat-9.0.120\webapps\sunrise-dental-clinic" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "Copying new WAR..."
Copy-Item target\sunrise-dental-clinic.war "D:\Apps\Tomcat Server\apache-tomcat-9.0.120\webapps\"

Write-Host "Done. Start Tomcat manually with startup.bat, then visit http://localhost:8080/sunrise-dental-clinic/"