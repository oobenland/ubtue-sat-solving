package satsolving;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimacsParser {
    /**
     * For counting variables, clauses, etc...
     */
    private Statistics statistics = new Statistics();

    private static void printHelp() {
        System.out.println("java satsolving.DimacsParser dimacs-file");
        System.out.println("dimacs-file      A file in dimacs format");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Wrong argument count!\n");
            printHelp();
        }
        System.out.println(new File(args[0]).getAbsolutePath());
        new DimacsParser().read(new FileInputStream(args[0]));
    }

    /**
     * Reads a dimacs file.
     * <p>
     * TODO: return the formula.
     *
     * @param in Input stream to read from.
     * @throws IOException {@see InputStream#read}
     */
    protected void read(InputStream in) throws IOException {
        int input;
        while ((input = in.read()) != -1) {
            switch ((char) input) {
                case 'c':
                    ignoreComments(in);
                    break;
                case 'p':
                    parseFormula(in);
                    statistics.print();
                    return;
                default:
                    throw new IllegalStateException("Unknown state for input: " + (char) input);
            }
        }
        throw new EOFException();
    }

    /**
     * Reads the full formula from input stream
     * <p>
     * TODO: return the formula.
     *
     * @param in The input stream to read from
     * @throws IOException {@see InputStream#read}
     */
    private void parseFormula(InputStream in) throws IOException {
        parseHeader(in);
        parseClauses(in);
    }

    /**
     * Reads all the comments and discards them.
     *
     * @param in Input stream to read from.
     * @throws IOException {@see InputStream#read}
     */
    private void ignoreComments(InputStream in) throws IOException {
        while (true) {
            if (in.read() == '\n') {
                break;
            }
        }
    }

    /**
     * Reads the header from input stream and parses it.
     * <p>
     * TODO: return the header.
     *
     * @param in Input stream to read from.
     * @throws IOException {@see InputStream#read}
     */
    private void parseHeader(InputStream in) throws IOException {
        String type = readType(in);
        Integer numberOfVariables = readNumber(in);
        if (numberOfVariables != null) {
            statistics.setNumberOfVariables(numberOfVariables);
        } else {
            throw new IllegalArgumentException("Header: Number of variables is not readable.");
        }
        Integer numberOfClauses = readNumber(in);
        if (numberOfClauses != null) {
            statistics.setNumberOfClauses(numberOfClauses);
        } else {
            throw new IllegalArgumentException("Header: Number of clauses is not readable.");
        }
    }

    /**
     * Reads all clauses from input stream and parses them.
     * <p>
     * TODO: return the clauses.
     *
     * @param in Input stream to read from.
     * @throws IOException {@see InputStream#read}
     */
    private void parseClauses(InputStream in) throws IOException {
        List<Integer> clauseList = new ArrayList<>();
        Integer number;
        int variable;
        while (true) {
            number = readNumber(in);
            if (number == null) {
                if (!clauseList.isEmpty()) {
                    int[] clauses = integerListToIntArray(clauseList);
                    statistics.countClause(clauses);
                }
                return;
            }
            variable = number;
            if (variable == 0) {
                int[] clauses = integerListToIntArray(clauseList);
                statistics.countClause(clauses);
                clauseList.clear();
            } else {
                clauseList.add(variable);
                statistics.countVariable(variable);
            }
        }
    }

    /**
     * Converts a list of Integer to an array of int.
     *
     * @param list the list.
     * @return the array.
     */
    private int[] integerListToIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Reads the three character type string from input stream.
     *
     * @param in Input stream to read from.
     * @return The type string.
     * @throws IOException {@see InputStream#read}
     */
    private String readType(InputStream in) throws IOException {
        return String.valueOf(skipSpaces(in)) + (char) in.read() + (char) in.read();
    }

    /**
     * Reads a number from input stream by first skipping all whitespaces and then reading some characters
     * until the next whitespace occurs.
     *
     * @param in Input stream to read from.
     * @return The read number or NULL on EOF.
     * @throws IOException {@see InputStream#read}
     */
    private Integer readNumber(InputStream in) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append(skipSpaces(in));
        String currentContent = buffer.toString().trim();
        if (currentContent.length() == 1 && !Character.isDigit(currentContent.charAt(0))) {
            return null;
        }

        int input;
        char c;
        while ((input = in.read()) != -1) {
            c = (char) input;
            if (Character.isDigit(c)) {
                buffer.append(c);
            } else {
                break;
            }
        }
        return Integer.parseInt(buffer.toString());
    }

    /**
     * Skips all whitespaces while reading from input stream in.
     * The first non-whitespace character will be returned.
     * <p>
     * {@see Character#isWhitespace}
     *
     * @param in Input stream to read from.
     * @return the first non-whitespace character.
     * @throws IOException {@see InputStream#read}
     */
    private char skipSpaces(InputStream in) throws IOException {
        int input;
        char c = 0;
        while ((input = in.read()) != -1) {
            c = (char) input;
            if (!Character.isWhitespace(c)) {
                return c;
            }
        }
        return c;
    }
}
