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
    
    /// The course number (for example, "CS499")
    public let number: String
    
    /// The course title
    public let title: String
    
    /// An array of prerequisite course numbers. Empty if there are none.
    public let prerequisites: [String]
    
    /// The short description for the course
    ///
    /// - Returns: `String` formatted as `number`: `title`.
    public var shortDescription: String {
        "\(number): \(title)"
    }
    
    /// The full description for the course
    ///
    /// - Returns: A multiline `String` formatted with the
    /// `shortDescription` followed by the list of prerequisites
    public var fullDescription: String {
        let prerequisitesString = prerequisites.isEmpty ? Strings.Menu.noPrerequisites : prerequisites.joined(separator: ", ")
        
        return """
        \(shortDescription)
        \(Strings.Menu.prerequisites) \(prerequisitesString)
        """
    }
}

// MARK: - Comparable

extension Course: Comparable {
    
    /// Compares two courses based on their course number.
    ///
    /// - Parameters:
    ///   - lhs: The left-hand-side Course in the comparison.
    ///   - rhs: The right-hand-side Course in the comparison.
    /// - Returns: `true` if the course number for `lhs` is less
    /// than the course number for `rhs`, `false` otherwise.
    public static func < (lhs: Course, rhs: Course) -> Bool {
        return lhs.number < rhs.number
    }
    
    
    /// Compares equality of two courses based on their course number.
    ///
    /// - Parameters:
    ///   - lhs: The left-hand-side Course in the comparison.
    ///   - rhs: The right-hand-side Course in the comparison.
    /// - Returns: `true` if the course number for `lhs` is equal
    /// to the course number for `rhs`, `false` otherwise
    public static func == (lhs: Course, rhs: Course) -> Bool {
        return lhs.number == rhs.number
    }
}
