package org.Agents;

import jade.core.Agent;
import org.behaviours.OrderSendingBehaviour;
import org.behaviours.ResponseHandlingBehaviour;

public class ClientAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("ClientAgent " + getLocalName() + " started.");

        addBehaviour(new OrderSendingBehaviour(this));
        addBehaviour(new ResponseHandlingBehaviour(this));
    }

    @Override
    protected void takeDown() {
        System.out.println("ClientAgent " + getLocalName() + " terminated.");
    }
}
