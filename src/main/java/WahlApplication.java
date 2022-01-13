import logging.Logger;
import model.Kandidat;
import model.Wahl;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.TreeSet;

public class WahlApplication {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("000");
    private static Logger logger;

    private static Mode mode = Mode.DEFAULT;

    public static void main(String[] args) {

        WahlApplication.printWelcomeMessage();

        try {

            if (WahlApplication.checkInputParams(args)) {
                WahlApplication.logger = new Logger(args[0]);
                Wahl wahl = new Wahl(WahlApplication.getCandidates());

                WahlApplication.conductElection(wahl);

            } else {
                System.out.println("Keine Logdatei");
                System.out.println("Usage: java model.Wahl pathToLog");
            }

        } catch (IOException ioe) {
            System.out.println("Unable to write to the file.");
            System.out.println(ioe.getMessage());
        }
    }

    private static void conductElection(Wahl wahl) {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));) {

            WahlApplication.readInputsAndCastVotes(bufferedReader, wahl);

        } catch (IOException ioe) {
            System.err.println("Error: " + ioe.getMessage());
            WahlApplication.logger.log(ioe.getMessage(), Logger.Severity.ERROR);
        }
    }

    private static void readInputsAndCastVotes(BufferedReader bufferedReader, Wahl wahl) throws IOException {

        int idx = 1;

        System.out.print(WahlApplication.DECIMAL_FORMAT.format(idx) + " >");
        String consoleInput = bufferedReader.readLine();

        while (WahlApplication.checkInput(consoleInput)) {

            WahlApplication.logger.log(WahlApplication.DECIMAL_FORMAT.format(idx) + " >" + consoleInput, Logger.Severity.INFO);

            idx += WahlApplication.interpretCommand(wahl, consoleInput, bufferedReader);

            System.out.print(WahlApplication.DECIMAL_FORMAT.format(idx) + " >");
            consoleInput = bufferedReader.readLine();
        }
    }

    private static int interpretCommand(Wahl wahl, String consoleInput, BufferedReader bufferedReader) throws IOException {
        String [] parts = consoleInput.trim().split(" ");
        int voteAdded = 0;

        if ("remove".equals(parts[0])) {
            WahlApplication.removeVote(wahl, parts[1]);
        } else if ("change".equals(parts[0])) {
            WahlApplication.changeVote(wahl, parts[1], bufferedReader);
        } else if ("help".equals(parts[0])) {
            WahlApplication.printHelp();
        } else {
            voteAdded = WahlApplication.castVote(wahl, consoleInput);
        }

        return voteAdded;
    }

    private static void removeVote(Wahl wahl, String part) {
        try {
            wahl.removeVote(WahlApplication.getIndexFromInputParameter(part));
            WahlApplication.logResults(wahl);

        } catch (NumberFormatException e) {
            WahlApplication.logger.log("Command usage: First parameter must be an Integer.", Logger.Severity.WARNING);
            System.out.println("Command usage: First parameter must be an Integer.");
        } catch (IndexOutOfBoundsException | IllegalArgumentException exception) {
            WahlApplication.logger.log(String.format("Command Error: %s", exception.getMessage()), Logger.Severity.WARNING);
            System.out.printf("Command Error: %s%n", exception.getMessage());
        }
    }

    private static void changeVote(Wahl wahl, String part, BufferedReader bufferedReader) throws IOException {
        try {
            int index = WahlApplication.getIndexFromInputParameter(part);
            WahlApplication.checkIndexAndGetInput(wahl, index, bufferedReader);

        } catch (NumberFormatException e) {
            WahlApplication.logger.log("Wrong command usage: First parameter must be an Integer.", Logger.Severity.WARNING);
        } catch (IndexOutOfBoundsException | IllegalArgumentException exception) {
            WahlApplication.logger.log(String.format("Command Error: %s", exception.getMessage()), Logger.Severity.WARNING);
            System.out.printf("Command Error: %s%n", exception.getMessage());
        }
    }

    private static int getIndexFromInputParameter(String param) throws NumberFormatException {
        return Integer.parseInt(param) - 1;
    }

    private static void checkIndexAndGetInput(Wahl wahl, int index, BufferedReader bufferedReader) throws IOException {

        if (index < 0 || index >= wahl.getNumberOfVotes()) {
            WahlApplication.logger.log(String.format("Command Error: %s", "Index is out of Bounds"), Logger.Severity.WARNING);
            System.out.printf("Command Error: %s%n", "Index is out of Bounds");
        } else {
            WahlApplication.readInputForIndex(wahl, index, bufferedReader);
        }
    }

    private static void readInputForIndex(Wahl wahl, int index, BufferedReader bufferedReader) throws IOException {

        int readableIndex = index + 1;

        System.out.println("Entering patch mode for index " + (readableIndex)+". Type 'quit' to exit patch mode.");
        System.out.print(WahlApplication.DECIMAL_FORMAT.format(readableIndex) + " (PATCH MODE) >");
        String consoleInput = bufferedReader.readLine();

        boolean repeat = WahlApplication.checkInput(consoleInput);

        while (repeat) {
            WahlApplication.logger.log(WahlApplication.DECIMAL_FORMAT.format(readableIndex) + " (PATCH MODE) >" + consoleInput, Logger.Severity.INFO);

            try {
                System.out.println(index+" "+ consoleInput);
                wahl.changeVote(index, consoleInput);
                WahlApplication.logResults(wahl);
                repeat = false;
            } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
                System.out.println(exception.getMessage());
                WahlApplication.logger.log("Falsche Eingabe!", Logger.Severity.WARNING);
                System.out.println("     Falsche Eingabe!");

                System.out.print(WahlApplication.DECIMAL_FORMAT.format(readableIndex) + " (PATCH MODE) >");
                consoleInput = bufferedReader.readLine();
                repeat = WahlApplication.checkInput(consoleInput);
            }
        }
    }

    private static boolean checkInput(String inputSequence) {
        return inputSequence != null && !"quit".equals(inputSequence);
    }

    private static int castVote(Wahl wahl, String inputSequence) {
        int voteAdded = 0;

        try {
            WahlApplication.selectVotingMethod(wahl, inputSequence);
            voteAdded = 1;
            WahlApplication.logResults(wahl);

        } catch (IllegalArgumentException iae) {
            WahlApplication.logger.log("Falsche Eingabe!", Logger.Severity.WARNING);
            System.out.println("     Falsche Eingabe!");
        }

        return voteAdded;
    }

    private static void logResults(Wahl wahl) {
        String results;

        if (WahlApplication.mode == Mode.LEGACY) {
            results = wahl.getLegacyStringResults();
        } else {
            results = wahl.toString();
        }

        WahlApplication.logger.log("\n" + results, Logger.Severity.INFO);
        System.out.println("Current standings: \n" + results);
    }

    private static void selectVotingMethod(Wahl wahl, String inputSequence) {
        if (WahlApplication.mode == Mode.LEGACY) {
            wahl.voteByFirstCharacters(inputSequence);
        } else {
            wahl.voteByIndices(inputSequence);
        }
    }

    private static Kandidat[] getLegacyCandidates() {
        Kandidat[] candidateList = new Kandidat[5];

        candidateList[0] = new Kandidat("Dominik Hofmann");
        candidateList[1] = new Kandidat("Kilian Prager");
        candidateList[2] = new Kandidat("Niklas Hochstöger");
        candidateList[3] = new Kandidat("Paul Pfiel");
        candidateList[4] = new Kandidat("Raid Alarkhanov");

        return candidateList;
    }

    private static Set<Kandidat> getCandidates() {
        Set<Kandidat> candidates = new TreeSet<>();

        candidates.add(new Kandidat("d", "Dominik Hofmann"));
        candidates.add(new Kandidat("k", "Kilian Prager"));
        candidates.add(new Kandidat("nh", "Niklas Hochstöger"));
        candidates.add(new Kandidat("p", "Paul Pfiel"));
        candidates.add(new Kandidat("r", "Raid Alarkhanov"));
        candidates.add(new Kandidat("nh", "Niklas Haiden"));
        candidates.add(new Kandidat("ms", "Michael Schally"));
        candidates.add(new Kandidat("f", "Fabian Weichselbaum"));
        candidates.add(new Kandidat("s", "Sebastian Vettel"));
        candidates.add(new Kandidat("e", "Ein Käsebrot"));
        candidates.add(new Kandidat("l", "Lewis Hamilton"));
        candidates.add(new Kandidat("mv", "Max Verstappen"));

        return candidates;
    }

    private static boolean checkInputParams(String[] args) {
        return (args.length == 1);
    }

    private static void printWelcomeMessage() {
        System.out.println(
                " __      __ _    _  _  _    \n" +
                " \\ \\    / //_\\  | || || |   \n" +
                "  \\ \\/\\/ // _ \\ | __ || |__ \n" +
                "   \\_/\\_//_/ \\_\\|_||_||____|\n");
        System.out.println("For a list of commands type 'help' and press ENTER.\n");
    }

    private static void printHelp() {
        System.out.println("Available Commands:");
        System.out.println("\thelp\t\t\t\tdisplays this help section");
        System.out.println("\tremove [INDEX]\t\tremoves a prior vote corresponding to the provided INDEX");
        System.out.println("\tchange [INDEX]\t\tlets you change the prior vote corresponding to the provided INDEX");
    }

    enum Mode {
        LEGACY, DEFAULT
    }
}
