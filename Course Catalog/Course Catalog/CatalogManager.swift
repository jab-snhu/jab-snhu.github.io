// CatalogManager.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

import Foundation


/// Manages loading and storing a course catalog from a CSV.
///
/// Uses an internal `CatalogCSVParser` to load courses from a CSV file.
public final class CatalogManager {
    private let csvParser: CatalogCSVParser
    private var courses: [Course] = []
    
    
    /// Creates a new catalog manager object with the provided parser.
    ///
    /// - Parameter parser: The CSV parser used to load courses. Defaults to a new
    /// instance of `CatalogCSVParser`.
    public init(parser: CatalogCSVParser = .init()) {
        self.csvParser = parser
    }
    
    
    /// Loads and stores parsed course objects from the specified file.
    ///
    /// - Parameter fileName: The name of the CSV file to load
    /// - Returns: The number of courses loaded. This result is discardable.
    /// - Throws: Any error encountered while parsing
    @discardableResult
    public func loadCourses(from fileName: String) throws -> Int {
        courses = try csvParser.parse(fileName)
        
        return courses.count
    }
}
