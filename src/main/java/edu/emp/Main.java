package edu.emp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        EmployeeService service = context.getBean(EmployeeService.class);

        service.addEmployee(101, "ANAND", "CSE");
        service.addEmployee(102, "Divya", "CSE");
        service.addEmployee(103, "JAYA", "ECE");

        System.out.println("All Employees");
        service.getAllEmployees().forEach(System.out::println);

        System.out.println("\nFind Employee 102:");
        System.out.println(service.getEmployee(102));

        System.out.println("\nDelete Employee 101:");
        System.out.println(service.removeEmployee(101));

        System.out.println("\nAll Employees After Delete:");
        service.getAllEmployees().forEach(System.out::println);
    }
}
