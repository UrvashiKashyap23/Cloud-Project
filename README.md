# E-Commerce Cloud Deployment Project

## 1. Project Overview

This project is a Spring Boot based E-Commerce backend application that has been containerized using Docker and deployed on Amazon Web Services (AWS).

The project demonstrates practical use of cloud and distributed-system technologies through:

- Docker containerization
- Docker Compose
- PostgreSQL database container
- Amazon Elastic Container Registry (ECR)
- Amazon EC2
- GitHub Actions
- Automated testing
- Spring Boot REST APIs
- JWT-based authentication

The application provides REST APIs for user registration, authentication, and profile management.

---

## 2. Technologies Used

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL 16
- Maven
- Docker
- Docker Compose
- GitHub
- GitHub Actions
- Amazon ECR
- Amazon EC2
- Ubuntu Server

---

## 3. Architecture

The overall deployment architecture is:

```text
                         Client
                           |
                           | HTTP Request
                           v
                    Amazon EC2 Instance
                           |
                           |
                  +--------+--------+
                  |                 |
                  v                 v
          Spring Boot App      PostgreSQL
          Docker Container     Docker Container
                  |                 |
                  +-------+---------+
                          |
                    Docker Network
