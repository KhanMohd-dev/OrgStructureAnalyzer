package org.example;

import org.example.model.Organization;
import org.example.model.ReportingLineViolation;
import org.example.model.SalaryViolation;
import org.example.repository.EmployeeCsvReader;
import org.example.repository.EmployeeRepository;
import org.example.service.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Application entry point for the organization analyzer CLI
 * -----
 * Parse command-line argument to get the CSV file path.
 * Delegate to the repository to load employees from CSV.
 * Invoke services to analyze salaries and reporting lines.
 * Use the ReportPrinter to output results to the console.
 * ------
 */
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar org-analyzer.jar <employees.csv>");
            System.exit(1);
        }

        Path csvPath = Paths.get(args[0]);
        EmployeeRepository reader = new EmployeeCsvReader();

        Organization org;
        try {
            org = reader.read(csvPath);
        } catch (IOException e) {
            System.err.println("Failed to read CSV file: " + e.getMessage());
            System.exit(1);
            return;
        } catch (RuntimeException e) {
            System.err.println("Error while parsing CSV file: " + e.getMessage());
            System.exit(1);
            return;
        }

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        ReportingLineAnalyzer reportingLineAnalyzer = new ReportingLineAnalyzer();

        List<SalaryViolation> salaryViolations =
                salaryAnalyzer.analyze(org.getEmployeesById().values());
        List<ReportingLineViolation> depthViolations =
                reportingLineAnalyzer.analyze(org.getCeo());

        ReportPrinter printer = new ReportPrinter();
        printer.printSalaryViolations(salaryViolations);
        printer.printReportingViolations(depthViolations);
    }
}