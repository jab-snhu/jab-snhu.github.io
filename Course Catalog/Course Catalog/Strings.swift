// Strings.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

enum Strings {
    static let welcomeMessage = "Welcome to the course catalog!"
    static let goodbyeMessage = "Goodbye!"
}

extension Strings {
    enum Menu {
        static let loadData = "Load data structure"
        static let printCourseList = "Print course list"
        static let printCourse = "Print course"
        static let exit = "Exit"
        static let selectOption = "Select an option: "
        static let invalidOption = "Invalid option. Please try again."
    }
}

extension Strings {
    enum CatalogParser {
        static let fileNotFound = "File not found"
        static let unreadableFile = "Unreadable file"
        static let malformedCourse = "Malformed course"
    }
}
