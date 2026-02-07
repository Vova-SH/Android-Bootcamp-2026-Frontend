@echo off
echo Добавление правила брандмауэра для порта 8080...
netsh advfirewall firewall add rule name="Spring Boot 8080" dir=in action=allow protocol=TCP localport=8080
echo.
echo Готово! Перезапустите приложение на эмуляторе/телефоне.
pause
