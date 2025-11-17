// CatalogManager.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

import Foundation


/// Manages loading and storing a course catalog from an external file.
///
/// Uses an internal `CatalogParsing` to load courses from a file.
public final class CatalogManager {
    private let parser: CatalogParsing
    private var courses: [Course] = []
    
    /// Creates a new catalog manager object with the provided parser.
    ///
    /// - Parameter parser: The parser used to load courses. Defaults to a new
    /// instance of `CatalogCSVParser`.
    public init(parser: CatalogParsing = CatalogCSVParser()) {
        self.parser = parser
    }
    
    /// Loads and stores parsed course objects from the specified file.
    ///
    /// - Parameter fileName: The name of the file to load
    /// - Returns: The number of courses loaded. This result is discardable.
    /// - Throws: Any error encountered while parsing
    @discardableResult
    public func loadCourses(from fileName: String) throws -> Int {
        courses = try parser.parse(fileName)
        
        return courses.count
    }
}
