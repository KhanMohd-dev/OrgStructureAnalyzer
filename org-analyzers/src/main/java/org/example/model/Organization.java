package org.example.model;

import java.util.Collections;
import java.util.Map;

/**
 * Aggregate root representing the entire organization.
 * The Employee that is the CEO
 * A map of all employees by id for efficient lookups.
 * --
 * Easy to navigate to pass a single object to services that need to navigate the org chart.
 */
public class Organization {

    private final Employee ceo;
    private final Map<Integer, Employee> employeesById;

    public Organization(Employee ceo, Map<Integer, Employee> employeesById) {
        this.ceo = ceo;
        this.employeesById = employeesById;
    }

    public Employee getCeo() {
        return ceo;
    }

    public Map<Integer, Employee> getEmployeesById() {
        return Collections.unmodifiableMap(employeesById);
    }
}
