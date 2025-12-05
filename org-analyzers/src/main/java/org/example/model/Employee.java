package org.example.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Domain model representing a single employee in the org.
 * This class also exposes helpers like (isManager()) and (isCeo()) to simplify business logic
 *
 */
public class Employee {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final Integer managerIdRaw; // from CSV, may be null
    private final List<Employee> subordinates = new ArrayList<>();
    private Employee manager;

    public Employee(int id,
                    String firstName,
                    String lastName,
                    BigDecimal salary,
                    Integer managerIdRaw) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerIdRaw = managerIdRaw;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Integer getManagerIdRaw() {
        return managerIdRaw;
    }

    public Employee getManager() {
        return manager;
    }

    public List<Employee> getSubordinates() {
        return Collections.unmodifiableList(subordinates);
    }

    public void addSubordinate(Employee subordinate) {
        this.subordinates.add(subordinate);
    }

    // return true if this employee has at least one direct subordinate, meaning they are consdered
    // a manager for the salary rules.
    public boolean isManager() {
        return !subordinates.isEmpty();
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    // return true if this employee is the CEO i.e. has no manager.
    public boolean isCeo() {
        return manager == null;
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName;
    }
}
