//

import Foundation

public final class CatalogManager {
    private let parser: CatalogCSVParser
    private var courses: [Course] = []
    
    public init(parser: CatalogCSVParser = .init()) {
        self.parser = parser
    }
    
    @discardableResult
    public func loadCourses(from fileName: String) throws -> Int {
        courses = try parser.parse(fileName)
        
        return courses.count
    }
    
    
}
