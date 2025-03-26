package org.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import org.Agents.DeliveryAgent;

public class OfferResponseBehaviour extends CyclicBehaviour {

    public OfferResponseBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.CFP) {
                System.out.println("(approval) Received offer request from ClientAgent: " + msg.getContent());

                // setting order for the client offer
                String[] items = msg.getContent().split(", ");
                ((DeliveryAgent) myAgent).setOrderItems(items);

                // Start price calculation
                myAgent.addBehaviour(new PriceCalculationBehaviour(myAgent));

                // get calculated cost
                int calculatedCost = ((DeliveryAgent) myAgent).getCalculatedCost();

                ACLMessage reply = msg.createReply();
                if (calculatedCost > 0) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent("Estimated price: " + calculatedCost + " zl (including delivery fee)");
                    System.out.println("(message) Sent offer to ClientAgent with estimated price: " + calculatedCost + " zl");
                }
                myAgent.send(reply);
            } else if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                System.out.println("(approval) Order accepted by " + msg.getSender().getLocalName());
            }
        } else {
            block();
        }
    }
}
