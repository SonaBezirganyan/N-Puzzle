import java.util.Arrays;

public class MatrixTiles extends Tiles implements Cloneable{
    private byte[][] tiles;     // represents the game board
    private int emptyCol;       // represents the column of the empty tile
    private int emptyRow;       // represents the row of the empty tile

    // Constructor for MatrixTiles to initialise the states of the class
    public MatrixTiles(Configuration configuration) throws ConfigurationFormatException, InvalidConfigurationException {
        super(configuration);
        tiles = new byte[getSize()][getSize()];
        getConfiguration().initialise(this);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == EMPTY) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    // Copy constructor for MatrixTiles
    public MatrixTiles(MatrixTiles other) {
        super(other);
        this.emptyCol = other.emptyCol;
        this.emptyRow = other.emptyRow;
        tiles = copyMatrix(other.tiles);
    }

    @Override
    /** Overrides the clone method of the class Object to return a deep copy of the given MatrixTiles object. */
    protected MatrixTiles clone() {
        try {
            MatrixTiles copy = (MatrixTiles) super.clone();
            copy.tiles = copyMatrix(tiles);
            return copy;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * A method to return an independent copy of a 2D array of bytes.
     * @param m a 2D array containing byte values
     * @return a deep copy of matrix
     */
    private byte[][] copyMatrix(byte[][] m) {
        byte[][] c = new byte[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            c[i] = Arrays.copyOf(m[i], m[i].length);
        }
        return c;
    }

    @Override
    /** Makes a move by sliding a tile into the empty space in the specified direction. */
    public void makeMove(Direction direction) {
        switch (direction) {
            case UP:
                tiles[emptyRow][emptyCol] = tiles[emptyRow + 1][emptyCol];
                tiles[emptyRow + 1][emptyCol] = 0;
                emptyRow++;
                break;
            case RIGHT:
                tiles[emptyRow][emptyCol] = tiles[emptyRow][emptyCol - 1];
                tiles[emptyRow][emptyCol - 1] = 0;
                emptyCol--;
                break;
            case DOWN:
                tiles[emptyRow][emptyCol] = tiles[emptyRow - 1][emptyCol];
                tiles[emptyRow - 1][emptyCol] = 0;
                emptyRow--;
                break;
            case LEFT:
                tiles[emptyRow][emptyCol] = tiles[emptyRow][emptyCol + 1];
                tiles[emptyRow][emptyCol + 1] = 0;
                emptyCol++;
                break;
        }
    }

    @Override
    // accessor for a specific tile
    public byte getTile(int col, int row) {
        if (row < 0 || row >= getSize()) {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }
        if (col < 0 || col >= getSize()) {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }

        return tiles[row][col];
    }


    @Override
    // mutator for a specific tile
    public void setTile(int col, int row, byte value) {
        if (row < 0 || row >= getSize()) {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }
        if (col < 0 || col >= getSize()) {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }

        tiles[row][col] = value;
    }

    @Override
    /**
     * Checks if one of the tiles can be moved to the specified direction.
     * @return true if a move to the specified direction can be performed, false otherwise
     */
    protected boolean canMove(Direction direction) {
        switch (direction) {
            case UP:
                return isValidPosition(emptyCol, emptyRow + 1);
            case RIGHT:
                return isValidPosition(emptyCol - 1, emptyRow);
            case DOWN:
                return isValidPosition(emptyCol, emptyRow - 1);
            case LEFT:
                return isValidPosition(emptyCol + 1, emptyRow);
        }

        return false;
    }
}
