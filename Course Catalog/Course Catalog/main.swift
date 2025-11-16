// main.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025

import Foundation

// Entry point for the Course Catalog program.
// Displays the menu, reads user input, and delegates actions
// to a menu controller.

let menuController = MenuController()
var isRunning = true

print(Strings.welcomeMessage)

while isRunning {
    
    menuController.displayMenu()
    
    // Gets the user input and makes sure it's an integer. Displays an error
    // if not.
    guard let userInput = readLine(), let option = Int(userInput) else {
        print(Strings.Menu.invalidOption)
        continue
    }
    
    // Handle the menu selection, if false is returned, the program exits
    isRunning = menuController.handleSelection(option)
}

