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
            handleLoadData()
            return true
        case .printCourseList:
            handlePrintCourseList()
            return true
        case .printCourse:
            handlePrintCourse()
            return true
        case .exit:
            print(Strings.goodbyeMessage)
            return false
        }
    }
}

// MARK: Menu Actions

extension MenuController {
    
    /// Loads the data from the CourseCatalog.csv file and
    /// prints confirmation the data has loaded or an error
    /// if an issue was encountered.
    private func handleLoadData() {
        do {
            let numberOfCourses = try catalogManager.loadCourses(from: "CourseCatalog.csv")
            print("\(Strings.Menu.numberOfCoursesLoaded) \(numberOfCourses)")
        } catch {
            print("\(Strings.Menu.errorLoadingCatalog) \(error)")
        }
    }
    
    /// Prints the list of courses from the catalog
    private func handlePrintCourseList() {
        // make sure there are courses available to print
        guard !catalogManager.isEmpty else {
            print(Strings.Menu.emptyCourseCatalog)
            return
        }
        
        print(Strings.Menu.availableCourses)
        
        catalogManager.traverseCourses(andPerform: {
            print($0.shortDescription)
        })
        
        print("")
    }
    
    /// Asks the user to input a course number and prints the
    /// full course description.
    private func handlePrintCourse() {
        // make sure there are courses loaded in the catalog
        guard !catalogManager.isEmpty else {
            print(Strings.Menu.emptyCourseCatalog)
            return
        }
        
        print("\(Strings.Menu.enterCourseNumber)", terminator: "")
        
        guard let input = readLine()?.uppercased(), !input.isEmpty else {
            print(Strings.Menu.invalidCourseNumber)
            return
        }
        
        guard let course = catalogManager.searchCourse(by: input) else {
            print(Strings.Menu.courseNotFound)
            return
        }
        
        print("\n\(course.fullDescription)")
        print("")
    }
}

// MARK: - MenuOption

extension MenuController {
    /// Represents an available menu option for the program.
    ///
    /// The raw integer value corresponds to the expected input from the user.
    private enum MenuOption: Int, CaseIterable, CustomStringConvertible {
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
