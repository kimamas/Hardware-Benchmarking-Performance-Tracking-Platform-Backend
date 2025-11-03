# Hardware Benchmarking & Performance Tracking Platform – Backend

This backend service powers a platform for benchmarking and comparing computer hardware performance, including **CPUs**, **GPUs**, and **RAM**.  
It provides REST APIs to collect, store, and analyze benchmark results, allowing users to upload and compare their system performance with others.

Developed as part of a Fontys University project to explore **software architecture, data engineering, and system optimization** using Java proigramming language.

---

## Project Structure

| Component | Description | Technologies |
|------------|-------------|---------------|
| **Backend** | RESTful API providing data management, benchmark result storage, authentication, and analysis logic. | Java 17, Spring Boot 3, Gradle, SQL Server/H2, JUnit, SonarQube |
| **Frontend** | Web interface for users to browse hardware data, compare results, and upload benchmarks. | React, JavaScript, HTML/CSS |
| **Benchmarking App** | Desktop client for running hardware tests and submitting performance results to the backend. | C# .NET 8, WinForms |

---

## Features

- Benchmark CPU, GPU, and RAM performance through the WinForms app.  
- Upload results and view analytics via the React web platform.  
- Centralized backend for data aggregation, filtering, and comparison.  
- CRUD operations for hardware components, users, and benchmark results.  
- Authentication and role-based access control (optional).  
- Integrated **CI/CD pipeline** with **SonarQube** quality analysis.  
- **Modular architecture** designed for scalability and maintainability.

---

## Tech Stack

**Backend:** Java 17 · Spring Boot · Gradle · SQL Server · H2 · JUnit · SonarQube  
**Frontend:** React · JavaScript · HTML · CSS  
**Desktop:** C# · .NET 8 · WinForms  
**DevOps:** GitLab CI/CD · Docker · SonarQube · Jira  

---

## Getting Started 

1. Add viriables for the sonarqube: SONAR_HOST_URL and SONAR_TOKEN;
2. Change application-sample.properties to include your own data;
