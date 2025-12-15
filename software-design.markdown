---
layout: default
title: Software Design and Engineering
permalink: /software-design/
---

# Software Design and Engineering

For the Software Design and Engineering category of the Capstone, I enhanced two different artifacts. They are discussed individually below.

---

## Course Catalog CLI Program – C++ to Swift

### Introduction

The first artifact is a course catalog CLI program originally written in C++ for the CS 300 Data Structures and Algorithms: Analysis and Design course. For the capstone project, I rewrote the program in Swift, with a focus on improving the architecture and maintaining its efficiency. The purpose of the program is to load a course catalog from a provided CSV file and allow the user to print the parsed course list or search for an individual course. The current Swift implementation provides a new program architecture and can load and create `Course` objects from the provided CSV file. Displaying the course list and searching for an individual course using a binary search tree was implemented for the Data Structures and Algorithms category to create a fully functional application.

**Artifact Links:**
- [Original C++ Implementation](https://github.com/jab-snhu/jab-snhu.github.io/tree/main/CS300%20-%20Final%20Project)
- [Enhanced Swift Implementation](https://github.com/jab-snhu/jab-snhu.github.io/tree/main/Course%20Catalog)

### Justification

I selected the course catalog for the Software Design and Engineering category because it provided the opportunity to take a less structured implementation and apply a more professional program architecture to it, focused on code design, modularity, and extensibility. The intention is to showcase my ability to think beyond simple feature implementation and consider the long-term impact of code decisions.

Rewriting this program involved refactoring the code into more modular architectural components, each responsible for a specific task. The new `MenuController` type handles displaying the menu options to the user and handling their selections. A `CatalogManager` class was created to handle loading and managing course data. For parsing the course data, I created a `CatalogCSVParser` class, which implements the `CatalogParsing` protocol. This abstraction means the code is set up to easily implement parsers for other file types in the future. The catalog manager owns a `CatalogParsing` object, so it is not limited to using the CSV-specific parser.

The code has descriptive DocC comments throughout, making it easy to create and access readable documentation, and is organized using extensions and code markers following Swift conventions. The aim of these enhancements is to showcase thoughtful engineering design decisions and to create a cleaner program that is easier to maintain or update in the future.

### Course Outcomes

The enhancements for the course catalog artifact address course outcomes 1-4 in the following ways:

**Outcome 1 – Collaboration:**
The modular nature of the enhanced architecture helps build a collaborative environment for developers of all skill levels by making code components individually testable, replaceable, and reusable.

**Outcome 2 – Communication:**
The new codebase includes professional documentation using DocC comments standard in Swift development, making it easy to see code definitions across the app. Class, property, and function naming is kept simple and descriptive throughout to enhance readability.

**Outcome 3 – Standards and Practices:**
The new program architecture was designed with modularity and reusability in mind following common professional standards of software development. Each class has a well-defined scope of responsibilities with limited coupling throughout.

**Outcome 4 – Innovative Techniques:**
The port from C++ to Swift showcases my ability to use new tools and languages beyond those taught in the program. I focused on including techniques appropriate for Swift, such as DocC comments, protocol-oriented design, and creating namespaces with enums for code clarity.

### Reflection

The structural enhancements for this artifact forced me to think first about how the code should be implemented to maximize extensibility, testability, and modularity. Even though I was porting an existing project, the structure is now totally different, and this exercise underscored the power of abstraction. For example, the final implementation for this capstone will only use an included CSV file for the course catalog, but adding a parsing protocol now will make it much simpler to add support for other file types in the future without requiring a major refactor.

The main challenge faced was learning the process of creating a command-line program in Xcode. I have never done that before and encountered some initial issues trying to get the code to determine the correct file path to access files within the executable's directory.

---

## Event Tracker – Authentication Enhancement

### Introduction

The second artifact I enhanced for the Software Engineering and Design category is the Event Tracker Android app created for the CS 360 Mobile Architecture and Programming course. The purpose of the program is to allow users to create and manage a list of upcoming events. The original implementation features a rudimentary authentication system using a local Room database to compare unencrypted usernames and passwords to log in. This enhancement replaced that implementation with a cloud-based, secure authentication system from Firebase featuring encrypted communication and storage, removing the security vulnerabilities of the original app.

**Artifact Links:**
- [Original Android Implementation](https://github.com/jab-snhu/jab-snhu.github.io/tree/main/CS360%20-%20Final%20Project)
- [Enhanced Android Implementation](https://github.com/jab-snhu/jab-snhu.github.io/tree/main/Event%20Tracker)

### Justification

The planned authentication enhancement for the Event Tracker Android app allowed me to demonstrate professional software design practices while addressing a critical security vulnerability. Instead of simply swapping out the authentication mechanism, I designed an abstraction layer using the `AuthManager` interface. This decouples the overall application from the Firebase-specific authentication implementation, making it easier to adopt other authentication methods in the future without modifying any view models or activities. The `FirebaseAuthManager` class implements this interface, but the rest of the app remains generally unaware of (and unopinionated about) Firebase itself.

This update addresses a critical security vulnerability in the original implementation by removing the unencrypted storage of user passwords in a local database. The integration with Firebase Authentication delegates all password handling to Firebase's encrypted services and user credentials are no longer stored locally. The `UserSessionManager` class was updated to use an `AuthManager` to retrieve authentication status, making sure there is a single source of truth for a user's login status.

This integration required creating a Firebase account for the project, adding the SDK as a dependency, and designing asynchronous callback-based APIs to handle the authentication operations. To accomplish this, I created an `AuthCallback` interface with success and failure methods allowing the view model layer to handle the business logic away from the UI layer, maintaining the app's MVVM architecture and adhering to best programming practices surrounding separation of concerns.

### Course Outcomes

The enhancements for the Event Tracker artifact address course outcomes 2, 4, and 5 in the following ways:

**Outcome 2 – Communication:**
The enhancement showcases professional communication by using JavaDoc documentation comments for all public methods, clear interface design, and informative error messages to inform users of encountered issues.

**Outcome 4 – Innovative Techniques and Tools:**
The integration with the Firebase SDK demonstrates use of modern, industry-standard tools. The interface-based code design, asynchronous callbacks, and MVVM architecture show an ability to refactor an existing codebase while maintaining its overall structure.

**Outcome 5 – Security:**
This enhancement was performed with the primary goal of eliminating the security vulnerability introduced in the original app by storing unencrypted passwords in a local database. Integrating with Firebase Authentication gives the app a secure, cloud-based login system.

### Reflection

The most significant learning from adding the authentication enhancement to the Event Tracker app came from understanding how to integrate a third-party SDK and how to make sure that integration is sufficiently decoupled from the rest of the app. The integration involved learning how to import a new dependency to an Android project, create and manage a Firebase account for an app, and parse Firebase documentation to learn how to perform authentication operations with their APIs. Similar to the course catalog program, adding the interface-based `AuthManager` abstraction allowed me to perform the integration in a single place, making it easier to replace in the future and weakening the app's dependency on a third party. Additionally, working with the asynchronous APIs deepened my understanding of the relationship between frontend and backend communication.

The biggest challenge I faced with this integration was how much I needed to update across the original implementation to accommodate Firebase requirements. This work centered around using email addresses for Firebase login instead of simple usernames and updating the code to use Firebase's `String`-based user IDs instead of the `long`-based IDs of the original implementation. Loading events from the local database was done by using the user ID as the key for event lookup. Since I completely updated the database to use Firebase Firestore during the Databases category for this capstone project, I ended up disabling event look-up temporarily to avoid spending time on throwaway work to make the current database work with the new user ID format.
