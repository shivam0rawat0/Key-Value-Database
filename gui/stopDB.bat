@echo off
set /p PID=<java.pid
taskkill /f /PID %PID%