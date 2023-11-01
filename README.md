## HW3 - Lattice Linear Predicate Algorithm 

# Babuji Periasubbramaniam (bp26282) & Ryan Denlinger (rd28444)

The goal of this assignment is to implement a parallel LLP algorithm and apply it to hosts of problems.

Implementation of libraries in Java which allows us to use LLP parallel algorithm to solve a problem.

Designed API for the library such that it is easy to use the LLP algorithm for various problems. Applied our library to solve the following problems:


(a) Computing transitive closure of a directed graph

(b) Topological Sort of an Acyclic Directed Graph

(c) Boruvka’s Algorithm

(d) List-Ranking Problem

# List of libraries: 

LLPInitializer.java

LLPRunner.java

LLPWorker.java

LLPOutputBuilder.java

LLPWorkerState.java


# List of APIs:

TransitiveClosure.java

TopologicalSort.java

ListRanking.java

Boruvka.java


# Test executables for each assignment problem:

RunTestsTransitiveClosure.java

RunTestsTopologicalSort.java

RunTestsListRanking.java

RunTestsBoruvka.java


# List of JUnit Test Cases for each assignment problem:

JUnitTransitiveClosure.java

JUnitTopologicalSort.java

JUnitListRanking.java

JUnitBoruvka.java

# List of Test Input Files for each assignment problem:

TransitiveInput.txt

TransitiveInput-1.txt

TransitiveInput-2.txt

TransitiveInput-3.txt

TransitiveInput-4.txt

TopoSort.txt

TopoSort-1.txt

TopoSort-2.txt

TopoSort-3.txt

TopoSort-4.txt

ListRanking.txt

ListRanking-1.txt

ListRanking-2.txt

ListRanking-3.txt

ListRanking-4.txt

Boruvka-1.txt

Boruvka-2.txt

Boruvka-3.txt

Boruvka-4.txt

# Time and Work Complexity:

(a) Computing transitive closure of a directed graph - O(n^3 log n), time depends on number of processors. O((log n)^2) parallel time for n^2 processors, O(log n) parallel time for n^3 processors

(b) Topological Sort of an Acyclic Directed Graph - Time Complexity of O(n), Work Complexity of O(n) - for totally ordered DAG

(c) Boruvka’s Algorithm - Time Complexity of O(log^2 n), Work Complexity of O(mlogn)

(d) List-Ranking Problem - Time Complexity of O(log n), Work Complexity of O(nlogn) - Not a work optimal

# Instructions to run the code

1) Open Eclipse - Launch the Eclipse Integrated Development Environment (IDE) on your computer.
   
2) Import or Open our Project including APIs, JUnit Code, test inputs (submitted as archive file in Canvas - bp26282_rd28444.zip). Please make sure that JUnit is properly configured in your Eclipse IDE.
   
3) Navigate to our executables for each of the assignment problem and execute the code (which will run against one of the test input file)

   RunTestsTransitiveClosure.java

   RunTestsTopologicalSort.java

   RunTestsListRanking.java

   RunTestsBoruvka.java
  
5) Navigate to our JUnit Test Class. Locate our JUnit test class (listed above) in the Project Explorer. It should be in the same project as the code you want to test. The test class's name typically follows the pattern:

   JUnitTransitiveClosure.java

   JUnitTopologicalSort.java

   JUnitListRanking.java

   JUnitBoruvka.java

6) Run JUnit Tests which will pull input from multiple test input files for each assignment problem. Right-click on the JUnit test class or specific test method you want to run. Choose Run As > JUnit Test. Alternatively, you can use the keyboard shortcut Ctrl + F11 (Windows/Linux) or Command + F11 (Mac) to run the JUnit tests.

7) View Test Results. The JUnit test results will appear in the JUnit view in Eclipse. You can access the JUnit view by clicking Window > Show View > Other, then selecting Java > JUnit. This view will display the test results, including passed and failed tests.

# Sample Output of each assignment problem below:

# Computing transitive closure of a directed graph:

**

** ParallelTransitiveClosure **

**

1 1 1 1 

0 1 1 1 

0 0 1 1 

0 0 0 1 

# Topological Sort of an Acyclic Directed Graph

**

** ParallelTopologicalSort **

**

0 1 2 3 4 5 6 7 8 9 10 


# List-Ranking Problem

**

** ParallelListRanking **

**


Node 0 has distance 1 

Node 1 has distance 2 

Node 2 has distance 3 

Node 3 has distance 4 

Node 4 has distance 5 

Node 5 has distance 6 

# Boruvka’s Algorithm

**

** ParallelBoruvka **

**

*** Minimum Spanning Tree ***

Node 0 <-> Node 2 Weight: 6.0

Node 1 <-> Node 3 Weight: 5.0

Node 3 <-> Node 12 Weight: 1.0

Node 4 <-> Node 3 Weight: 4.0

Node 5 <-> Node 6 Weight: 5.0

Node 6 <-> Node 7 Weight: 4.0

Node 8 <-> Node 9 Weight: 6.0

Node 11 <-> Node 10 Weight: 15.0

# Sample JUnit Test Results of each assignment problem below:

**

** ParallelBoruvka - JUnit Testing : Boruvka's algorithm of a Simple Linear Graph **

**

*** Minimum Spanning Tree ***

Node 0 <-> Node 3 Weight: 5.0

Node 1 <-> Node 0 Weight: 10.0

Node 3 <-> Node 2 Weight: 4.0

**

** ParallelBoruvka - JUnit Testing : Boruvka's algorithm of a Tree Structure **

**

*** Minimum Spanning Tree ***

Node 0 <-> Node 1 Weight: 2.0

Node 2 <-> Node 3 Weight: 1.0

Node 5 <-> Node 6 Weight: 4.0

Node 6 <-> Node 4 Weight: 2.0

Node 7 <-> Node 8 Weight: 7.0

Node 9 <-> Node 10 Weight: 3.0

**

** ParallelBoruvka - JUnit Testing : Boruvka's algorithm of a Large Graph - MST distance **

**

*** Minimum Spanning Tree ***

Node 0 <-> Node 2 Weight: 6.0

Node 1 <-> Node 3 Weight: 5.0

Node 3 <-> Node 12 Weight: 1.0

Node 4 <-> Node 3 Weight: 4.0

Node 5 <-> Node 6 Weight: 5.0

Node 6 <-> Node 7 Weight: 4.0

Node 8 <-> Node 9 Weight: 6.0

Node 11 <-> Node 10 Weight: 15.0

**

** ParallelBoruvka - JUnit Testing : Boruvka's algorithm of Empty Graph **

**

*** Minimum Spanning Tree ***

Empty Graph


**

** ParallelListRanking - JUnit Testing : List Ranking of a Simple Linear Graph **

**

Node 0 has distance 1 

Node 1 has distance 2 

Node 2 has distance 3 

Node 3 has distance 4 

Node 4 has distance 5 

Node 5 has distance 6 

**

** ParallelListRanking - JUnit Testing : List Ranking of a Tree Structure **

**

Node 0 has distance 1 

Node 1 has distance 2 

Node 2 has distance 2 

Node 3 has distance 3 

Node 4 has distance 4 

Node 5 has distance 4 

Node 6 has distance 5 


**

** ParallelListRanking - JUnit Testing : List Ranking of a Single Node Graph **

**

Node 0 has distance 1 

**

** ParallelListRanking - JUnit Testing : List Ranking of a Empty Graph **

**

**

** ParallelTopologicalSort - JUnit Testing : Simple Linear Graph **

**

0 1 2 3 4 5 6 7 8 9 10 

**

** ParallelTopologicalSort - JUnit Testing : Multiple Independent Chains **

**

0 1 1 2 3 3 4 0 1 2 3 

**

** ParallelTopologicalSort - JUnit Testing : DAG with Cycles **

**

**

** ParallelTopologicalSort - JUnit Testing : Tree Structure **

**

0 1 1 2 3 3 4 5 6 7 8 

**

** ParallelTopologicalSort - JUnit Testing : Empty Graph **

**

**

** ParallelTransitiveClosure - JUnit Testing : Transitive Closure of a Graph with 4 nodes - Linear Graph **

**

1 1 1 1 

0 1 1 1 

0 0 1 1 

0 0 0 1 

**

** ParallelTransitiveClosure - JUnit Testing : Transitive Closure of a Empty Graph with 0 nodes **

**

**

** ParallelTransitiveClosure - JUnit Testing : Transitive Closure of a Graph without square matrix **

**

**

** ParallelTransitiveClosure - JUnit Testing : Transitive Closure of a Cyclic Graph **

**

1 1 1 1 

1 1 1 1 

1 1 1 1 

1 1 1 1 

**

** ParallelTransitiveClosure - JUnit Testing : Transitive Closure of a Graph with disconnected components **

**

0 1 0 0 

0 0 0 0 

0 0 0 0 

0 0 0 0 

