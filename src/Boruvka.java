import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Boruvka {
    private Map<Integer, WeightedEdge> minEdges = new HashMap<Integer, WeightedEdge>();
    private WeightedGraph minEdgeGraph = new WeightedGraph(0, new ArrayList<WeightedEdge>());
    private Map<Double, WeightedEdge> weightToEdgeMap = new HashMap<Double, WeightedEdge>();
    private int size;
    private int numThreads;

    public static class WeightedEdge {
        public final int source;
        public final int dest;
        public final double weight;

        public WeightedEdge(int source, int dest, double weight) {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }

        public boolean equals(WeightedEdge other) {
            return source == other.source && dest == other.dest && weight == other.weight;
        }

        public WeightedEdge reverse() {
            return new WeightedEdge(dest, source, weight);
        }
    }

    public static class WeightedGraph {
        public final List<WeightedEdge> edges;
        public final int size;

        public WeightedGraph(int size, List<WeightedEdge> edges) {
            this.size = size;
            this.edges = edges;
        }
    }

    public static Map<Integer, WeightedEdge> treeify(Map<Integer, WeightedEdge> minEdges, int size) {
        Map<Integer, List<WeightedEdge>> reverseMinEdges = new HashMap<Integer, List<WeightedEdge>>();
        Collection<WeightedEdge> edges = minEdges.values();
        for (WeightedEdge edge : edges) {
            if (!reverseMinEdges.containsKey(edge.dest)) {
                reverseMinEdges.put(edge.dest, new ArrayList<WeightedEdge>());
            }

            reverseMinEdges.get(edge.dest).add(edge.reverse());
        }

        Map<Integer, WeightedEdge> treeified = new HashMap<Integer, WeightedEdge>();

        for (int i = 0; i < size; ++i) {
            if (!minEdges.containsKey(i)) {
                continue;
            }

            WeightedEdge edge = minEdges.get(i);

            // different weights
            if (minEdges.containsKey(edge.dest) && edge.weight != minEdges.get(edge.dest).weight) {
                treeified.put(i, edge);
                continue;
            }

            // no leaves in pseudo-tree
            if (reverseMinEdges.containsKey(i)
                    && reverseMinEdges.containsKey(edge.dest)
                    && reverseMinEdges.get(i).size() == 1
                    && reverseMinEdges.get(edge.dest).size() == 1
                    && reverseMinEdges.get(edge.dest).get(0).dest == i
                    && i != edge.dest) {
                treeified.put(i, i < edge.dest ? edge : new WeightedEdge(i, i, edge.weight));
                continue;
            }

            // two incoming min-edges
            if (reverseMinEdges.containsKey(i) && reverseMinEdges.get(i).size() > 1) {
                treeified.put(i, edge);
                continue;
            }

            // root
            treeified.put(i, new WeightedEdge(i, i, edge.weight));
        }

        return treeified;
    }

    public class Worker implements LLPWorker<WeightedGraph> {
        private LLPWorkerState<WeightedGraph> state;

        @Override
        public void setState(LLPWorkerState<WeightedGraph> state) {
            this.state = state;
        }

        @Override
        public boolean isForbidden() {
            if (state.getValue() == -1) {
                return false;
            }

            if (state.getValue(state.getValue()) == -1) {
                return false;
            }

            return state.getValue() != state.getValue(state.getValue());
        }

        @Override
        public int getAdvanceValue() {
            return isForbidden() ? state.getValue(state.getValue()) : state.getValue();
        }

        @Override
        public void advance() {
            int newValue = getAdvanceValue();
            if (newValue != state.getValue()) {
                state.setValue(newValue);
            }
        }

        @Override
        public LLPWorker<WeightedGraph> clone() {
            Worker workerClone = new Worker();
            if (state != null) {
                workerClone.setState(state.clone());
            }
            return workerClone;
        }

        @Override
        public void setLatticeIndex(int latticeIndex) {
            state.setLatticeIndex(latticeIndex);
        }
    }

    public static Map<Integer, WeightedEdge> getMinEdgesOf(WeightedGraph graph, boolean undirected, boolean treeify) {
        Set<Integer> activeVertices = new HashSet<Integer>();
        for (WeightedEdge edge : graph.edges) {
            activeVertices.add(edge.source);
            activeVertices.add(edge.dest);
        }
        Map<Integer, WeightedEdge> minEdges = new HashMap<Integer, WeightedEdge>();
        for (WeightedEdge edge : graph.edges) {
            if (!activeVertices.contains(edge.source) || !activeVertices.contains(edge.dest)) {
                continue;
            }

            if (!minEdges.containsKey(edge.source)) {
                minEdges.put(edge.source, edge);
            } else if (edge.weight < minEdges.get(edge.source).weight) {
                minEdges.put(edge.source, edge);
            }

            if (undirected) {
                WeightedEdge reverse = edge.reverse();
                if (!minEdges.containsKey(reverse.source)) {
                    minEdges.put(reverse.source, reverse);
                } else if (reverse.weight < minEdges.get(reverse.source).weight) {
                    minEdges.put(reverse.source, reverse);
                }
            }
        }

        if (treeify) {
            return treeify(minEdges, graph.size);
        }

        return minEdges;
    }

    public static WeightedGraph getMinEdgeGraphOf(int size, Map<Integer, WeightedEdge> graph) {
        List<WeightedEdge> edges = new ArrayList<WeightedEdge>();
        Collection<WeightedEdge> values = graph.values();
        for (WeightedEdge value : values) {
            edges.add(value);
        }
        return new WeightedGraph(size, edges);
    }

    public static WeightedGraph getMinEdgeGraphOf(WeightedGraph graph, boolean undirected, boolean treeify) {
        return getMinEdgeGraphOf(graph.size, getMinEdgesOf(graph, undirected, treeify));
    }

    public static Map<Double, WeightedEdge> getWeightMapOf(WeightedGraph graph) {
        Map<Double, WeightedEdge> weightMap = new HashMap<Double, WeightedEdge>();
        for (WeightedEdge edge : graph.edges) {
            weightMap.put(edge.weight, edge);
        }
        return weightMap;
    }

    public WeightedGraph execute(WeightedGraph input, int numThreads) throws InterruptedException {
        minEdges = Boruvka.getMinEdgesOf(input, true, true);
        minEdgeGraph = getMinEdgeGraphOf(input.size, minEdges);
        weightToEdgeMap = getWeightMapOf(input);
        size = input.size;
        this.numThreads = numThreads;
        LLPRunner<WeightedGraph, WeightedGraph> runner = new LLPRunner.LLPRunnerBuilder<WeightedGraph, WeightedGraph>()
                .setInitializer(new Initializer())
                .setInput(input)
                .setNumThreads(numThreads)
                .setWorkerPrototype(new Worker())
                .setOutputBuilder(new OutputBuilder())
                .build();
        runner.computeLLP();
        runner.joinAllThreads();
        return runner.getOutput();
    }

    public WeightedGraph getPrimedGraph(int[] latticeValues) {
        WeightedGraph primedGraph = new Boruvka.WeightedGraph(size, new ArrayList<WeightedEdge>());

        Set<Integer> vertices = new HashSet<Integer>();
        for (int i = 0; i < size; ++i) {
            if (i == latticeValues[i]) {
                vertices.add(i);
            }
        }

        for (WeightedEdge edge : minEdgeGraph.edges) {
            int a = latticeValues[edge.source];
            int b = latticeValues[edge.dest];
            if (a != b && vertices.contains(a) && vertices.contains(b)) {
                primedGraph.edges.add(new WeightedEdge(a, b, edge.weight));
            }
        }

        return primedGraph;
    }

    public class OutputBuilder implements LLPOutputBuilder<WeightedGraph, WeightedGraph> {
        private int[] latticeValues = new int[0];
        private WeightedGraph input;

        @Override
        public WeightedGraph build() {
            boolean hasEdges = false;
            for (WeightedEdge edge : input.edges) {
                if (edge.source != edge.dest) {
                    hasEdges = true;
                }
            }
            if (!hasEdges) {
                return new WeightedGraph(input.size, new ArrayList<WeightedEdge>());
            }

            WeightedGraph output = new WeightedGraph(input.size, new ArrayList<WeightedEdge>());
            for (WeightedEdge edge : minEdgeGraph.edges) {
                if (edge.source != edge.dest) {
                    output.edges.add(edge);
                }
            }
            try {
                WeightedGraph boruvka = new Boruvka().execute(getPrimedGraph(latticeValues), numThreads);
                for (WeightedEdge edge : boruvka.edges) {
                    if (weightToEdgeMap.containsKey(edge.weight)) {
                        output.edges.add(weightToEdgeMap.get(edge.weight));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        public LLPOutputBuilder<WeightedGraph, WeightedGraph> setInput(WeightedGraph input) {
            this.input = input;
            return this;
        }

        @Override
        public LLPOutputBuilder<WeightedGraph, WeightedGraph> setLatticeValues(int[] latticeValues) {
            this.latticeValues = latticeValues;
            return this;
        }
    }

    public class Initializer implements LLPInitializer<WeightedGraph> {
        @Override
        public int[] createInitialLatticeState(WeightedGraph graph) {
            int size = graph.size;
            int[] latticeIndices = new int[size];
            for (int i = 0; i < size; ++i) {
                latticeIndices[i] = -1;
            }

            // must have already been treeified!
            for (WeightedEdge edge : minEdgeGraph.edges) {
                latticeIndices[edge.source] = edge.dest;
            }

            return latticeIndices;
        }
    }
}