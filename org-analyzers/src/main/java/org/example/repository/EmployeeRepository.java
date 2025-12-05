package org.example.repository;

import org.example.model.Organization;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Abstraction for loading organization data from some data source.
 * ---
 * For this assignment, there is a single implementation that reads from CSV file. The interface
 * keeps the app loosely coupled to the source, and allows easy replacement with another source in the future.
 *
 */
public interface EmployeeRepository {
    Organization read(Path path) throws IOException;
}
