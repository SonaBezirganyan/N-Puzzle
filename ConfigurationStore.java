import java.io.*;
import java.net.*;
import java.util.*;

public class ConfigurationStore {
    private ArrayList<Configuration> configs = new ArrayList<>();

    // Constructor for ConfigurationStore
    public ConfigurationStore(String source) throws IOException, InvalidConfigurationException, ConfigurationFormatException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            loadFromURL(source);
        } else {
            loadFromDisk(source);
        }
    }

    // Constructor for ConfigurationStore
    public ConfigurationStore(Reader source) throws IOException, InvalidConfigurationException, ConfigurationFormatException {
        load(source);
    }

    /** Reads each line from 'r' and stores in 'configs' */
    private void load(Reader r) throws IOException {
        BufferedReader b = new BufferedReader(r);
        String line;
        while ((line = b.readLine()) != null) {
            try {
                configs.add(new Configuration(line));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /** Reads configurations from a file over the web given a URL. */
    private void loadFromURL(String url) throws IOException, InvalidConfigurationException, ConfigurationFormatException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new InputStreamReader(conn.getInputStream());
        load(r);
    }

    /** Reads configurations from a file. */
    private void loadFromDisk(String filename) throws IOException, InvalidConfigurationException, ConfigurationFormatException {
        Reader r = new FileReader(filename);
        load(r);
    }

    /** Returns a list of all configurations sorted by size. */
     public ArrayList<Configuration> getConfigurationsSizeSorted() {
        Collections.sort(configs);
        ArrayList<Configuration> copy = new ArrayList<>(configs);
        return copy;
    }
}
