package satsolving;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private long[] positiveOccurrence;
    private long[] negativeOccurrence;
    private long numberOfClausesInProblemLine;
    private long numberOfClauses;
    private List<Integer> unitClauses = new ArrayList<>();

    public void setNumberOfVariables(int variables) {
        positiveOccurrence = new long[variables + 1];
        negativeOccurrence = new long[variables + 1];
    }

    public void countVariable(int variable) {
        if (variable > 0) {
            positiveOccurrence[variable]++;
        } else {
            negativeOccurrence[-variable]++;
        }
    }

    public void countClause(int[] clause) {
        numberOfClauses++;
        if (clause.length == 1) {
            unitClauses.add(clause[0]);
        }
    }

    public void print() {
        List<Integer> purePositive = new ArrayList<>();
        List<Integer> pureNegative = new ArrayList<>();
        List<Integer> mostFrequentlyVariables = new ArrayList<>();

        long mostFrequentlyVariableOccurrence = 0;

        long literalCount = 0;
        for (int i = 0; i < positiveOccurrence.length; i++) {
            long occurrence = positiveOccurrence[i] + negativeOccurrence[i];
            if (occurrence == mostFrequentlyVariableOccurrence) {
                mostFrequentlyVariables.add(i);
            } else if (occurrence > mostFrequentlyVariableOccurrence) {
                mostFrequentlyVariables.clear();
                mostFrequentlyVariables.add(i);
                mostFrequentlyVariableOccurrence = occurrence;
            }
            if (positiveOccurrence[i] != 0 && negativeOccurrence[i] == 0) {
                purePositive.add(i);
            } else if (positiveOccurrence[i] == 0 && negativeOccurrence[i] != 0) {
                pureNegative.add(i);
            }
            literalCount += (positiveOccurrence[i] != 0 ? 1 : 0) + (negativeOccurrence[i] != 0 ? 1 : 0);
        }
        System.out.println("Problem line: #vars = " + (positiveOccurrence.length - 1) + ", #clauses = " + numberOfClausesInProblemLine);
        System.out.println("Clauses count: " + numberOfClauses);
        System.out.println("Literal count: " + literalCount);
        System.out.println("Maximal occurrence of a variable: " + mostFrequentlyVariableOccurrence);
        System.out.println("Variables with maximum number of occurrence: " + mostFrequentlyVariables);
        System.out.println("Positive pure literals: " + purePositive);
        System.out.println("Negative pure literals: " + pureNegative);
        System.out.println("Unit clauses: " + unitClauses);
    }

    public void setNumberOfClauses(int numberOfClauses) {
        this.numberOfClausesInProblemLine = numberOfClauses;
    }
}
