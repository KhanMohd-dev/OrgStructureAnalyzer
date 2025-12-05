package org.example.repository;

import org.example.model.Employee;
import org.example.model.Organization;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV backed implementation of EmployeeRepository
 *
 * Read all lines from CSV file
 * Parse each line into an Employee with raw manager id.
 * Wire manager/subordinate relationships based on those ids.
 * Identify and return the CEO and all employees as an organization.
 * the csv is assumed to have a header  row and fields.
 */
public class EmployeeCsvReader implements EmployeeRepository {

    @Override
    public Organization read(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty");
        }

        Map<Integer, Employee> employeesById = new LinkedHashMap<>();

        // Skip header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split(",");
            if (tokens.length < 4) {
                throw new IllegalArgumentException("Invalid line: " + line);
            }

            int id = Integer.parseInt(tokens[0].trim());
            String firstName = tokens[1].trim();
            String lastName = tokens[2].trim();

            BigDecimal salary = new BigDecimal(tokens[3].trim())
                    .setScale(2);

            Integer managerId = null;
            if (tokens.length > 4 && !tokens[4].trim().isEmpty()) {
                managerId = Integer.valueOf(tokens[4].trim());
            }

            Employee e = new Employee(id, firstName, lastName, salary, managerId);
            if (employeesById.putIfAbsent(id, e) != null) {
                throw new IllegalArgumentException("Duplicate employee id: " + id);
            }
        }

        Employee ceo = null;
        for (Employee e : employeesById.values()) {
            Integer mid = e.getManagerIdRaw();
            if (mid == null) {
                if (ceo != null) {
                    throw new IllegalStateException("Multiple CEOs found");
                }
                ceo = e;
            } else {
                Employee manager = employeesById.get(mid);
                if (manager == null) {
                    throw new IllegalStateException("Manager with id " + mid +
                            " not found for employee " + e.getId());
                }
                e.setManager(manager);
                manager.addSubordinate(e);
            }
        }

        if (ceo == null) {
            throw new IllegalStateException("No CEO found (no row with empty managerId)");
        }

        return new Organization(ceo, employeesById);
    }
}
