package org.example.service;

import org.example.model.ReportingLineViolation;
import org.example.model.SalaryViolation;
import org.example.model.ViolationType;

import java.util.List;

/**
 * Services responsible for printing analysis results to the console.
 */
public class ReportPrinter {

    // prints
    // Managers earning less than they should
    // Managers earning more than they should
    public void printSalaryViolations(List<SalaryViolation> violations) {
        System.out.println("=========================================================");
        System.out.println("Managers earning less than they should:");
        System.out.println();
        violations.stream()
                .filter(v -> v.getType() == ViolationType.UNDERPAID)
                .forEach(v -> System.out.printf(
                        "- %d %s %s: underpaid by %s (avg subordinate salary: %s)%n",
                        v.getManager().getId(),
                        v.getManager().getFirstName(),
                        v.getManager().getLastName(),
                        v.getDifference(),
                        v.getAvgSubordinateSalary()
                ));


        System.out.println("=========================================================");
        System.out.println("Managers earning more than they should:");
        System.out.println();
        violations.stream()
                .filter(v -> v.getType() == ViolationType.OVERPAID)
                .forEach(v -> System.out.printf(
                        "- %d %s %s: overpaid by %s (avg subordinate salary: %s)%n",
                        v.getManager().getId(),
                        v.getManager().getFirstName(),
                        v.getManager().getLastName(),
                        v.getDifference(),
                        v.getAvgSubordinateSalary()
                ));
        System.out.println("=========================================================");

    }

    // prints employees where reporting lines are too long.
    public void printReportingViolations(List<ReportingLineViolation> violations) {
        System.out.println("Employees with reporting lines that are too long:");
        System.out.println();
        for (ReportingLineViolation v : violations) {
            System.out.printf(
                    "- %d %s %s: %d levels too deep (has %d managers between them and the CEO)%n",
                    v.getEmployee().getId(),
                    v.getEmployee().getFirstName(),
                    v.getEmployee().getLastName(),
                    v.getExtraLevels(),
                    v.getManagersBetween()
            );
        }
        System.out.println("=========================================================");
    }
}
