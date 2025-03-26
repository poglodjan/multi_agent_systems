package org.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ResponseHandlingBehaviour extends CyclicBehaviour {

    public ResponseHandlingBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().receive();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                System.out.println("(approval) Delivery proposal received: " + msg.getContent());

                // offer acceptation
                ACLMessage accept = msg.createReply();
                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                accept.setContent("Accepting offer: " + msg.getContent());
                getAgent().send(accept);
                System.out.println("(approval) Offer accepted from: " + msg.getSender().getLocalName());
            }
        } else {
            block();
        }
    }
}
