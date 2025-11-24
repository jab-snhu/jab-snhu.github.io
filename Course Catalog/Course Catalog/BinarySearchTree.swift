// BinarySearchTree.swift
//
// Author: Jeff Blagg
// Class: CS-499 - CS Capstone
// Date: November 2025


/// A binary search tree implementation using generic `Comparable` elements.
///
/// The tree maintains a sorted order, providing O(log n) performance on average for supported operations (insert, search, in-order traversal).
public final class BinarySearchTree<Element: Comparable> {
    
    /// The root node for the tree
    private var root: Node?
    
    /// Returns `true` if there is no data in the tree, `false`
    /// otherwise
    public var isEmpty: Bool { root == nil }
    
    /// Initializes an empty binary search tree
    ///
    /// - Note: The root node is set to nil when initializing an empty tree.
    public init() {
        self.root = nil
    }
    
    /// Inserts an element into the tree.
    ///
    /// - Parameter element: The element to insert.
    public func insert(_ element: Element) {
        root = insert(element, into: root)
    }
    
    /// Searches for an element in the tree.
    ///
    /// - Parameter element: The element to search for.
    /// - Returns: The found element, or `nil` if the element wasn't found.
    public func search(_ element: Element) -> Element? {
        return search(element, in: root)
    }
    
    
    /// Traverses the tree in order and performs the provided action on each element.
    ///
    /// - Parameter action: The action closer to perform on each element.
    public func traverseInOrder(andPerform action: (Element) -> Void) {
        traverseInOrder(at: root, andPerform: action)
    }
    
    /// Removes all elements from the tree by setting root to nil
    public func clear() {
        root = nil
    }
}

// MARK: - Private Methods

extension BinarySearchTree {
    /// Inserts a new node with the provided element to the tree. (recursive)
    ///
    /// - Parameters:
    ///   - element: The element to insert.
    ///   - node: The current node in the tree.
    /// - Returns: The node with the inserted element.
    private func insert(_ element: Element, into node: Node?) -> Node {
        guard let node = node else {
            return Node(element)
        }
        
        if element < node.element {
            node.left = insert(element, into: node.left)
        } else if element > node.element {
            node.right = insert(element, into: node.right)
        }
        
        return node
    }
    
    /// Searches for an element in the tree. (recursive)
    ///
    /// - Parameters:
    ///   - element: The element to search for.
    ///   - node: The current node in the tree.
    /// - Returns: The found element, or 'nil' if the element wasn't found.
    private func search(_ element: Element, in node: Node?) -> Element? {
        guard let node = node else {
            return nil
        }
        
        if element == node.element {
            return node.element
        } else if element < node.element {
            return search(element, in: node.left)
        } else {
            return search(element, in: node.right)
        }
    }
    
    /// Traverses the tree in order and performs the provided action on each element. (recursive)
    ///
    /// - Parameters:
    ///   - node: The current node in the tree.
    ///   - action: The action to perform on the node element.
    private func traverseInOrder(at node: Node?, andPerform action: (Element) -> Void) {
        guard let node = node else {
            return
        }
        
        traverseInOrder(at: node.left, andPerform: action)
        action(node.element)
        traverseInOrder(at: node.right, andPerform: action)
    }
}

// MARK: - Node

extension BinarySearchTree {
    /// Node class for the binary search tree
    private final class Node {
        let element: Element
        var left: Node?
        var right: Node?
        
        /// Initializes a node with the provided element.
        ///
        /// - Parameter element: The element contained within the node.
        /// - Note: `left` and `right` nodes are `nil` on initialization.
        init(_ element: Element) {
            self.element = element
        }
    }
}
