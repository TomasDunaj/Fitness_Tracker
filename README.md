# 🏋️‍♂️ Fitness Tracker

<p align="center">
  A full-stack workout tracking application built with React, Spring Boot, and PostgreSQL.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
</p>

---

## 📖 About the Project

**Fitness Tracker** is a full-stack web application designed to help users record, organize, and track their workout progress.

The application allows users to create workout sessions, add exercises, log sets with repetitions and weights, and keep a structured history of their training.

This project was built from scratch as a personal learning project to gain practical experience with **frontend development, backend development, and database management**. The main goal was to understand how a complete web application works by connecting the user interface, backend services, and database into one functional system.

---

## ✨ Features

- 🏋️ **Workout Management**  
  Create and manage workout sessions.

- 💪 **Exercise Tracking**  
  Add exercises with sets, repetitions, and weights.

- 📅 **Workout History**  
  View previous training sessions in an organized format.

- 💾 **Database Storage**  
  Store workout data permanently using PostgreSQL.

- 🌙 **Responsive Dark UI**  
  Clean interface designed with modern CSS.

- 🔄 **Frontend ↔ Backend Communication**  
  React communicates with Spring Boot through REST API endpoints.

---

# 🛠️ Tech Stack

## 🎨 Frontend

<p>
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"/>
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"/>
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/>
</p>

Used for building the user interface, managing application state, creating reusable components, and displaying dynamic workout data.

---

## ⚙️ Backend

<p>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"/>
</p>

Used for developing REST APIs, handling application logic, managing requests, and connecting the frontend with the database layer.

---

## 🐘 Database

<p>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
</p>

Used for storing workout data, managing relationships between entities, and learning database design and persistence.

---

# 🧠 What I Learned

## ⚛️ React Development

- Building reusable components.
- Managing application state using `useState` and `useEffect`.
- Fetching and displaying data from backend APIs.
- Creating responsive layouts and improving user experience.

## 🍃 Spring Boot Development

- Creating RESTful APIs.
- Structuring backend applications using controllers, services, and repositories.
- Implementing CRUD operations.
- Understanding backend architecture and data flow.

## 🐘 Database Development

- Working with PostgreSQL.
- Designing relational database structures.
- Creating entity relationships.
- Connecting Java applications with databases using Spring Data JPA.
- Understanding how application data is stored and retrieved.

---

# 🔗 Application Architecture

```text
                 React Frontend
                       |
                       |
              REST API Communication
                       |
                       ↓
              Spring Boot Backend
                       |
                       |
                Spring Data JPA
                       |
                       ↓
              PostgreSQL Database
