## HW3 - Lattice Linear Predicate Algorithm

The goal of this assignment is to implement a parallel LLP algorithm and apply it to hosts of problems. For this assignment, you can work in groups of two. You should submit the name of your partner in the
submitted assignment. All the teams must initially agree on the input and the output format. Each team will create two input cases for each of the problem.

You will submit the source code for your assignment along with the runtimes for each of the problems.

1. (100 points) Implement a library in Java that allows one to use LLP parallel algorithm to solve a problem.

You would need to design the API for the library such that it is easy to use the LLP algorithm for various problems. Apply your library to solve the following problems:

Teams 7,8,9 and 10

(a) Computing transitive closure of a directed graph

(b) Topological Sort of an Acyclic Directed Graph

(c) Boruvka’s Algorithm

(d) List-Ranking Problem

# List of API Libraries developed for this assignment

LLPInitializer.java

LLPRunner.java

LLPWorker.java

LLPOutputBuilder.java

LLPWorkerState.java


# List of code for each assignment problem:

TransitiveClosure.java

TopologicalSort.java

ListRanking.java

Boruvka.java


# Test executables for each assignment problem:

RunTestsTransitiveClosure.java

RunTestsTopologicalSort.java

RunTestsListRanking.java

RunTestsBoruvka.java


# List of JUnit Test Cases

JUnitTransitiveClosure.java

JUnitTopologicalSort.java

JUnitListRanking.java

JUnitBoruvka.java


# Instructions to run the code

1) Open Eclipse - Launch the Eclipse Integrated Development Environment (IDE) on your computer.
   
2) Import or Open our Project including APIs and JUnit test cases (submitted as archive file in Canvas). Please make sure that JUnit is properly configured in your Eclipse IDE.
  
3) Navigate to Your JUnit Test Class. Locate our JUnit test class (listed above) in the Project Explorer. It should be in the same project as the code you want to test. The test class's name typically follows the pattern:

   JUnit<AssignmentProblem>.java.

4) Run JUnit Tests. Right-click on the JUnit test class or specific test method you want to run. Choose Run As > JUnit Test. Alternatively, you can use the keyboard shortcut Ctrl + F11 (Windows/Linux) or Command + F11 (Mac) to run the JUnit tests.

5) View Test Results. The JUnit test results will appear in the JUnit view in Eclipse. You can access the JUnit view by clicking Window > Show View > Other, then selecting Java > JUnit. This view will display the test results, including passed and failed tests.

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

