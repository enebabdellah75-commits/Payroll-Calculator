package com.plularsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class PayRollCalculatorApp {
    public static void main(String[] args) {

        // try-with-resources closes the reader automatically
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/employecvs"))) {

            // Skip the header row (id|name|hours-worked|pay-rate)
            // Remove this line if your file has no header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {

                // Skip any blank lines
                if (line.isBlank()) {
                    continue;
                }

                // Split the line on the | character
                String[] tokens = line.split("\\|");

                // Copy the tokens into variables
                int employeeId = Integer.parseInt(tokens[0].trim());
                String name = tokens[1].trim();
                double hoursWorked = Double.parseDouble(tokens[2].trim());
                double payRate = Double.parseDouble(tokens[3].trim());

                // Create the Employee object
                Employee employee = new Employee(employeeId, name, hoursWorked, payRate);

                // Display the employee using the getters
                System.out.printf("ID: %d | Name: %-20s | Gross Pay: $%.2f%n",
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getGrossPay());
            }

        } catch (IOException e) {
            System.out.println("Error reading employees.csv: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Bad number in employees.csv: " + e.getMessage());
        }
    }
}

