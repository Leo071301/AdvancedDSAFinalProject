The Wasteland Exchange

Purpose:
In a post-apocalyptic world, survival depends on the efficient distribution of resources. 
This system tracks inventories of food, medicine, and weapons across multiple colonies 
and processes trade requests between them. By managing incoming requests and matching 
them with available providers, the system ensures fair and efficient allocation of resources.

Features:
- Generates unique IDs for each colony, assigns locations using Point2D, and tracks risk factors
- Encapsulates colony inventories to maintain data integrity
- Uses polymorphism through a base Resource class with subclasses (Food, Medicine, Weapon)
- Automatically logs trade requests with unique IDs and timestamps
- Processes trade requests using FIFO order to ensure fairness
- Implements sorting (Merge Sort) and searching (Binary Search)
- Uses a HashMap for fast colony lookup and management
- Implements an AVL Tree for balanced indexing (O(log n))
- Models colony relationships using a weighted graph (adjacency list)
- Uses Prim’s Algorithm to generate a Minimum Spanning Tree (MST)
- Uses Dijkstra’s Algorithm for shortest paths

File Structure:
- Colony.java – Manages colony data and inventory
- Resource.java – Abstract resource class
- Food.java, Medicine.java, Weapon.java – Resource subclasses
- TradeRequest.java – Stores trade request data
- Identifiable.java – Interface for unique IDs
- TradeManager.java – Handles trade logic and colony management
- SearchSortUtils.java – Sorting and searching algorithms
- AVLTree.java – Balanced tree structure
- WeightedEdge.java – Weighted connections between colonies
- WeightedGraph.java – Graph structure and MST logic
- Tester.java – Main program demonstrating features

Data Structure Decisions:
- ArrayList – Dynamic storage for vertices and adjacency lists
- HashMap – O(1) average lookup for colonies
- AVL Tree – O(log n) search, insert, delete
- Queue (FIFO) – Fair trade request processing
- Adjacency List – Efficient graph representation
- Weighted Graph – Distance-based cost between colonies
- MST (Prim’s) – Minimum cost network connecting all colonies

How to Run:
Compile and execute Tester.java.

The program demonstrates:
- Sorting and searching
- Trade request processing
- Hashing and identity verification
- Graph construction and visualization
- Minimum Spanning Tree generation
- Integrates MST and shortest path into trade logic

Limitations:
- Only one resource type per request
- No data persistence (resets on program exit)
- Graph assumes all colonies are connected

Future Implementations:
- Improve graph output formatting
- Add persistent storage (file/database)
- Expand trade matching criteria
- Optimize graph algorithms using priority queues
