package org.example.model;

/**
 * Value object representing a reporting-line length violation for an employee.
 * Contains:
 * the employee whose reporting line is too long.
 * total number of managers between this employee and  CEO.
 * how many levels above the allowed maximum (4) this employee exceeds.
 */
public class ReportingLineViolation {

    private final Employee employee;
    private final int managersBetween;
    private final int extraLevels;

    public ReportingLineViolation(Employee employee,
                                  int managersBetween,
                                  int extraLevels) {
        this.employee = employee;
        this.managersBetween = managersBetween;
        this.extraLevels = extraLevels;
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getManagersBetween() {
        return managersBetween;
    }

    public int getExtraLevels() {
        return extraLevels;
    }
}
