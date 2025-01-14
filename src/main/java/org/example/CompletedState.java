package org.example;

public class CompletedState implements DonationState {

    @Override
    public void nextState(DonationContext context) {
        System.out.println("Donation is already completed. No further transitions.");
    }

    @Override
    public void previousState(DonationContext context) {
        System.out.println("Reverting from Completed to Processing.");
        context.setState(new ProcessingState());
        context.updateStatusInDatabase("Processing");
    }

    @Override
    public void printCurrentState() {
        System.out.println("Current State: Completed.");
    }
}
