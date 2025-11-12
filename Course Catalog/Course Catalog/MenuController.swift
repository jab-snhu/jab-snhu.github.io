// 

import Foundation

public struct MenuController {
    let catalogManager: CatalogManager
    
    init(catalogManager: CatalogManager = .init()) {
        self.catalogManager = catalogManager
    }
    
    public func displayMenu() {
        print("")
        MenuOption.allCases.forEach { print($0.description) }
        print("")
        print(Strings.Menu.selectOption, terminator: "")
    }
    
    public func handleSelection(_ selection: Int) -> Bool {
        
        guard let selectedOption = MenuOption(rawValue: selection) else {
            print(Strings.Menu.invalidOption)
            return true
        }
        
        switch selectedOption {
        case .loadData:
            do {
                let numberOfCourses = try catalogManager.loadCourses(from: "CourseCatalog.csv")
                print("Loaded \(numberOfCourses) courses")
            } catch {
                print("Error loading catalog: \(error)")
            }
            return true
        case .printCourseList:
            print("You chose print course list")
            return true
        case .printCourse:
            print("You chose print course")
            return true
        case .exit:
            print(Strings.goodbyeMessage)
            return false
        }
    }
}

//MARK: - MenuOption

extension MenuController {
    fileprivate enum MenuOption: Int, CaseIterable, CustomStringConvertible {
        case loadData = 1
        case printCourseList
        case printCourse
        case exit = 9
        
        var description: String {
            switch self {
            case .loadData: return "\(rawValue). \(Strings.Menu.loadData)"
            case .printCourseList: return "\(rawValue). \(Strings.Menu.printCourseList)"
            case .printCourse: return "\(rawValue). \(Strings.Menu.printCourse)"
            case .exit: return "\(rawValue). \(Strings.Menu.exit)"
            }
        }
    }
}
