package org.example;

public class ProcessingState implements DonationState {

    @Override
    public void nextState(DonationContext context) {
        System.out.println("Transitioning from Processing to Completed.");
        context.setState(new CompletedState());
        context.updateStatusInDatabase("Completed");
    }

    @Override
    public void previousState(DonationContext context) {
        System.out.println("Reverting from Processing to Pending Approval.");
        context.setState(new PendingApprovalState());
        context.updateStatusInDatabase("PendingApproval");
    }

    @Override
    public void printCurrentState() {
        System.out.println("Current State: Processing.");
    }
}
