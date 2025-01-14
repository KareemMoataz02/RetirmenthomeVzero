package org.example;

import org.bson.Document;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Step 1: Initialize MongoDB Users
        System.out.println("Initializing Users...");

        // Clean existing users to avoid duplicates (Optional)
        User.deleteUser(1);
        User.deleteUser(2);

        // Create new users
        User.createUser(1, "Dr. Alice");
        User.createUser(2, "Bob");


        // Step 2: Initialize Donation Behaviors
        DonationBehavior medicineDonationBehavior = new MedicineDonation();
        DonationBehavior moneyDonationBehavior = new MoneyDonation();

        // Step 3: Initialize Receipt Generators
        IReceiptGenerator medicineReceiptGenerator = new MedicineDonationReceiptAdapter();
        IReceiptGenerator moneyReceiptGenerator = new MoneyDonationReceiptAdapter();

        // Step 4: Create Donations
        System.out.println("Creating Donations...");
        // For MedicineDonation, medicineType is set internally (e.g., "Pain Killers")
        Donation medDonation1 = medicineDonationBehavior.createDonation("2025-01-15", 50.0, 101, 1001);
        // For MoneyDonation, medicineType is implicitly null
        Donation moneyDonation1 = moneyDonationBehavior.createDonation("2025-01-16", 200.0, 102, 1002);

        // Step 5: Generate Receipts
        System.out.println("\nGenerating Receipts...");
        String medReceipt1 = medicineReceiptGenerator.generateReceipt(medDonation1);
        String moneyReceipt1 = moneyReceiptGenerator.generateReceipt(moneyDonation1);

        System.out.println(medReceipt1);
        System.out.println(moneyReceipt1);

        // Step 6: Update Donations
        System.out.println("\nUpdating Donations...");
        // Updating Medicine Donation (medicineType remains unchanged)
        Donation updatedMedDonation = medicineDonationBehavior.updateDonation(
                medDonation1.getDonationId(),
                "2025-01-20",
                75.0,
                101
        );
        // Updating Money Donation
        Donation updatedMoneyDonation = moneyDonationBehavior.updateDonation(
                moneyDonation1.getDonationId(),
                "2025-01-21",
                250.0,
                102
        );

        // Step 7: Generate Updated Receipts
        System.out.println("\nGenerating Updated Receipts...");
        String updatedMedReceipt = medicineReceiptGenerator.generateReceipt(updatedMedDonation);
        String updatedMoneyReceipt = moneyReceiptGenerator.generateReceipt(updatedMoneyDonation);

        System.out.println(updatedMedReceipt);
        System.out.println(updatedMoneyReceipt);

        // Step 8: Cancel Donations
        System.out.println("\nCancelling Donations...");
        boolean medCancelStatus = medicineDonationBehavior.cancelDonation(medDonation1.getDonationId());
        boolean moneyCancelStatus = moneyDonationBehavior.cancelDonation(moneyDonation1.getDonationId());

        System.out.println("Medicine Donation Cancelled: " + medCancelStatus);
        System.out.println("Money Donation Cancelled: " + moneyCancelStatus);

        // Step 9: Generate Receipts for Cancelled Donations
        System.out.println("\nGenerating Receipts for Cancelled Donations...");
        // Assuming that generating a receipt for a cancelled donation still shows the original details
        String cancelledMedReceipt = medicineReceiptGenerator.generateReceipt(medDonation1);
        String cancelledMoneyReceipt = moneyReceiptGenerator.generateReceipt(moneyDonation1);

        System.out.println(cancelledMedReceipt);
        System.out.println(cancelledMoneyReceipt);

        // Step 10: Integrate with Observer Pattern
        System.out.println("\nIntegrating with Observer Pattern...");

        // Create EventManager (Subject)
        EventManager eventManager = new EventManager();

        // Step 11: Create and Register Observers
        System.out.println("\nCreating and Registering Observers...");

        // Create a Doctor and append to database
        Doctor doctor = new Doctor(1, "Dr. Alice");
        boolean doctorAdded = doctor.appendDoctorToDatabase();
        if (doctorAdded) {
            System.out.println("Doctor " + doctor.getName() + " added to database.");
        } else {
            System.err.println("Failed to add Doctor to database.");
        }

        // Create a Volunteer and append to database
        Volunteer volunteer = new Volunteer(new User(2)); // User with id=2, name="Bob"
        boolean volunteerAdded = volunteer.createVolunteer("Bob", 30);
        if (volunteerAdded) {
            System.out.println("Volunteer " + volunteer.volunteer.getName() + " added to database.");
        } else {
            System.err.println("Failed to add Volunteer to database.");
        }

        // Register Observers
        eventManager.registerObserver(doctor);
        eventManager.registerObserver(volunteer);

        // Step 12: Create Events and Notify Observers
        System.out.println("\nCreating Events...");
        eventManager.createEvent(101, "Health Checkup", "2025-01-15", "Free health checkup for seniors.");
        eventManager.createEvent(102, "Blood Donation Camp", "2025-02-10", "Community blood donation drive.");

        // Step 13: Retrieve and Display All Events
        System.out.println("\nAll Events in the System:");
        List<Event> allEvents = eventManager.getAllEvents();
        for (Event event : allEvents) {
            System.out.println(event);
        }

        // Step 14: Retrieve a Specific Event by ID
        System.out.println("\nRetrieving Event with ID 101:");
        Event event101 = eventManager.getEventById(101);
        if (event101 != null) {
            System.out.println(event101);
        } else {
            System.out.println("Event not found.");
        }

        // Step 15: Update an Event
        System.out.println("\nUpdating Event with ID 101...");
        boolean updateStatus = eventManager.updateEvent(
                101,
                "Health Checkup",
                "2025-01-20",
                "Updated details for the health checkup."
        );
        if (updateStatus) {
            System.out.println("Event updated successfully.");
        } else {
            System.out.println("Failed to update event.");
        }

        // Step 16: Delete an Event
        System.out.println("\nDeleting Event with ID 102...");
        boolean deleteStatus = eventManager.deleteEvent(102);
        if (deleteStatus) {
            System.out.println("Event deleted successfully.");
        } else {
            System.out.println("Failed to delete event.");
        }

        // Step 17: Retrieve All Events After Deletion
        System.out.println("\nAll Events After Deletion:");
        allEvents = eventManager.getAllEvents();
        for (Event event : allEvents) {
            System.out.println(event);
        }

        // Step 18: Remove an Observer and Create Another Event
        System.out.println("\nRemoving volunteer observer...");
        eventManager.removeObserver(volunteer);

        System.out.println("Creating a new event to test observer notifications...");
        eventManager.createEvent(103, "Yoga Workshop", "2025-03-05", "Yoga workshop for community wellness.");

        // Step 19: Generate Receipt for a New Donation
        System.out.println("\nCreating a New Medicine Donation...");
        Donation medDonation2 = medicineDonationBehavior.createDonation("2025-03-10", 100.0, 103, 1003);
        String medReceipt2 = medicineReceiptGenerator.generateReceipt(medDonation2);
        System.out.println(medReceipt2);

        // Step 20: Make a Reservation as Volunteer
        System.out.println("\nVolunteer Making a Reservation for Event ID 101...");
        boolean reservationStatus = volunteer.makeReservation(101);
        if (reservationStatus) {
            System.out.println("Reservation made successfully.");
        } else {
            System.out.println("Failed to make reservation.");
        }

        // Step 21: Retrieve and Display Reservations
        System.out.println("\nRetrieving Volunteer Reservations...");
        List<Document> reservations = volunteer.getReservations();
        for (Document reservation : reservations) {
            System.out.println(reservation.toJson());
        }

        // End of Test
        System.out.println("\nTest completed.");
    }
}
