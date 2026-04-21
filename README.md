# 🚀 Task Management System (TaskFlow)

TaskFlow is a Java-based desktop application designed to help users efficiently manage daily tasks. It provides a clean and interactive interface to create, update, delete, and track tasks with persistent storage.

This project demonstrates strong fundamentals in Java, Object-Oriented Programming, and GUI development using Swing.

---

## ✨ Features

- ✅ Add, update, and delete tasks (CRUD operations)
- 🔍 Filter tasks based on status (Completed / Pending)
- 🏷️ Status indicators for better task tracking
- 💾 Persistent storage using file handling
- 🎨 User-friendly GUI built with Java Swing
- ⚡ Lightweight and fast desktop application

---

## 🛠️ Tech Stack

- **Java 17**
- **Swing (GUI)**
- **File Handling / Serialization**

---

## 📁 Project Structure
Task-Management-System/
│── TaskFlow/
│ ├── src/
│ │ ├── model/ # Task class (data structure)
│ │ ├── service/ # Business logic
│ │ ├── ui/ # GUI components (Swing)
│ │ ├── util/ # File handling utilities
│ │ └── Main.java # Entry point
│ │
│ ├── data/ # Stored task data
│
│── README.md                                                                                                                                                       
---

## ▶️ Getting Started

### 🔹 Option 1: Run using script
- Double-click `run.bat`

### 🔹 Option 2: Run manually

```bash
javac -d bin src/**/*.java
java -cp bin Main                
