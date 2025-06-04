JOZEL M. VIERNES
APRIL ROSE B. VILLEDO
AP3C


# 🎓 Student Scholarship Management System

A Java-based desktop application for managing student information and scholarship assignments. Built using **Java Swing** for the UI and **MySQL** for the backend database.

---

## 📌 Features

- View and edit student profiles
- Upload student documents (birth certificate, COE, 2x2 ID)
- Assign scholarships to students 
- Automatically update scholarship descriptions
- Search and update student records
- File handling with database linking

---

## 🛠️ Tech Stack

- **Language:** Java 
- **GUI Framework:** Java Swing
- **Database:** MySQL
- **Build Tool:**NetBeans

---

Database Setup
To run this project, you need to create a MySQL database and set up the necessary tables. Follow these steps:

Create the Database
Log in to your MySQL server and run:
CREATE DATABASE scholarship_db;
or
Replace it with the name you want for your database.

Create the Tables
Run the following SQL commands to create the required tables:

CREATE TABLE scholarships (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) DEFAULT NULL,
    description TEXT DEFAULT NULL,
    type VARCHAR(50) DEFAULT NULL,
    start_date DATE DEFAULT NULL,
    deadline DATE DEFAULT NULL,
    student_needed INT(11) DEFAULT NULL,
    contact_person VARCHAR(100) DEFAULT NULL
);

CREATE TABLE admins (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL
);

CREATE TABLE students (
    student_id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gpa DECIMAL(3,2) DEFAULT NULL,
    course VARCHAR(150) NOT NULL,
    year_level INT(11) NOT NULL,
    birth_certificate VARCHAR(255) DEFAULT NULL,
    certificate_of_enrollment VARCHAR(255) DEFAULT NULL,
    two_x_two_id VARCHAR(255) DEFAULT NULL,
    father_full_name VARCHAR(100) DEFAULT NULL,
    father_occupation VARCHAR(100) DEFAULT NULL,
    father_income DECIMAL(10,2) DEFAULT NULL,
    mother_full_name VARCHAR(100) DEFAULT NULL,
    mother_occupation VARCHAR(100) DEFAULT NULL,
    mother_income DECIMAL(10,2) DEFAULT NULL,
    scholarship_id INT(11) DEFAULT NULL,
    contact_person VARCHAR(100) DEFAULT NULL,
    email VARCHAR(100) DEFAULT NULL,
    phone_number VARCHAR(15) DEFAULT NULL,
    address TEXT DEFAULT NULL,
    FOREIGN KEY (scholarship_id) REFERENCES scholarships(id)
);

Update Application Configuration
Make sure to configure your application’s database connection settings to use your database name, username, and password.
