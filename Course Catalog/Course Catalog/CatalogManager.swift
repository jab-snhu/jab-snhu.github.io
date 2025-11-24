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
    private var courseTree: BinarySearchTree<Course>
    
    /// Creates a new catalog manager object with the provided parser and an empty course tree.
    ///
    /// - Parameter parser: The parser used to load courses. Defaults to a new
    /// instance of `CatalogCSVParser`.
    public init(parser: CatalogParsing = CatalogCSVParser()) {
        self.parser = parser
        self.courseTree = BinarySearchTree<Course>()
    }
    
    /// Loads and stores parsed course objects from the specified file.
    ///
    /// - Parameter fileName: The name of the file to load
    /// - Returns: The number of courses loaded. This result is discardable.
    /// - Throws: Any error encountered while parsing
    @discardableResult
    public func loadCourses(from fileName: String) throws -> Int {
        // empty the tree in case it had been previously loaded
        courseTree.clear()
        
        let courses = try parser.parse(fileName)
        
        courses.forEach {
            courseTree.insert($0)
        }
        
        return courses.count
    }
    
    /// Searches for a course by its course number.
    ///
    /// - Parameter courseNumber: The course number to search for (e.g., "CS499")
    /// - Returns: The found course, or 'nil' if no course was found
    public func searchCourse(by courseNumber: String) -> Course? {
        // create a Course object using just the provided course number for searching
        let searchObject = Course(number: courseNumber, title: "", prerequisites: [])
        
        return courseTree.search(searchObject)
    }
    
    /// Returns `true` if the courseTree has no data, `false`
    /// otherwise
    public var isEmpty: Bool { courseTree.isEmpty }
    
    /// Traverses the courses in the catalog and performs the provided action on each course.
    ///
    /// - Parameter action: The action to perform on each course.
    public func traverseCourses(andPerform action: (Course) -> Void) {
        courseTree.traverseInOrder(andPerform: action)
    }
}
