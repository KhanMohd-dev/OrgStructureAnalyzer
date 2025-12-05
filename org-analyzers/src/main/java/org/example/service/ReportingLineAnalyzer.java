package org.example.service;

import org.example.model.Employee;
import org.example.model.ReportingLineViolation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Service that analyzes reporting-line depth for employee.
 * ---
 * For each employee, count how many managers are between them and CEO
 * if this count is greater than 4, the reporting line is considered too long
 * Create a ReportingViolation containing the count and how many levels too deep.
 * ---
 * Traversal is done from CEO downward to avoid repeatedly scanning the entire graph
 */
public class ReportingLineAnalyzer {

    private static final int MAX_MANAGERS_BETWEEN = 4;

    // analyzes starting tree from CEO
    public List<ReportingLineViolation> analyze(Employee ceo) {
        List<ReportingLineViolation> result = new ArrayList<>();

        Deque<Employee> stack = new ArrayDeque<>();
        stack.push(ceo);

        while (!stack.isEmpty()) {
            Employee current = stack.pop();
            for (Employee child : current.getSubordinates()) {
                int managersBetween = countManagersBetween(child);
                if (managersBetween > MAX_MANAGERS_BETWEEN) {
                    int extra = managersBetween - MAX_MANAGERS_BETWEEN;
                    result.add(new ReportingLineViolation(child, managersBetween, extra));
                }
                stack.push(child);
            }
        }

        return result;
    }

    // counts how many managers are between the given employee and CEO
    int countManagersBetween(Employee employee) {
        int count = 0;
        Employee m = employee.getManager();

        while (m != null && !m.isCeo()) {
            count++;
            m = m.getManager();
        }

        return count;
    }
}
