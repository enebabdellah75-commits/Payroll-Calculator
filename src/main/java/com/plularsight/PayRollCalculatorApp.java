package com.plularsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PayRollCalculatorApp {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter the name of the file to process: ");
        String inputFileName = input.nextLine().trim();

        System.out.print("Enter the name of the payroll file to create: ");
        String outputFileName = input.nextLine().trim();

        ArrayList<Employee> employees = new ArrayList<>();

        // ---------- READ the employees ----------
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + inputFileName))) {

            // Skip the header row (id|name|hours-worked|pay-rate)
            // Remove this line if your file has no header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] tokens = line.split("\\|");

                int employeeId = Integer.parseInt(tokens[0].trim());
                String name = tokens[1].trim();
                double hoursWorked = Double.parseDouble(tokens[2].trim());
                double payRate = Double.parseDouble(tokens[3].trim());

                Employee employee = new Employee(employeeId, name, hoursWorked, payRate);

                // Save the employee in the list
                employees.add(employee);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Bad number in file: " + e.getMessage());
        }

        // ---------- WRITE the payroll file ----------
        String outputPath = "src/main/resources/" + inputFileName;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {

            if (outputFileName.toLowerCase().endsWith(".json")) {
                // BONUS: JSON format
                bw.write("[");
                bw.newLine();
                for (int i = 0; i < employees.size(); i++) {
                    Employee e = employees.get(i);
                    bw.write(String.format("  { \"id\": %d, \"name\": \"%s\", \"grossPay\": %.2f }",
                            e.getEmployeeId(), e.getName(), e.getGrossPay()));
                    if (i < employees.size() - 1) {
                        bw.write(",");
                    }
                    bw.newLine();
                }
                bw.write("]");
            } else {
                // CSV format
                bw.write("id|name|gross pay");
                bw.newLine();
                for (Employee e : employees) {
                    bw.write(String.format("%d|%s|%.2f", e.getEmployeeId(), e.getName(), e.getGrossPay()));
                    bw.newLine();
                }
            }

            System.out.println("Payroll written to " + outputPath);

        } catch (IOException ex) {
            System.out.println("Error writing file: " + ex.getMessage());
        }

        input.close();
    }
}