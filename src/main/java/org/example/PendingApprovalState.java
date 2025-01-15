package org.example;

public class PendingApprovalState implements DonationState {

    @Override
    public void nextState(DonationContext context) {
        System.out.println("Transitioning from Pending Approval to Processing.");
        context.setState(new ProcessingState());
        context.updateStatusInDatabase("Processing");
    }

    @Override
    public void previousState(DonationContext context) {
        System.out.println("Pending Approval is the initial state. Cannot revert further.");
    }

    @Override
    public void printCurrentState() {
        System.out.println("Current State: Pending Approval.");
    }
}