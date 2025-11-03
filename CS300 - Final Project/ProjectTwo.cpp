//============================================================================
// Name        : ProjectTwo.cpp
// Author      : Jeff Blagg
// Version     : 1.0
// Class       : CS 300
// Description : Assignment 7-2 - Project Two
//============================================================================

#include <cctype>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

using namespace std;

string toUpper(string text);
string trimTrailingCarriageReturn(const string& str);

//============================================================================
// MENU_OPTION Definition
//============================================================================

/**
 * Enum to represent the different menu options to load data, print the course
 * list, print an individual course, or exit the program
 */
enum MENU_OPTION {
    LOAD_DATA = 1,
    PRINT_COURSE_LIST = 2,
    PRINT_COURSE = 3,
    EXIT = 9
};

//============================================================================
// Course Definition
//============================================================================

/**
 * Structure for holding course information, including courseNumber, title,
 * and prerequisites. Full course information can be printed using the
 * printInfo method.
 */
struct Course {
    string courseNumber;
    string title;
    vector<string> prerequisites;

    /**
     * Prints the full course information including prerequisites.
     */
    void printInfo() {
        cout << courseNumber << ", " << title << endl;
        cout << "Prerequisites: ";

        // check for prereqs
        if (prerequisites.empty()) {
            // if none, output "None"
            cout << "None";
        } else {
            // loop through the prereqs and output them
            for (size_t i = 0; i < prerequisites.size(); ++i) {
                cout << prerequisites[i];
                // no comma for the last one
                if (i < prerequisites.size() - 1) {
                    cout << ", ";
                }
            }
        }
    }
};

//============================================================================
// Node Definition
//============================================================================

/**
 * Internal structure for a CourseTree Node
 */
struct Node {
    Course course;
    Node* left;
    Node* right;

    // default constructor
    Node() {
        this->left = nullptr;
        this->right = nullptr;
    }

    // initialize with a course
    explicit Node(const Course& course) : Node() {
        this->course = course;
    }
};

//============================================================================
// CourseTree Definition
//============================================================================

class CourseTree final {

private:
    Node* root;

    void addNode(Node* node, Course course);
    void inOrder(Node* node);
    void deleteNode(Node* node);

public:
    CourseTree();
    virtual ~CourseTree();

    void Insert(Course course);
    void InOrder();
    Course Search(string query);
    void Clear();
    bool empty() const;
};

//============================================================================
// CourseTree Implementation
//============================================================================

/**
 * Default constructor
 */
CourseTree::CourseTree() {
    // root is equal to nullptr
    this->root = nullptr;
}

/**
 * Destructor
 */
CourseTree::~CourseTree() {
    // recurse from root deleting every node
    deleteNode(this->root);
}

/**
 * Insert a course.
 *
 * @param course the course to insert
 */
void CourseTree::Insert(Course course) {
    // if root is equal to null pointer
    if (this->root == nullptr) {
        // set root equal to new course node
        this->root = new Node(course);
    } else {
        // add node using root and course
        this->addNode(this->root, course);
    }
}

/**
 * Traverse the tree in order
 */
void CourseTree::InOrder() {
    // call inOrder using root
    this->inOrder(this->root);
}

/**
 * Search for a course.
 *
 * @param query the course number to search for
 * @return the found course. If no course is found, returns
 * an empty course.
 */
Course CourseTree::Search(string query) {
    // set current node equal to root
    Node* currentNode = this->root;

    // loop downward until bottom reached or matching courseNumber is found
    while (currentNode != nullptr) {
        // if match found, return current course
        if (currentNode->course.courseNumber == query) {
            return currentNode->course;
        }

        // if query is smaller than the current node's course number then
        // traverse left
        if (currentNode->course.courseNumber > query) {
            currentNode = currentNode->left;
        } else {
            // else traverse right
            currentNode = currentNode->right;
        }
    }

    // if no course is found, return an empty course
    Course course;
    return course;
}

/**
 * Deletes all existing nodes from the tree
 */
void CourseTree::Clear() {
    deleteNode(this->root);
    this->root = nullptr;
}

/**
 * Add a node with the provided course to the tree. (recursive)
 *
 * @param node the current node in the tree
 * @param course the course to be added
 */
void CourseTree::addNode(Node* node, Course course) {
    // if node's courseNumber is larger than course's courseNumber,
    // add to left
    if (node->course.courseNumber > course.courseNumber) {
        // if no left node
        if (node->left == nullptr) {
            // add new node to left with course
            node->left = new Node(course);
        } else {
            // otherwise recurse down the left node
            this->addNode(node->left, course);
        }
    } else {
        // if no right node
        if (node->right == nullptr) {
            // add new node to right with course
            node->right = new Node(course);
        } else {
            // otherwise recurse down the right node
            this->addNode(node->right, course);
        }
    }
}

/**
 * Traverses the tree stemming from the given node in order
 * and displays each node's course information. (recursive)
 *
 * @param node The current node. Returns immediately if node is a
 * null pointer.
 */
void CourseTree::inOrder(Node* node) {
    // if node is not equal to null pointer
    if (node != nullptr) {
        // inOrder left
        this->inOrder(node->left);

        // output courseNumber, title
        cout << node->course.courseNumber << ", "
             << node->course.title << endl;

        // inOrder right
        this->inOrder(node->right);
    }
}

/**
 * Deletes all nodes in the tree stemming from the given node
 *
 * @param node The root of the tree to delete. Returns immediately if the
 * node is a null pointer.
 */
void CourseTree::deleteNode(Node* node) {
    if (node != nullptr) {
        this->deleteNode(node->left);
        this->deleteNode(node->right);
        delete node;
    }
}

/**
 * Checks if the course tree is empty or not.
 *
 * @return true if empty, false if not.
 */
bool CourseTree::empty() const {
    return this->root == nullptr;
}

//============================================================================
// Utility Helper Methods
//============================================================================

/**
 * Returns the provided text with all characters converted
 * to upper case.
 *
 * @param text the text to upper case
 * @return upper case string
 */
string toUpper(string text) {
    // make every character upper case
    for (char& ch : text) {
        ch = static_cast<char>(toupper(ch));
    }

    return text;
}

/**
 * Removes trailing carriage returns from the provided string
 *
 * @param str The string from which to remove trailing carriage returns
 * @return A new string with trailing carriage returns removed. If the provided
 * string contains only carriage returns, returns an empty string.
 */
string trimTrailingCarriageReturn(const string& str) {
    // Find the position of the last non-carriage return character
    const size_t end = str.find_last_not_of("\r");

    // Return an empty string if there were only carriage returns found
    if (end == string::npos) return "";

    // Return a substring from the beginning of the string to the last
    // valid character
    return str.substr(0, end + 1);
}

//============================================================================
// Course Loading Methods
//============================================================================

/**
 * Parse a vector of course information into a Course object
 *
 * @param courseInfo the course information to parse. Expectation is that
 * courseNumber is at index 0, course title is at index 1, and course
 * prerequisites (if any) are in indexes 2 and higher.
 * @return a Course object with the provided data. If the data is not
 * structured as expected, returns an empty Course object.
 */
static Course parseCourse(const vector<string>& courseInfo) {
    // create an empty course
    Course course;

    // check to make sure course number and title exist
    if (courseInfo.size() < 2) {
        // if not return the empty course
        return course;
    }

    // set course number and title
    course.courseNumber = courseInfo[0];
    course.title = courseInfo[1];

    // if any values remain in the courseInfo, add them to the prereqs
    for (size_t i = 2; i < courseInfo.size(); ++i) {
        if (!courseInfo[i].empty()) {
            course.prerequisites.push_back(courseInfo[i]);
        }
    }

    return course;
}

/**
 * Load course data from the provided file name and insert each parsed
 * course into the provided CourseTree
 *
 * @param fileName the name for the file containing the course data
 * @param courseTree the CourseTree to populate with parsed courses
 */
static void loadCoursesFromFile(const string& fileName, CourseTree& courseTree) {
    // open the file and validate that it opened correctly
    ifstream inputFile(fileName);
    if (!inputFile.is_open()) {
        // if not, inform the user and exit the method
        cout << "Couldn't open file " << fileName << endl;
        return;
    }

    // read each line in the file
    string line;
    while (getline(inputFile, line)) {
        // if any lines are blank, skip them
        if (line.empty()) {
            continue;
        }

        // remove any trailing carriage returns to avoid issues on macOS
        line = trimTrailingCarriageReturn(line);

        // create a string stream from the line
        vector<string> courseFields;
        string fieldValue;
        stringstream lineStream(line);

        // split the line by commas and add each value to the course fields
        while (getline(lineStream, fieldValue, ',')) {
            courseFields.push_back(fieldValue);
        }

        // created a Course object with the separated course fields
        Course parsedCourse = parseCourse(courseFields);

        // If the course is valid, insert it into the courseTree
        if (!parsedCourse.courseNumber.empty()) {
            courseTree.Insert(parsedCourse);
        }
    }
}

//============================================================================
// Main
//============================================================================

/**
 * The one and only main() method
 */
int main() {
    CourseTree courseTree;

    // Greet the user
    cout << "Welcome to the course planner." << endl;
    cout << endl;

    // set initial menu selection to 0
    int choice = 0;

    while (choice != EXIT) {
        // print the menu
        cout << "   " << LOAD_DATA << ". Load Data Structure." << endl;
        cout << "   " << PRINT_COURSE_LIST << ". Print Course List." << endl;
        cout << "   " << PRINT_COURSE << ". Print Course." << endl;
        cout << "   " << EXIT << ". Exit" << endl;
        cout << endl;
        cout << "What would you like to do? ";

        // get the user's choice
        if (!(cin >> choice)) {
            // if the input is not a valid number, inform the user.
            cin.clear();
            cin.ignore(1000, '\n');
            cout << "Please enter a valid choice." << endl << endl;
            continue;
        }

        switch (choice) {
            case LOAD_DATA: {
                string fileName;

                // request the file name from the user
                cout << "Enter file name: ";
                cin.ignore(1000, '\n');
                getline(cin, fileName);

                // create a new course tree and load it from the file name
                courseTree.Clear();
                loadCoursesFromFile(fileName, courseTree);

                // check to make sure courseTree contains data
                if (courseTree.empty()) {
                    // inform the user there is no valid data
                    cout << "Failed to load course data." << endl << endl;
                } else {
                    // inform the user of successful load
                    cout << "Successfully loaded course data." << endl << endl;
                }

                break;
            }

            case PRINT_COURSE_LIST:
                // check to make sure there's data in the tree
                if (courseTree.empty()) {
                    // inform the user there's no data and show the menu again
                    cout << "Please load valid course data first." << endl << endl;
                    break;
                }

                // Print the sorted schedule
                cout << "Here is a sample schedule:" << endl << endl;
                courseTree.InOrder();
                cout << endl;
                break;

            case PRINT_COURSE: {
                // check to make sure there's data in the tree
                if (courseTree.empty()) {
                    // inform the user there's no data and show the menu again
                    cout << "Please load valid course data first." << endl << endl;
                    break;
                }

                string query;

                // get the course number from the user
                cout << "What course do you want to know about? ";
                cin.ignore(1000, '\n');
                cin >> query;
                cout << endl;

                // uppercase the input for accuracy
                query = toUpper(query);

                // search for the course
                Course foundCourse = courseTree.Search(query);

                // check for a valid course
                if (foundCourse.courseNumber.empty()) {
                    // if not found, inform the user
                    cout << "Course " << query << " not found." << endl;
                } else {
                    // otherwise, print the full course info
                    foundCourse.printInfo();
                    cout << endl << endl;
                }
                break;
            }

            case EXIT:
                cout << "Thank you for using the course planner!" << endl;
                break;

            default:
                cout << choice << " is not a valid option." << endl << endl;
                break;
        }
    }

    return 0;
}
