package org.example;

public class RejectedState implements DonationState {

    @Override
    public void nextState(DonationContext context) {
        System.out.println("Donation has been rejected. No further transitions.");
        // No further state transitions from RejectedState
    }

    @Override
    public void previousState(DonationContext context) {
        System.out.println("Reverting from Rejected to Pending Approval.");
        context.setState(new PendingApprovalState());
        context.updateStatusInDatabase("PendingApproval");
    }

    @Override
    public void printCurrentState() {
        System.out.println("Current State: Rejected.");
    }
}