public class ConfigurationFormatException extends Exception {
    public ConfigurationFormatException() {
        super("Invalid configuration format");
    }

    public ConfigurationFormatException(String message) {
        super(message);
    }
}
