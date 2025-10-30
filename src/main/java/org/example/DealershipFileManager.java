package org.example;

import java.io.*;

/**
 * The DealershipFileManager class provides functionality to read and write dealership data to and from files.
 * It enables creating a Dealership object by reading data from a file and persists dealership data to a file.
 */
public class DealershipFileManager
{
    public Dealership getDealership(String filename)
    {
        Dealership dealership = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            String line = reader.readLine();
            if (line != null)
            {
                String[] parts = line.split("\\|");
                if (parts.length == 3)
                {
                    dealership = new Dealership(parts[0], parts[1], parts[2]);
                }
            }

            while ((line = reader.readLine()) != null)
            {
                String[] vehicleData = line.split("\\|");
                if (vehicleData.length == 8 && dealership != null)
                {
                    int vin = Integer.parseInt(vehicleData[0]);
                    int year = Integer.parseInt(vehicleData[1]);
                    String make = vehicleData[2];
                    String model = vehicleData[3];
                    String type = vehicleData[4];
                    String color = vehicleData[5];
                    int odometer = Integer.parseInt(vehicleData[6]);
                    double price = Double.parseDouble(vehicleData[7]);
                    Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, odometer, price);
                    dealership.addVehicle(vehicle);
                }
            }
        } catch (IOException e)
        {
            System.out.println("Could not load dealership: " + e.getMessage());
        }
        return dealership;
    }

 public void saveDealership(Dealership dealership, String filename)
 {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename)))
        {
            // Dealership info as first line
            writer.printf("%s|%s|%s%n", dealership.getName(), dealership.getAddress(), dealership.getPhone());
            // Each vehicle per line
            for (Vehicle v : dealership.getAllVehicles())
            {
                writer.printf("%d|%d|%s|%s|%s|%s|%d|%.2f%n",
                        v.getVin(),
                        v.getYear(),
                        v.getMake(),
                        v.getModel(),
                        v.getVehicleType(),
                        v.getColor(),
                        v.getOdometer(),
                        v.getPrice());
            }
        } catch (IOException e) {
            System.out.println("Could not save dealership: " + e.getMessage());
        }
    }
}

