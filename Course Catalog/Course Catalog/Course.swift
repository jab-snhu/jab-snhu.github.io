// Course.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025


/// A course from the course catalog.
///
/// Each course includes:
/// - `number`: the course number (for example, "CS499")
/// - `title`: the course title
/// - `prerequisites`: the course's prerequisites. This array is left empty if there are none.
public struct Course {
    
    // The course number (for example, "CS499")
    public let number: String
    
    // The course title
    public let title: String
    
    // An array of prerequisite course numbers. Empty if there are none.
    public let prerequisites: [String]
}
