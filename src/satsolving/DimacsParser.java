package satsolving;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimacsParser {
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
        System.out.println(new File(args [0]).getAbsolutePath());
        new DimacsParser().read(new FileInputStream(args[0]));
    }

    protected void read(InputStream in) throws IOException {
        int input;
        while ((input = in.read()) != -1) {
            switch ((char) input) {
                case 'c':
                    while (true) {
                        if (in.read() == '\n') {
                            break;
                        }
                    }
                    break;
                case 'p':
                    parseHeader(in);
                    List<Integer> clauseList = new ArrayList<>();
                    Integer number;
                    while ((number = readNumber(in)) != null) {
                        int variable = number;
                        if (variable == 0) {
                            int[] clauses = new int[clauseList.size()];
                            for (int i = 0; i < clauseList.size(); i++) {
                                clauses[i] = clauseList.get(i);
                            }
                            statistics.countClause(clauses);
                        } else {
                            clauseList.add(variable);
                            statistics.countVariable(variable);
                        }
                    }
                    statistics.print();
                    return;
                default:
                    throw new IllegalStateException("Unknown state for input: " + (char) input);
            }
        }
        throw new EOFException();
    }

    private void parseHeader(InputStream in) throws IOException {
        String type = readType(in);
        int numberOfVariables = readNumber(in);
        int numberOfClauses = readNumber(in);

        statistics.setNumberOfVariables(numberOfVariables);
        statistics.setNumberOfClauses(numberOfClauses);
    }

    private String readType(InputStream in) throws IOException {
        return String.valueOf(skipSpaces(in)) + (char) in.read() + (char) in.read();
    }

    private Integer readNumber(InputStream in) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append(skipSpaces(in));
        if (buffer.toString().length() == 1 && !Character.isDigit(buffer.toString().charAt(0))) {
            return null;
        }

        char c;
        while ((c = (char) in.read()) != -1) {
            if (Character.isDigit(c)) {
                buffer.append(c);
            } else {
                break;
            }
        }
        return Integer.parseInt(buffer.toString());
    }

    private char skipSpaces(InputStream in) throws IOException {
        char c;
        while ((c = (char) in.read()) != -1) {
            if (c != ' ' && c != '\t') {
                return c;
            }
        }
        return c;
    }
}
