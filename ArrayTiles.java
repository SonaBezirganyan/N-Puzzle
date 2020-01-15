/** A class for an alternative representation of the board that relies on a 1D array to represent the board and
 * the data type byte to represent the value of each tyle. */

import java.util.Arrays;

public class ArrayTiles extends Tiles {
    private byte[] tiles;   // represents the board
    private int emptyPos;

    // Constructor for ArrayTiles
    public ArrayTiles(Configuration configuration) throws ConfigurationFormatException, InvalidConfigurationException {
        super(configuration);
        tiles = new byte[getSize() * getSize()];
        getConfiguration().initialise(this);
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getTile(j, i) == EMPTY) {
                    emptyPos = i * getSize() + j;
                }
            }
        }
    }

    // Copy constructor for ArrayTiles
    public ArrayTiles(ArrayTiles other) {
        super(other);
        this.emptyPos = other.emptyPos;
        tiles = new byte[other.tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = other.tiles[i];
        }
    }

    @Override
    /** Overrides the clone method of the class Object to return a deep copy of the given ArrayTiles object. */
    protected ArrayTiles clone() {
        try {
            ArrayTiles copy = (ArrayTiles) super.clone();
            copy.tiles = Arrays.copyOf(tiles, tiles.length);
            return copy;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    /** Makes a move by sliding a tile into the empty space in the specified direction. */
    protected void makeMove(Direction direction) {
        int emptyRow = emptyPos / getSize();
        int emptyCol = emptyPos % getSize();
        switch (direction) {
            case UP:
                setTile(emptyCol, emptyRow, getTile(emptyCol, emptyRow + 1));
                setTile(emptyCol, emptyRow + 1, (byte) 0);
                emptyPos += getSize();
                break;
            case RIGHT:
                setTile(emptyCol, emptyRow, getTile(emptyCol - 1, emptyRow));
                setTile(emptyCol - 1, emptyRow, (byte) 0);
                emptyPos--;
                break;
            case DOWN:
                setTile(emptyCol, emptyRow, getTile(emptyCol, emptyRow - 1));
                setTile(emptyCol, emptyRow - 1, (byte) 0);
                emptyPos -= getSize();
                break;
            case LEFT:
                setTile(emptyCol, emptyRow, getTile(emptyCol + 1, emptyRow));
                setTile(emptyCol + 1, emptyRow, (byte) 0);
                emptyPos++;
                break;
        }
    }

    @Override
    // accessor for a specific tile
    public byte getTile(int col, int row) {
        return getTile(row * getSize() + col);
    }

    // overloaded helper method for getTile
    private byte getTile(int pos) {
        if (pos < 0 || pos >= tiles.length) {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }

        return tiles[pos];
    }

    @Override
    // mutator for a specific tile
    public void setTile(int col, int row, byte value) {
        setTile(row * getSize()  + col, value);
    }

    // overloaded helper method for setTile
    private void setTile(int pos, byte value) {
        if (pos < 0 || pos >= tiles.length) {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }

        tiles[pos] = value;
    }

    @Override
    /**
     * Checks if one of the tiles can be moved to the specified direction.
     * @return true if a move to the specified direction can be performed, false otherwise
     */
    protected boolean canMove(Direction direction) {
        int emptyRow = emptyPos / getSize();
        int emptyCol = emptyPos % getSize();
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
