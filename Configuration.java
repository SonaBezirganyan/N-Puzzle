public class Configuration implements Comparable<Configuration> {
    private String data;
    private int size;

    /** Initialises the only field of this class using ’format’ */
    public Configuration(String format) throws ConfigurationFormatException {
        if (format.equals(""))  {
            throw new ConfigurationFormatException("Please specify a configuration.");
        }

        if (!format.contains(" ")) {
            throw new ConfigurationFormatException("Invalid configuration format:" +
                    "Inorrect number of fields in configuration (found 1).");
        }

        data = format.substring(format.indexOf(":") + 2);
        String first = format.substring(0, format.indexOf(":"));
        try {
            size = Integer.parseInt(first);
        } catch (NumberFormatException e) {
            throw new ConfigurationFormatException("Invalid configuration format: " +
                    "could not interpret the size field as a number ('" + first + "' given).");
        }

        throwException();
    }

    // accessor for data
    public String getData() {
        return data;
    }

    // accessor for size
    public int getSize() {
        return size;
    }

    @Override
    /** Overrides the compareTo method to compare configurations based on size. */
    public int compareTo(Configuration configuration) {
        return this.size - configuration.size;
    }

    /**  Updates the elements in the 2D array representing the values of ’tiles’
     *  as expressed by the contents of the field ’data’ .*/
    public void initialise(Tiles tiles) throws ConfigurationFormatException, InvalidConfigurationException {
        throwException();
        String[] arr = data.split(" ");
        int c = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arr[c].equals(":") || arr[c].equals("")) {
                    c++;
                }

                tiles.setTile(j, i, Byte.valueOf(arr[c++]));
            }
        }

        tiles.ensureValidity();
    }

    /** Throws ConfigurationFormatException if necessary. */
    private void throwException() throws ConfigurationFormatException {

        String[] arr = data.split(" ");

        int rows = numberOfRows(arr);
        if (rows != size) {
            throw new ConfigurationFormatException("Invalid configuration format: " +
                    "Invalid number of rows in configuration (found " + rows + ").");
        }

        int columns = numberOfColumns(arr);
        if (columns != size) {
            throw new ConfigurationFormatException("Invalid configuration format: " +
                    "Invalid number of columns in configuration (found " + columns + ").");
        }


        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].equals(":")) {
                try {
                    int a = Integer.parseInt(arr[i]);
                } catch (NumberFormatException e) {
                    throw new ConfigurationFormatException("Invalid configuration format: " +
                           "Malformed configuration '" + data + "'.");
                }
            }
        }
    }

    /** Returns the number of rows of the board represented by 'arr' */
    private int numberOfRows(String[] arr) {
        int number = 1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(":")) {
                number++;
            }
        }

        return number;
    }

    /** Returns the number of columns of the board represented by 'arr' */
    private int numberOfColumns(String[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(":")) {
                break;
            }
            count++;
        }
        return count;
    }
}
