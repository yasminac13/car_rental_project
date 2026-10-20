# Car Rental Management System

A Java-based desktop application designed to streamline the operations of a car rental service. Built with an Object-Oriented architecture, the system provides a robust graphical user interface (GUI) and secure role-based access for both customers and administrators.

## Features

* **Role-Based Authentication:** Dedicated dashboards for administrators (`AdminDashboard`) and clients (`ClientDashboard`) accessed through a unified login system.
* **Vehicle Inventory Management:** Dynamic tracking of available fleet, utilizing polymorphism to handle specific vehicle categories like Sedans and SUVs.
* **Booking Engine:** End-to-end reservation tracking with custom validation to prevent double-booking (e.g., throwing a `CarNotAvailableException` if a reserved vehicle is selected).
* **Data Persistence:** Utilizes the Repository Design Pattern to interface securely with an SQL database, alongside local file caching for states and backups.

## Technologies Used

* **Language:** Java
* **UI Framework:** Java Swing
* **Architecture:** Repository Design Pattern, Object-Oriented Programming (OOP)
* **Database:** SQL via JDBC (`RentalDatabaseSql`, `ConnectionFactory`)
* **Testing:** JUnit Jupiter
* **IDE:** IntelliJ IDEA

## Project Structure

* `src/gui/`: Contains all visual components, frames, and panels (e.g., `LoginFrame`, `BackgroundPanel`).
* `src/models/`: Core entities representing domain logic (`User`, `Admin`, `Client`, `Vehicle`, `SUV`, `Sedan`, `Booking`).
* `src/database/`: Database connectivity and repository classes (`AdminRepository`, `ClientRepository`, `VehicleRepository`).
* `src/interfaces/`: Contracts defining system behaviors (`Bookable`).
* `src/exceptions/`: Custom error handling classes.
* `src/test/`: JUnit test cases ensuring system reliability (`BookingTest`, `VehicleTest`).

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher.
* An SQL database configured and running.
* IntelliJ IDEA (recommended, as `.idea` configuration files are included).

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/yasminac13/car_rental_project.git](https://github.com/yasminac13/car_rental_project.git)
