package org.example;


public class Main {
    public static void main(String[] args) {
        // Create a Doctor object with ID 1 and name
        Doctor doctor = new Doctor(1, "Dr. John Doe");

        // Create a new medical visit for the doctor
        String medicalVisitDate = "2024-11-16";
        String medicalVisitTime = "10:00 AM";
        int elderId = 123; // Example Elder ID
        String medicalVisitStatus = "Scheduled";

        MedicalVisit newMedicalVisit = doctor.makeMedicalVisit(medicalVisitDate, medicalVisitTime, elderId, medicalVisitStatus);
        System.out.println("New medical visit created with ID: " + newMedicalVisit.getVisitId());

        // Retrieve and update the medical visit
        String visitId = newMedicalVisit.getVisitId();
        MedicalVisit retrievedVisit = doctor.getMedicalVisit(visitId);
        System.out.println("Retrieved Medical Visit: " + retrievedVisit);

        // Update the medical visit status
        String updatedStatus = "Completed";
        MedicalVisit updatedVisit = doctor.updateMedicalVisit(visitId, medicalVisitDate, medicalVisitTime, elderId, updatedStatus);
        System.out.println("Updated Medical Visit: " + updatedVisit);

        // Create a Visitor object with ID 101
        Visitor visitor = new Visitor(101);

        // Create a normal visit for the visitor
        String normalVisitDate = "2024-11-17";
        String normalVisitTime = "2:00 PM";
        int elderVisitId1 = 124; // First Elder ID for normal visit
        String visitStatus = "Pending";

        // Create the first normal visit for Elder 124
        NormalVisit newNormalVisit1 = visitor.createNormalVisit(normalVisitDate, normalVisitTime, elderVisitId1, visitStatus);
        System.out.println("New normal visit created with ID: " + newNormalVisit1.getVisitId());

        // Retrieve and update the first normal visit
        String normalVisitId1 = newNormalVisit1.getVisitId();
        NormalVisit retrievedNormalVisit1 = visitor.getNormalVisit(normalVisitId1);
        System.out.println("Retrieved Normal Visit 1: " + retrievedNormalVisit1);

        // Update the first normal visit status
        String updatedNormalVisitStatus1 = "Completed";
        NormalVisit updatedNormalVisit1 = visitor.updateNormalVisit(normalVisitId1, normalVisitDate, normalVisitTime, elderVisitId1, updatedNormalVisitStatus1);
        System.out.println("Updated Normal Visit 1: " + updatedNormalVisit1);

        // Create a second normal visit for another elder (Elder 125)
        int elderVisitId2 = 125; // Second Elder ID for normal visit

        // Create the second normal visit for Elder 125
        NormalVisit newNormalVisit2 = visitor.createNormalVisit(normalVisitDate, normalVisitTime, elderVisitId2, visitStatus);
        System.out.println("New normal visit created with ID: " + newNormalVisit2.getVisitId());

        // Retrieve and update the second normal visit
        String normalVisitId2 = newNormalVisit2.getVisitId();
        NormalVisit retrievedNormalVisit2 = visitor.getNormalVisit(normalVisitId2);
        System.out.println("Retrieved Normal Visit 2: " + retrievedNormalVisit2);

        // Update the second normal visit status
        String updatedNormalVisitStatus2 = "Completed";
        NormalVisit updatedNormalVisit2 = visitor.updateNormalVisit(normalVisitId2, normalVisitDate, normalVisitTime, elderVisitId2, updatedNormalVisitStatus2);
        System.out.println("Updated Normal Visit 2: " + updatedNormalVisit2);

        // Close the connections
        doctor.close();
        visitor.close();
    }
}
