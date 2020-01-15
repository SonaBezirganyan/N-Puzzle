import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NPuzzle {
    private Tiles tiles;
    private ConfigurationStore store;
    private ArrayList<Tiles> cachedTiles;

    // Constructor for NPuzzle
    public NPuzzle(ConfigurationStore store) {
        this.store = store;
        cachedTiles = new ArrayList<>();
    }

    /**
     * @param useCloning indicates whether 'tiles' should be copied using the clone method or the corresponding copy constructor
     * @return a deep copy of 'tiles'
     */
    private Tiles copyTiles(boolean useCloning) {
        if (useCloning) {
            Tiles copy = tiles.clone();
            return copy;
        }
        // if useCloning is false
        if (tiles instanceof MatrixTiles) {
            Tiles copy = new MatrixTiles((MatrixTiles) tiles);
            return copy;
        }
        // tiles is instanceof ArrayTiles
        Tiles copy = new ArrayTiles((ArrayTiles) tiles);
        return copy;
    }

    /**
     * Performs the following operations when the corresponding letter is entered:
     * • l to list all the configurations with a number associated to each
     * • c X to start playing configuration number X
     * • UP, DOWN, LEFT, RIGHT, or q to make a move or quit once play has begun
     * • b to move back one move unless we are already at move 0,
     *   in which case it just prints the initial board
     * • f to make forward one move
     */
    public void play() throws IOException, ConfigurationFormatException, InvalidConfigurationException {
        String response = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please select a configuration to play (l to list):");
        while (!response.equals("q")) {
            response = in.readLine();
            System.out.println(response);
            if (response.equals("UP") || response.equals("RIGHT") ||
                    response.equals("DOWN") || response.equals("LEFT")) {
                if (tiles == null) {
                    System.out.println("Please select a configuration to play (l to list):");
                } else {
                    tiles.move(Tiles.Direction.valueOf(response));
                    Tiles copy = copyTiles(true);
                    cachedTiles.add(copy);
                    print();
                    if (!tiles.isSolved()) {
                        System.out.println("Please make a move by inputting UP, DOWN, LEFT, RIGHT;");
                        System.out.println("or stop the game by inputting q: ");
                    } else {
                        System.out.println("You solved the puzzle!");
                        tiles = null;
                        System.out.println("Please select a configuration to play (l to list):");
                    }
                }
            } else if (response.equals("l")) {
                ArrayList<Configuration> configs = store.getConfigurationsSizeSorted();
                int i = 0;
                for (Configuration c : configs) {
                    System.out.println(i + " " + c.getSize() + " (" + c.getData() + ")");
                    i++;
                }
            } else if (response.startsWith("c")) {
                ArrayList<Configuration> configs = store.getConfigurationsSizeSorted();
                String[] arr = response.split(" ");
                int intAfterC = Integer.parseInt(arr[1]);
                Configuration conf = configs.get(intAfterC);
                tiles = new ArrayTiles(conf);
                Tiles copy = copyTiles(true);
                cachedTiles.add(copy);
                print();

                if (!tiles.isSolvable()) {
                    System.out.println("The game is not solvable. Quitting.");
                    System.exit(0);
                }
                if (!tiles.isSolved()) {
                    System.out.println("Please make a move by inputting UP, DOWN, LEFT, RIGHT;");
                    System.out.println("or stop the game by inputting q: ");
                } else {
                    System.out.println("You solved the puzzle!");
                    tiles = null;
                    System.out.println("Please select a configuration to play (l to list):");
                }
            } else if (response.equals("f")) {
                if (tiles == null) {
                    System.out.println("Please select a configuration to play (l to list):");
                } else if (tiles.getMoveCount() + 1 < cachedTiles.size()) {
                    tiles.incrementMoveCount();
                    tiles = cachedTiles.get(tiles.getMoveCount());
                    tiles = copyTiles(true);
                    print();
                } else {
                    print();
                }
            } else if (response.equals("b")) {
                if (tiles == null) {
                    System.out.println("Please select a configuration to play (l to list):");
                } else if (tiles.getMoveCount() > 0) {
                    tiles.decrementMoveCount();
                    tiles = cachedTiles.get(tiles.getMoveCount());
                    tiles = copyTiles(true);
                    print();
                } else if (tiles.getMoveCount() == 0) {
                    print();
                }
            }
        }
    }


    public void print() {
        System.out.println("- " + tiles.getMoveCount() + " moves");
        System.out.println("---------------------");
        for (int i = 0; i < tiles.getSize(); i++) {
            for (int j = 0; j < tiles.getSize(); j++) {
                if (tiles.getTile(j, i) < 10) {
                    System.out.print("|  " + tiles.getTile(j, i) + " ");
                } else {
                    System.out.print("| " + tiles.getTile(j, i) + " ");
                }
            }
            System.out.println("|");
            System.out.println("---------------------");
        }
    }


    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Usage: java NPuzzle <path/url to store>");
            return;
        }
        try {
            ConfigurationStore cs = new ConfigurationStore(args[0]);
            NPuzzle np = new NPuzzle(cs);
            np.play();
        } catch (IOException ioe) {
            System.out.println("Failed to load configuration store");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
