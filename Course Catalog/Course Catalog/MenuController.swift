// MenuConroller.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

import Foundation

/// Presents the menu and handles user menu selections.
public struct MenuController {
    private let catalogManager: CatalogManager
    
    
    /// Creates a new menu controller.
    ///
    /// - Parameter catalogManager: The `CatalogManager` used to load and query course
    /// data. Defaults to a new instance.
    init(catalogManager: CatalogManager = .init()) {
        self.catalogManager = catalogManager
    }
    
    
    /// Displays the command-line menu to the user with the available `MenuOption`s
    public func displayMenu() {
        print("")
        MenuOption.allCases.forEach { print($0.description) }
        print("")
        print(Strings.Menu.selectOption, terminator: "")
    }
    
    
    /// Handles the selected menu option.
    ///
    /// - Parameter selection: The number associated with the selected option.
    /// - Returns: `true` for all handled or invalid options. `false` when the user
    /// selects the number matching `MenuOption.exit`
    public func handleSelection(_ selection: Int) -> Bool {
        
        // If the user selects an invalid option, display an error message and return
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
            // TODO: Implement course list output once the binary search tree is added
            print("You chose print course list")
            return true
        case .printCourse:
            // TODO: Implement course lookup and print output once the binary search tree is added
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
    
    /// Represents an available menu option for the program.
    ///
    /// The raw integer value corresponds to the expected input from the user.
    fileprivate enum MenuOption: Int, CaseIterable, CustomStringConvertible {
        case loadData = 1
        case printCourseList
        case printCourse
        case exit = 9
        
        
        /// A formatted description of the option, suitable for display in the menu
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
