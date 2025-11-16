// CatalogCSVParser.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

import Foundation

/// Parses a provided course catalog CSV file into an array of `Course` objects.
///
/// - Important: The required format for each line of the CSV is:
/// COURSE_NUMBER, COURSE_TITLE, PREREQ_1, PREREQ_2, ....
///
/// - Note: The first field is treated as the course number, the second as the course
/// title, and any remaining are treated as prereq courses. Empty lines are ignored.
public struct CatalogCSVParser {
    
    /// Creates a new CatalogCSVParser instance.
    public init() { }
    
    /// Parses a CSV file with the provided name into an array of `Course` objects.
    ///
    /// - Important: The file must exist in the same directory as the program executable.
    /// - Parameter fileName: The name of the CSV file to parse
    /// - Returns: An array of parsed `Course` objects
    /// - Throws: A `ParsingError` if the file is not found, cannot be read, or contains a malformed line
    public func parse(_ fileName: String) throws -> [Course] {
        
        // get the correct fileURL, file must be in the same directory as the executable
        let baseURL = URL(fileURLWithPath: CommandLine.arguments[0]).deletingLastPathComponent()
        let fileURL = baseURL.appendingPathComponent(fileName)
        
        // check to make sure the file exists, otherwise throw an error
        guard FileManager.default.fileExists(atPath: fileURL.path) else {
            throw ParsingError.fileNotFound(fileName)
        }
        
        // read and parse the file or throw an error if unreadable
        do {
            let csvText = try String(contentsOfFile: fileURL.path, encoding: .utf8)
            let csvLines = csvText.components(separatedBy: .newlines).filter { !$0.isEmpty }
        
            var courses: [Course] = []
            
            for line in csvLines {
                let parsedCourse = try parseCourseFrom(line)
                courses.append(parsedCourse)
            }
            
            return courses
        } catch {
            throw ParsingError.unreadable(fileName)
        }
    }
    
    
    /// Parses a single line of CSV text into a `Course` object.
    ///
    /// - Important: The required format of the provided line is:
    /// COURSE_NUMBER, COURSE_TITLE, PREREQ_1, PREREQ_2, ....
    ///
    /// - Parameter line: the line of csv text to parse
    /// - Returns: the `Course` created from the parsed line
    /// - Throws: Throws a `ParsingError.malformedCourse` if the line is not the
    /// proper format or does not contain at least a course number and title
    private func parseCourseFrom(_ line: String) throws -> Course {
        let components = line.components(separatedBy: ",")
        
        guard components.count >= 2 else {
            throw ParsingError.malformedCourse(line)
        }
        
        let courseNumber = components[0]
        let courseTitle = components[1]
        let coursePrereqs = components.count > 2 ? components.dropFirst(2).filter { !$0.isEmpty } : []
        
        return Course(number: courseNumber, title: courseTitle, prerequisites: Array(coursePrereqs))
    }
}

// MARK: - ParsingError
extension CatalogCSVParser {
    
    /// Possible errors that can occur while parsing a course catalog CSV file.
    ///  
    /// Contains errors for the following cases: fileNotFound, unreadable, and malformedCourse.
    public enum ParsingError: Error, CustomStringConvertible {
        
        /// The CSV file could not be found
        case fileNotFound(String)
        
        /// The CSV file could not be read as a string
        case unreadable(String)
        
        /// The CSV text did not match the expected format
        case malformedCourse(String)
        
        
        /// A description of the parsing error
        public var description: String {
            switch self {
            case .fileNotFound(let path):
                return "\(Strings.CatalogParser.fileNotFound): \(path)"
            case .unreadable(let path):
                return "\(Strings.CatalogParser.unreadableFile): \(path)"
            case .malformedCourse(let courseText):
                return "\(Strings.CatalogParser.malformedCourse): \(courseText)"
            }
        }
    }
}
