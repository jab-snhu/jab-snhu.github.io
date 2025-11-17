// Strings.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025


/// Organized namespace for strings used throughout the program.
enum Strings {
    static let welcomeMessage = "Welcome to the course catalog!"
    static let goodbyeMessage = "Goodbye!"
}

// MARK: - Menu Strings

extension Strings {
    /// Strings used for menu options and interactions.
    enum Menu {
        static let loadData = "Load data structure"
        static let printCourseList = "Print course list"
        static let printCourse = "Print course"
        static let exit = "Exit"
        static let selectOption = "Select an option: "
        static let invalidOption = "Invalid option. Please try again."
    }
}

// MARK: Catalog Parsing Strings
extension Strings {
    /// Strings used for catalog parsing operations.
    enum CatalogParser {
        static let fileNotFound = "File not found"
        static let unreadableFile = "Unreadable file"
        static let malformedCourse = "Malformed course"
    }
}
