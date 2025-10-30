package org.example;

import java.util.*;

/**
 * The UserInterface class provides a text-based interface for interacting with the Dealership system.
 * It allows users to perform various operations such as querying vehicles based on different criteria,
 * adding new vehicles, and removing existing ones.
 */
public class UserInterface
{
    private Dealership dealership;
    private Scanner scanner;

    public UserInterface()
    {
        scanner = new Scanner(System.in);
        init();
    }

    private void init()
    {
        DealershipFileManager fileManager = new DealershipFileManager();
        this.dealership = fileManager.getDealership("src/main/resources/inventory.csv");
    }

    public void display()
    {
        System.out.println("\u001B[0m===============================================");
        System.out.println("\u001B[32mWelcome to Apex Motors!");
        System.out.println("\u001B[32mYour destination for high-performance cars.");
        System.out.println("\u001B[0m===============================================");

        boolean quit = false;
        while (!quit)
        {
            displayMenu();
            int command = promptInt("");
            switch (command)
            {
                case 1:
                    processGetByPriceRequest();
                    break;
                case 2:
                    processGetByMakeModelRequest();
                    break;
                case 3:
                    processGetByYearRequest();
                    break;
                case 4:
                    processGetByColorRequest();
                    break;
                case 5:
                    processGetByMileageRequest();
                    break;
                case 6:
                    processGetByTypeRequest();
                    break;
                case 7:
                    processGetAllVehiclesRequest();
                    break;
                case 8:
                    processAddVehicleRequest();
                    break;
                case 9:
                    processRemoveVehicleRequest();
                    break;
                case 99:
                    System.out.println("===============================================");
                    System.out.println(" Thank you for visiting Apex Motors!");
                    System.out.println(" We appreciate your business — have a great day!");
                    System.out.println("===============================================");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("------------- MENU -------------");
        System.out.println(" [1] Find vehicles within a price range");
        System.out.println(" [2] Find vehicles by make & model");
        System.out.println(" [3] Find vehicles by year range");
        System.out.println(" [4] Find vehicles by color");
        System.out.println(" [5] Find vehicles by mileage range");
        System.out.println(" [6] Find vehicles by type");
        System.out.println(" [7] List ALL vehicles");
        System.out.println(" [8] Add a vehicle");
        System.out.println(" [9] Remove a vehicle");
        System.out.println(" [99] Quit");
        System.out.println("-------------------------------");
        System.out.print(" ➤ Choose an option: ");
    }


    private void displayVehicles(List<Vehicle> vehicles)
    {
        if (vehicles.isEmpty())
        {
            System.out.println("! No vehicles found!");
        } else
        {
            for (Vehicle v : vehicles)
            {
                System.out.println(v);
            }
        }
    }

    private void processGetByPriceRequest()
    {
        System.out.println("\n----------- Search by Price Range -----------");
        System.out.print("Min price: ");
        double min = Double.parseDouble(scanner.nextLine());
        System.out.print("Max price: ");
        double max = Double.parseDouble(scanner.nextLine());
        List<Vehicle> results = dealership.getVehiclesByPrice(min, max);
        displayVehicles(results);
    }

    private void processGetByMakeModelRequest()
    {
        System.out.println("\n----------- Search by Make & Model ----------");
        System.out.print("Make: ");
        String make = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        List<Vehicle> results = dealership.getVehiclesByMakeModel(make, model);
        displayVehicles(results);
    }

    private void processGetByYearRequest()
    {
        System.out.println("\n----------- Search by Year Range ------------");
        System.out.print("Min year: ");
        int min = Integer.parseInt(scanner.nextLine());
        System.out.print("Max year: ");
        int max = Integer.parseInt(scanner.nextLine());
        List<Vehicle> results = dealership.getVehiclesByYear(min, max);
        displayVehicles(results);
    }

    private void processGetByColorRequest()
    {
        System.out.println("\n----------- Search by Color -----------------");
        System.out.print("Color: ");
        String color = scanner.nextLine();
        List<Vehicle> results = dealership.getVehiclesByColor(color);
        displayVehicles(results);
    }

    private void processGetByMileageRequest()
    {
        System.out.println("\n----------- Search by Mileage Range ---------");
        System.out.print("Min mileage: ");
        int min = Integer.parseInt(scanner.nextLine());
        System.out.print("Max mileage: ");
        int max = Integer.parseInt(scanner.nextLine());
        List<Vehicle> results = dealership.getVehiclesByMileage(min, max);
        displayVehicles(results);
    }

    private void processGetByTypeRequest()
    {
        System.out.println("\n----------- Search by Type ------------------");
        System.out.print("Enter type (car, truck, SUV, van): ");
        String type = scanner.nextLine();
        List<Vehicle> results = dealership.getVehiclesByType(type);
        displayVehicles(results);
    }

    private void processGetAllVehiclesRequest()
    {
        System.out.println("\n----------- All Vehicles --------------------");
        List<Vehicle> results = dealership.getAllVehicles();
        displayVehicles(results);
    }

    private void processAddVehicleRequest()
    {
        System.out.println("\n----------- Add Vehicle ---------------------");
        System.out.println("Enter vehicle details: ");
        int vin = promptInt("VIN: ");
        int year = promptInt("Year: ");
        String make = promptString("Make: ");
        String model = promptString("Model: ");
        String type = promptString("Type: ");
        String color = promptString("Color: ");
        int odometer = promptInt("Odometer: ");
        double price = promptDouble("Price: ");

        Vehicle newVehicle = new Vehicle(vin, year, make, model, type, color, odometer, price);
        dealership.addVehicle(newVehicle);

        DealershipFileManager fileManager = new DealershipFileManager();
        fileManager.saveDealership(dealership, "src/main/resources/inventory.csv");
        System.out.println("✓ Vehicle added.");
    }

    private void processRemoveVehicleRequest()
    {
        System.out.println("\n----------- Remove Vehicle ------------------");
        System.out.print("Enter VIN of vehicle to remove: ");
        int vin = promptInt("Enter VIN of vehicle to remove: ");
        Vehicle toRemove = null;
        for (Vehicle v : dealership.getAllVehicles())
        {
            if (v.getVin() == vin)
            {
                toRemove = v;
                break;
            }
        }
        if (toRemove != null)
        {
            dealership.removeVehicle(toRemove);
            DealershipFileManager fileManager = new DealershipFileManager();
            fileManager.saveDealership(dealership, "src/main/resources/inventory.csv");
            System.out.println("✓ Vehicle removed.");
        }
        else
        {
            System.out.println("! Vehicle not found.");
        }
    }

    private int promptInt(String message) {
        while (true)
        {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
            {
                System.out.println("! Please enter a value.");
                continue;
            }
            try
            {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("! Please enter an integer.");
            }
        }
    }

    private double promptDouble(String message)
    {
        while (true)
        {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
            {
                System.out.println("! Please enter a value.");
                continue;
            }
            try
            {
                return Double.parseDouble(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("! Please enter a valid number (example: 9999.99).");
            }
        }
    }

    private String promptString(String message)
    {
        while (true)
        {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty())
                return input;
            System.out.println("! Please enter a value.");
        }
    }

}
