package file;
import java.io.FileWriter;
import java.io.IOException;
public class GameLogger {
    private static final String LOG_FILE = "gameLog.txt";
    /**
     * Records important game events into a text file named gameLog.txt.
     * @param text  a text message
     */
    public static void log(String text) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(text + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}