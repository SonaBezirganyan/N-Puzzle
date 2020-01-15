import java.util.ArrayList;

public abstract class Tiles implements Cloneable {
    protected static final byte EMPTY = 0;
    private int moves;      // a counter to keep track of the number of moves made
    private Configuration configuration;

    protected enum Direction {UP, RIGHT, DOWN, LEFT}

    // Constructor for Configuration class
    public Tiles(Configuration configuration) {
        this.configuration = configuration;
    }

    // Copy constructor for Configuration class
    public Tiles(Tiles other) {
        this.moves  = other.moves;
        this.configuration = other.configuration;
    }

    @Override
    /** Overrides the clone method of the class Object to return a deep copy of the given Tiles object. */
    protected Tiles clone() {
        try {
            Tiles copy =  (Tiles) super.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    // accessor for the size of the board
    public int getSize() {
        return configuration.getSize();
    }

    // accessor for moves
    public int getMoveCount() {
        return moves;
    }

    /** Increments 'moves' by 1. */
    public void incrementMoveCount() {
        moves++;
    }

    /** Decerements 'moves' by 1. */
    public void decrementMoveCount() {
        moves--;
    }

    // accessor for configuration
    public Configuration getConfiguration() {
        return configuration;
    }

    /** Checks if a move to the specified direction is possible. If yes, makes a move by
     * sliding a tile into the empty space in the specified direction. */
    public void move(Direction direction) {
        if (!canMove(direction)) {
            return;
        }
        makeMove(direction);
        incrementMoveCount();
    }

    /**
     * Checks if the given tile is in the bounds of the board.
     * @param col the column number of the tile
     * @param row the row number of the tile
     * @return true if the given coordinate is within the bounds of the board, false otherwise
     */
    protected boolean isValidPosition(int col, int row) {
        return col >= 0 && col < getSize() && row >= 0 && row < getSize();
    }

    /** Makes a move by sliding a tile into the empty space in the specified direction. */
    protected abstract void makeMove(Direction direction);

    /**
     * Checks if one of the tiles can be moved to the specified direction.
     * @return true if a move to the specified direction can be performed, false otherwise
     */
    protected abstract boolean canMove(Direction direction);

    // accessor for a specific tile
    public abstract byte getTile(int col, int row);

    // mutator for a specific tile
    public abstract void setTile(int col, int row, byte value);

    /**
     * Checks if the puzzle is solved.
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean isSolved() {
        int count = 1;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (count == getSize() * getSize()) {
                    break;
                }
                if (getTile(j, i) != count) {
                    return false;
                }
                count++;
            }
        }

        return true;
    }

    /** Checks if the there are invalid fields in the configuration and throws a corresponding exception if necessary. */
    public void ensureValidity() throws InvalidConfigurationException {
        //checking if there is tile with a too small or too large value for the given board
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getTile(j, i) < 0 || getTile(j, i) >= getSize() * getSize()) {
                    byte a = getTile(j, i);
                    throw new InvalidConfigurationException("Invalid configuration: incorrect tile value " + a);
                }
            }
        }

        //checking if the board contains multiple empty spaces
        int emptySpaces = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getTile(j, i) == EMPTY) {
                    emptySpaces++;
                }
                if (emptySpaces > 1) {
                    throw new InvalidConfigurationException("Invalid configuration: multiple empty spaces.");
                }
            }
        }


        // checking if the board contains multiple tiles with the same value
        ArrayList<Byte> container = new ArrayList<>(getSize() * getSize());
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (container.contains(getTile(j, i))) {
                    byte a = getTile(j, i);
                    throw new InvalidConfigurationException("Invalid configuration: multiple tiles " +
                            "with the value " + a + ".");
                }

                container.add(getTile(j, i));
            }
        }
    }

    /**
     * Checks if the puzzle is solvable.
     * @return true if it is possible to solve the puzzle, false otherwise
     */
    public boolean isSolvable() {
        // if n is odd
        if (getSize() % 2 == 1) {
            return isEvenPermutation();
        }

        // if n is even
        int blank = 0;
        outer: for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getTile(j, i) == EMPTY) {
                    blank = getSize() - i;
                    break outer;
                }
            }
        }

        //if the blank is on an even row counting from the bottom
        if (blank % 2 == 0) {
            return !isEvenPermutation();
        }

        //if the blank is on an odd row counting from the bottom
        return isEvenPermutation();
    }

    /** Checks if the permutation is even or not.
     * @return true if the permutation is even, false otherwise
     */
    private boolean isEvenPermutation() {
        int numberOfInversions = 0;
        byte[] arr = new byte[getSize() * getSize()];
        int c = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                arr[c++] = getTile(j, i);
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] != EMPTY && arr[j] < arr[i]) {
                    numberOfInversions++;
                }
            }
        }
        return numberOfInversions % 2 == 0;
    }
}
