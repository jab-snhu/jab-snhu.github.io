// CatalogParsing.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

/// Protocol defining an object capable of loading `Course` objects from a file.
public protocol CatalogParsing {
    /// Parses a file with the provided name into an array of `Course` objects.
    ///
    /// - Important: The file must exist in the same directory as the program executable.
    /// - Parameter fileName: The name of the file to parse.
    /// - Returns: An array of parsed `Course` objects.
    /// - Throws: An `Error` if errors are encountered while loading or parsing the file.
    func parse(_ filename: String) throws -> [Course]
}
