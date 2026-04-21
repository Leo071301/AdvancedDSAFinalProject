The Wasteland Exchange
Purpose:

In a post-apocalyptic world, survival depends on the efficient distribution of resources. This system tracks inventories of food, medicine, and weapons across multiple colonies and processes trade requests between them. By managing incoming requests and matching them with available providers, the system ensures fair and efficient allocation of resources.

Features:
Generates unique IDs for each colony, assigns locations using Point2D, and tracks environmental risk factors
Encapsulates colony inventories to maintain data integrity
Uses polymorphism through a base Resource class with specialized subclasses (Food, Medicine, Weapon)
Automatically logs trade requests with unique IDs and timestamps
Processes trade requests using FIFO order to ensure fairness
Implements sorting (Merge Sort, Quick Sort) and searching (Binary Search) for efficient data handling
Uses a HashMap for fast colony lookup and management
Implements an AVL Tree for balanced indexing with O(log n) operations
Models colony relationships using a weighted graph (adjacency list)
Uses Prim’s Algorithm to generate a Minimum Spanning Tree (MST) for optimal connectivity between colonies
Uses Dijkstra’s Algorithm to compute shortest paths between colonies (if implemented)
File Structure:
Colony.java – Manages colony name, location, risk level, and inventory
Resource.java – Abstract class defining shared resource attributes
Food.java, Medicine.java, Weapon.java – Resource subclasses with specialized attributes
TradeRequest.java – Stores request details including requester, resource, and timestamp
Identifiable.java – Interface for unique identification
TradeManager.java – Handles trade queue, matching logic, and colony management
SearchSortUtils.java – Contains sorting and searching algorithms
AVLTree.java – Self-balancing tree for efficient indexing
WeightedEdge.java – Represents weighted connections between colonies
WeightedGraph.java – Graph structure with adjacency list and Prim’s MST implementation
Tester.java – Main driver program demonstrating all features
Data Structure Decisions:
ArrayList – Used for dynamic storage of vertices and adjacency lists
HashMap – Enables O(1) average lookup for colonies
AVL Tree – Ensures O(log n) insertion, deletion, and search
Queue (FIFO) – Ensures fair trade request processing
Adjacency List (Graph) – Efficient representation of colony connections
Weighted Graph – Uses distance between colonies as edge cost
Minimum Spanning Tree (Prim’s Algorithm) – Determines lowest-cost network connecting all colonies
How to Run:

Compile and execute Tester.java.

The program demonstrates:

Sorting and searching operations
Trade request processing
Hashing and identity verification
Graph construction and visualization
Minimum Spanning Tree generation
Limitations:
Colonies can only request one type of resource per request
Data is not persistent; all information is lost when the program terminates
Graph assumes all colonies are connected for MST generation
Future Implementations:
Integrate MST and shortest path results into trade decision-making
Improve graph output formatting for readability
Add persistent storage (files or database)
Expand trade matching criteria beyond distance and risk factor
Optimize graph algorithms using priority queues
