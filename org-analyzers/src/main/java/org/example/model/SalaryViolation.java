package org.example.model;

import java.math.BigDecimal;

/**
 * Value object representing a single salary violation for a manager.
 * Contains:
 * manager - the manager whose salary is out of the allowed band.
 * type - underpaid or overpaid relative to their subordinates.
 * difference - how much the salary deviates from the nearest allowed boundary.
 * avgSubordinateSalary - the average direct-subordinates salary used in the calculation.
 */
public class SalaryViolation {

    private final Employee manager;
    private final ViolationType type;
    private final BigDecimal difference;
    private final BigDecimal avgSubordinateSalary;

    public SalaryViolation(Employee manager,
                           ViolationType type,
                           BigDecimal difference,
                           BigDecimal avgSubordinateSalary) {
        this.manager = manager;
        this.type = type;
        this.difference = difference;
        this.avgSubordinateSalary = avgSubordinateSalary;
    }

    public Employee getManager() {
        return manager;
    }

    public ViolationType getType() {
        return type;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public BigDecimal getAvgSubordinateSalary() {
        return avgSubordinateSalary;
    }
}
