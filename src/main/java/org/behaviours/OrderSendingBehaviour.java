package org.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class OrderSendingBehaviour extends OneShotBehaviour {

    public OrderSendingBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.CFP);
        msg.setContent("milk, coffee, rice");
        msg.addReceiver(getAgent().getAID("DeliveryAgent"));
        getAgent().send(msg);
        System.out.println("(ORDER REQUEST) Order sent by " + getAgent().getLocalName());
    }
}
