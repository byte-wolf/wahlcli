package logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private final String logfilePath;

    public Logger (String logfilePath) throws IOException {
        this.logfilePath = logfilePath;

        // Checking if the path to the file exists and clearing the file
        try (PrintWriter logfile = new PrintWriter(new FileWriter(this.logfilePath, false));) {}
    }

    public void log(String message, Severity severity) {
        String logfileMessage;

        logfileMessage = String.format("%s %s", severity, message);

        try (PrintWriter logfile = new PrintWriter(new FileWriter(this.logfilePath, true));) {

            logfile.println(logfileMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {
        this.log(message, Severity.INFO);
    }

    public enum Severity {
        INFO("[INFO]"), WARNING("[Warning]"), ERROR("[ERROR]");

        private final String output;

        Severity(String output) {
            this.output = output;
        }

        public String toString() {
            return this.output;
        }
    }
}
