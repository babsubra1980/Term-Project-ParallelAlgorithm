public class RunTests {
    public static void testTransitiveClosure() {
        System.out.println("**");
        System.out.println("** ParallelTranstiveClosure **");
        TransitiveClosure pt = new TransitiveClosure();
        System.out.println("**");
        Integer[][] graph = {
                {
                        1, 1, 0, 1
                },
                {
                        0, 1, 1, 0
                },
                {
                        0, 0, 1, 1
                },
                {
                        0, 0, 0, 1
                }
        };
        try {
            Integer[][] output = pt.execute(graph, 2);
            for (Integer[] row : output) {
                for (Integer val : row) {
                    System.out.print(val);
                }
                System.out.println("");
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testTransitiveClosure();
    }
}
