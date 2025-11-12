//

import Foundation

public struct CatalogCSVParser {
    public init() { }
    
    public func parse(_ fileName: String) throws -> [Course] {
        let baseURL = URL(fileURLWithPath: CommandLine.arguments[0]).deletingLastPathComponent()
        let fileURL = baseURL.appendingPathComponent(fileName)
        
        guard FileManager.default.fileExists(atPath: fileURL.path) else {
            throw ParsingError.fileNotFound(fileName)
        }
        
        do {
            let csvText = try String(contentsOfFile: fileURL.path, encoding: .utf8)
            
            let csvLines = csvText.components(separatedBy: .newlines).filter { !$0.isEmpty }
            
            var courses: [Course] = []
            
            for line in csvLines {
                let components = line.components(separatedBy: ",")
                
                guard components.count >= 2 else {
                    throw ParsingError.malformedCourse(line)
                }
                
                let courseNumber = components[0]
                let courseTitle = components[1]
                
                let coursePrereqs = components.count > 2 ? components.dropFirst(2).filter { !$0.isEmpty } : []
                
                let parsedCourse = Course(number: courseNumber, title: courseTitle, prerequisites: Array(coursePrereqs))
                
                courses.append(parsedCourse)
            }
            
            return courses
            
        } catch {
            throw ParsingError.unreadable(fileName)
        }
    }
}

//MARK: - ParsingError
extension CatalogCSVParser {
    public enum ParsingError: Error, CustomStringConvertible {
        case fileNotFound(String)
        case unreadable(String)
        case malformedCourse(String)
        
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
