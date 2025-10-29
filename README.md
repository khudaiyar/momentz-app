 Momentz - Share Your Moments
<img width="2461" height="1221" alt="Momentz" src="https://github.com/user-attachments/assets/12dd3de2-3863-4bce-a1e9-d060e8633ab9" />

---

A modern, Instagram-inspired social media platform built from scratch. Share photos, connect with people, and capture life's moments in a clean, distraction-free space.
 Why "Momentz"?
Ever take a photo and think "this moment is perfect"? That's what this is about. I built Momentz because I wanted a social platform that felt real - no algorithms pushing sponsored content, no endless scrolling through stuff you don't care about. Just moments. Real moments from real people.
The "z" instead of "s"? Makes it feel more personal. More unique. More ours.

---

The Story
I started building this around October 2024. I was learning Spring Boot and got tired of following tutorials that built yet another REST API for a bookstore. I wanted something challenging, something I'd actually use.
First week was smooth. Authentication worked, users could register, everything was great. Then I tried to add posts and my database couldn't handle the images. Turns out VARCHAR(255) can't store a 40KB base64 image. Who knew? (Everyone. Everyone knew. I learned the hard way.)
The follow feature took me a whole month to fix. Not kidding. The relationship kept creating circular references. Transactions rolled back randomly. I changed the code 20 times. Finally figured out it was a ManyToMany mapping issue. That fix felt better than my first "Hello World".

---

 Features

User Authentication - Sign up, log in, JWT tokens
Create Posts - Upload photos with captions (drag & drop supported!)
Social Feed - See everyone's posts, like and comment
Follow System - Follow users, track followers/following
User Profiles - Customize bio, profile picture, view posts
User Suggestions - Discover new people
Dark/Light Mode - Toggle themes
Responsive Design - Works on any device

 Built With
Backend:

Spring Boot 3.x (Java 21)
Spring Security + JWT
Spring Data JPA
H2 Database (for now)
Hibernate
Lombok

Frontend:

Vanilla JavaScript (no frameworks!)
HTML5/CSS3
Font Awesome

Why no React? Because sometimes you don't need a framework. I wanted to understand how everything works under the hood.

---

 Project Structure
momentz/
│
├── backend/
│   ├── src/main/java/com/momentz/
│   │   ├── config/              # Security configuration
│   │   ├── controller/          # REST API endpoints
│   │   ├── dto/                 # Data transfer objects
│   │   ├── model/               # Database entities
│   │   ├── repository/          # Database access
│   │   ├── security/            # JWT & authentication
│   │   └── MomentzApplication.java
│   │
│   └── resources/
│       └── application.properties
│
└── frontend/
    ├── index.html               # Login/Register
    ├── home.html                # Main feed
    ├── profile.html             # User profiles
    ├── css/                     # Styles
    ├── js/                      # Frontend logic
    └── images/                  # Logos

---

 What I Learned
Technical Stuff:

Spring Boot architecture
JWT authentication flow
JPA relationships (ManyToMany is tricky!)
REST API design
Database schema design

---

Life Lessons:

@Transactional solves half your database problems
Base64 encoding has size limits (learned the hard way)
Sometimes you need to drop the database and start fresh
Debugging for hours makes you better
Stack Overflow is your best friend

---

Biggest Challenges:

Follow feature - Took a month. ManyToMany mapping was wrong.
Image storage - VARCHAR(255) too small. Changed to TEXT.
Token validation - Fixed localStorage handling in frontend.

---

 Known Issues

Images stored as base64 (large database size)
H2 database resets on restart
No image compression yet
No direct messaging
No notifications

---
 
Future Plans

 Image compression & file storage
 Switch to MySQL
 Direct messaging
 Notifications
 Stories feature
 Video support
 Mobile app

---

Thanks To

Stack Overflow community
Spring Boot documentation
Coffee (lots of it)

---

"First, solve the problem. Then, write the code." – John Johnson﻿# Momentz App




