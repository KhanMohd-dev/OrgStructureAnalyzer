package org.example.service;

import org.example.model.Employee;
import org.example.model.SalaryViolation;
import org.example.model.ViolationType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaryAnalyzerTest {

    private final SalaryAnalyzer analyzer = new SalaryAnalyzer();

    @Test
    void managerWithinAllowedRange_hasNoViolations() {
        Employee manager = employee(1, "Manager", "Ok", 50000);
        Employee s1 = employee(2, "Sub", "One", 40000);
        Employee s2 = employee(3, "Sub", "Two", 40000);

        wire(manager, s1, s2);

        Collection<Employee> all = Arrays.asList(manager, s1, s2);
        List<SalaryViolation> violations = analyzer.analyze(all);

        assertTrue(violations.isEmpty(), "No violations expected");
    }

    @Test
    void managerExactlyAtLowerBound_hasNoViolation() {
        Employee manager = employee(1, "Manager", "LowerBound", 48000); // 40000 * 1.20
        Employee s1 = employee(2, "Sub", "One", 40000);
        Employee s2 = employee(3, "Sub", "Two", 40000);

        wire(manager, s1, s2);

        List<SalaryViolation> violations =
                analyzer.analyze(Arrays.asList(manager, s1, s2));

        assertTrue(violations.isEmpty());
    }

    @Test
    void managerUnderpaid_isFlaggedWithCorrectDifference() {
        Employee manager = employee(1, "Manager", "Under", 45000);
        Employee s1 = employee(2, "Sub", "One", 40000);
        Employee s2 = employee(3, "Sub", "Two", 40000);

        wire(manager, s1, s2);

        List<SalaryViolation> violations =
                analyzer.analyze(Arrays.asList(manager, s1, s2));

        assertEquals(1, violations.size());
        SalaryViolation v = violations.get(0);

        assertEquals(ViolationType.UNDERPAID, v.getType());
        assertEquals(manager, v.getManager());
        assertEquals(new BigDecimal("40000.00"), v.getAvgSubordinateSalary());
        assertEquals(new BigDecimal("3000.00"), v.getDifference());
    }

    @Test
    void managerOverpaid_isFlaggedWithCorrectDifference() {
        Employee manager = employee(1, "Manager", "Over", 65000);
        Employee s1 = employee(2, "Sub", "One", 40000);
        Employee s2 = employee(3, "Sub", "Two", 40000);

        wire(manager, s1, s2);

        List<SalaryViolation> violations =
                analyzer.analyze(Arrays.asList(manager, s1, s2));

        assertEquals(1, violations.size());
        SalaryViolation v = violations.get(0);

        assertEquals(ViolationType.OVERPAID, v.getType());
        assertEquals(manager, v.getManager());
        assertEquals(new BigDecimal("40000.00"), v.getAvgSubordinateSalary());
        assertEquals(new BigDecimal("5000.00"), v.getDifference());
    }

    @Test
    void employeeWithoutSubordinates_isNotConsideredManager() {
        Employee individual = employee(1, "Solo", "Dev", 50000);
        List<SalaryViolation> violations = analyzer.analyze(List.of(individual));
        assertTrue(violations.isEmpty());
    }

    // helpers

    private Employee employee(int id, String first, String last, int salary) {
        return new Employee(id, first, last,
                BigDecimal.valueOf(salary).setScale(2), null);
    }

    private void wire(Employee manager, Employee... subs) {
        for (Employee s : subs) {
            s.setManager(manager);
            manager.addSubordinate(s);
        }
    }
}
