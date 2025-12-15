---
layout: default
title: Algorithms and Data Structures
permalink: /algorithms/
---

# Algorithms and Data Structures

## Course Catalog CLI Program – C++ to Swift

### Introduction

For the Algorithms and Data Structures category, I finalized the port of the course catalog CLI program from C++ to Swift that I began with the enhancements in the Software Engineering and Design category. The artifact was originally written for the CS 300 Data Structures and Algorithms: Analysis and Design course. The purpose of the program is to load a course catalog from a provided CSV file and allow the user to print the parsed course list or search for an individual course. All functionality is now complete with the services using a binary search tree for the underlying data layer.

**Artifact Links:**
- [Original C++ Implementation](https://github.com/jab-snhu/jab-snhu.github.io/tree/main/CS300%20-%20Final%20Project)
- [Enhanced Swift Implementation](https://github.com/jab-snhu/jab-snhu.github.io/tree/main/Course%20Catalog)

### Justification

The course catalog artifact was selected because implementing a binary search tree in a new language demonstrates mastery of both the fundamentals of the data structure and its search and traversal algorithms. Swift does not contain a native binary search tree in its libraries, so the data structure and its algorithms were completely written from scratch. As someone interested in continuing my career in mobile development, I wanted to add this Swift project to my portfolio alongside the Event Tracker Android app I am also enhancing for this capstone project. This will form a clear picture of a developer capable of working in both the Android and iOS ecosystems.

This enhancement also allowed me to explore Swift generics. In the original C++ code, the data structure is tightly coupled with the `Course` object and only works with data of that type. I wanted to make sure the data structure I created for this enhancement was reusable in future projects, so I opted to use Swift's generic type (`<Element: Comparable>`), which means that any type that conforms to the `Comparable` protocol can be used with the binary search tree. The `Comparable` protocol allows objects to define how less than and equality comparisons are performed and is a requirement for the BST's search and sort algorithms. The insert, search, and in-order traversal algorithms are built recursively and maintain their efficiency, averaging O(log n) time.

### Course Outcomes

The enhancements for the course catalog artifact address course outcomes 3 and 4 in the following ways:

**Outcome 3 – Algorithmic Principles / Computer Science Standards and Practices:**
The Swift `BinarySearchTree` object retains all the efficiency in its algorithms and operations that exist in the C++ implementation. The enhanced use of generics makes the code more modular and reusable in future projects.

**Outcome 4 – Innovative Techniques:**
The port from C++ to Swift showcases my ability to use new tools and languages beyond those taught in the program. I focused on including techniques appropriate for Swift, such as DocC comments and programming with generics.

### Reflection

The most significant learning for this enhancement came from understanding Swift's generic type system. In combination with a protocol-oriented approach, Swift generics enforce protocol conformance at compile time. With the `BinarySearchTree<Element: Comparable>` object, types that do not conform to the `Comparable` protocol cannot be used with the BST. This is strictly enforced and attempting to use it with a non-conformant type would cause compilation to fail. It took a while to wrap my head around the concept, but I believe the generic implementation combined with the recursive nature of the structure's algorithms provides a mature and refined sample for my portfolio.

I also spent time comprehensively testing the program for edge cases and providing appropriate messages to the user any time an error is encountered. This included putting guards against trying to print a course's info or the whole course list before the course data has been loaded, making sure courses were ordered correctly, and informing the user if a course they search for can't be found. Testing validated that in-order traversal produces a correctly sorted output regardless of insertion order, and the search algorithm correctly returns `nil` if an object cannot be found.

Overall, this was a very valuable exercise. It allowed me to focus on learning and showcasing some of Swift's more advanced capabilities, while still being mindful of correctly implementing the binary search tree data structure and its algorithms.
