package org.example.service;

import org.example.model.Employee;
import org.example.model.SalaryViolation;
import org.example.model.ViolationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Services that analyzes manager salaries relative to their direct subordinates.
 * ---
 * Compute the average salary of each manager's direct subordinates.
 * Manager should earn between 20% and 50% more than the average.
 * ----
 * If below 20% above average -> create a salaryViolation of type UNDERPAID
 * if above 50% above average -> create a salaryViolation of type OVERPAID
 */
public class SalaryAnalyzer {

    private static final BigDecimal MIN_MULTIPLIER = new BigDecimal("1.20");
    private static final BigDecimal MAX_MULTIPLIER = new BigDecimal("1.50");

    // analyzes all employess and returns salary violations for those that are managers.
    public List<SalaryViolation> analyze(Collection<Employee> employees) {
        List<SalaryViolation> result = new ArrayList<>();

        for (Employee manager : employees) {
            if (manager.getSubordinates().isEmpty()) {
                continue;
            }

            BigDecimal avg = averageSalary(manager.getSubordinates());
            BigDecimal minAllowed = avg.multiply(MIN_MULTIPLIER);
            BigDecimal maxAllowed = avg.multiply(MAX_MULTIPLIER);

            BigDecimal salary = manager.getSalary();

            if (salary.compareTo(minAllowed) < 0) {
                BigDecimal diff = minAllowed.subtract(salary).setScale(2, RoundingMode.HALF_UP);
                result.add(new SalaryViolation(manager, ViolationType.UNDERPAID, diff, avg));
            } else if (salary.compareTo(maxAllowed) > 0) {
                BigDecimal diff = salary.subtract(maxAllowed).setScale(2, RoundingMode.HALF_UP);
                result.add(new SalaryViolation(manager, ViolationType.OVERPAID, diff, avg));
            }
        }

        return result;
    }

    // computes the average salary of the given employees
    private BigDecimal averageSalary(List<Employee> employees) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Employee e : employees) {
            sum = sum.add(e.getSalary());
        }

        return sum.divide(BigDecimal.valueOf(employees.size()), 2, RoundingMode.HALF_UP);
    }
}
