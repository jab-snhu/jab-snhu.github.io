// 

import Foundation

let menuController = MenuController()
var isRunning = true

print(Strings.welcomeMessage)

while isRunning {
    
    menuController.displayMenu()
    
    guard let userInput = readLine(), let option = Int(userInput) else {
        print(Strings.Menu.invalidOption)
        continue
    }
    
    isRunning = menuController.handleSelection(option)
}

