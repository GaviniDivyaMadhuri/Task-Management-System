@echo off
title TaskFlow - Build & Run
color 0A

echo ============================================
echo   TaskFlow - Task Management System
echo   Building project...
echo ============================================
echo.

set SRC=src
set OUT=out
set MAIN=com.taskflow.MainApp

if not exist %OUT% mkdir %OUT%

echo [1/2] Compiling Java sources...
javac -sourcepath %SRC% -d %OUT% ^
  %SRC%\com\taskflow\model\Task.java ^
  %SRC%\com\taskflow\dao\TaskDAO.java ^
  %SRC%\com\taskflow\ui\DashboardPanel.java ^
  %SRC%\com\taskflow\ui\TaskFormDialog.java ^
  %SRC%\com\taskflow\ui\TaskListPanel.java ^
  %SRC%\com\taskflow\ui\MainWindow.java ^
  %SRC%\com\taskflow\MainApp.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Check errors above.
    pause
    exit /b 1
)

echo [2/2] Launching TaskFlow...
echo.
java -cp %OUT% %MAIN%

pause
