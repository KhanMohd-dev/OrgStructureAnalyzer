package org.example.repository;

import org.example.model.Employee;
import org.example.model.Organization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeCsvReaderTest {

    @TempDir
    Path tempDir;

    private final EmployeeCsvReader reader = new EmployeeCsvReader();

    @Test
    void parsesCsvAndBuildsHierarchyCorrectly() throws IOException {
        Path csv = tempDir.resolve("employees.csv");

        String content = String.join(System.lineSeparator(),
                "Id,firstName,lastName,salary,managerId",
                "1,Joe,Doe,60000,",
                "2,Martin,Chekov,45000,1",
                "3,Francisca,Martinez,43000,1"
        );

        Files.writeString(csv, content);

        Organization org = reader.read(csv);

        assertNotNull(org.getCeo());
        assertEquals(1, org.getCeo().getId());
        assertEquals("Joe", org.getCeo().getFirstName());
        assertEquals(0, new BigDecimal("60000.00")
                .compareTo(org.getCeo().getSalary()));

        assertEquals(3, org.getEmployeesById().size());

        Employee martin = org.getEmployeesById().get(2);
        assertEquals(org.getCeo(), martin.getManager());
        assertTrue(org.getCeo().getSubordinates().contains(martin));

        Employee francisca = org.getEmployeesById().get(3);
        assertEquals(org.getCeo(), francisca.getManager());
        assertTrue(org.getCeo().getSubordinates().contains(francisca));
    }

    @Test
    void throwsIfManagerIdDoesNotExist() throws IOException {
        Path csv = tempDir.resolve("bad-employees.csv");

        String content = String.join(System.lineSeparator(),
                "Id,firstName,lastName,salary,managerId",
                "1,Joe,Doe,60000,",
                "2,Bad,Report,40000,99"
        );

        Files.writeString(csv, content);

        assertThrows(IllegalStateException.class, () -> reader.read(csv));
    }

    @Test
    void throwsIfMultipleCeoRowsExist() throws IOException {
        Path csv = tempDir.resolve("two-ceos.csv");

        String content = String.join(System.lineSeparator(),
                "Id,firstName,lastName,salary,managerId",
                "1,Joe,Doe,60000,",
                "2,Other,Ceo,65000,"
        );

        Files.writeString(csv, content);

        assertThrows(IllegalStateException.class, () -> reader.read(csv));
    }
}
