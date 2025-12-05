package org.example.service;

import org.example.model.Employee;
import org.example.model.ReportingLineViolation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportingLineAnalyzerTest {

    private final ReportingLineAnalyzer analyzer = new ReportingLineAnalyzer();

    @Test
    void directReportToCeo_hasZeroManagersBetween() {
        Employee ceo = employee(1, "CEO", "Boss");
        Employee direct = employee(2, "Direct", "Report");
        link(ceo, direct);

        int managersBetween = analyzer.countManagersBetween(direct);
        assertEquals(0, managersBetween);
    }

    @Test
    void employeeWithExactlyFourManagersBetween_isNotFlagged() {
        // CEO -> M1 -> M2 -> M3 -> M4 -> Emp
        Employee ceo = employee(1, "CEO", "Boss");
        Employee m1 = employee(2, "M", "1");
        Employee m2 = employee(3, "M", "2");
        Employee m3 = employee(4, "M", "3");
        Employee m4 = employee(5, "M", "4");
        Employee emp = employee(6, "Emp", "Ok");

        link(ceo, m1);
        link(m1, m2);
        link(m2, m3);
        link(m3, m4);
        link(m4, emp);

        int between = analyzer.countManagersBetween(emp);
        assertEquals(4, between);

        List<ReportingLineViolation> violations = analyzer.analyze(ceo);
        assertTrue(violations.isEmpty());
    }

    @Test
    void employeeWithFiveManagersBetween_isFlaggedWithExtraLevelsOne() {
        // CEO -> M1 -> M2 -> M3 -> M4 -> M5 -> Emp
        Employee ceo = employee(1, "CEO", "Boss");
        Employee m1 = employee(2, "M", "1");
        Employee m2 = employee(3, "M", "2");
        Employee m3 = employee(4, "M", "3");
        Employee m4 = employee(5, "M", "4");
        Employee m5 = employee(6, "M", "5");
        Employee emp = employee(7, "Emp", "TooDeep");

        link(ceo, m1);
        link(m1, m2);
        link(m2, m3);
        link(m3, m4);
        link(m4, m5);
        link(m5, emp);

        List<ReportingLineViolation> violations = analyzer.analyze(ceo);

        assertEquals(1, violations.size());
        ReportingLineViolation v = violations.get(0);

        assertEquals(emp, v.getEmployee());
        assertEquals(5, v.getManagersBetween());
        assertEquals(1, v.getExtraLevels());
    }

    // helpers

    private Employee employee(int id, String first, String last) {
        return new Employee(id, first, last,
                BigDecimal.valueOf(50000).setScale(2), null);
    }

    private void link(Employee manager, Employee subordinate) {
        subordinate.setManager(manager);
        manager.addSubordinate(subordinate);
    }
}
