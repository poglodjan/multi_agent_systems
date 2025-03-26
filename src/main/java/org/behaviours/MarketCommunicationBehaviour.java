package org.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class MarketCommunicationBehaviour extends OneShotBehaviour {

    public MarketCommunicationBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        try {
            // Search for agents in DF
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("grocery-supply");
            template.addServices(sd);

            DFAgentDescription[] results = DFService.search(myAgent, template);
            System.out.println("(market) Found MarketAgents: " + results.length);

            if (results.length > 0) {
                for (DFAgentDescription result : results) {
                    System.out.println("- " + result.getName().getLocalName());

                    // Creating request for prices
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.setContent("Get prices and availability");
                    msg.addReceiver(result.getName());
                    myAgent.send(msg);
                    System.out.println("(message) Request sent to " + result.getName().getLocalName());
                }
            } else {
                System.out.println("(error) No MarketAgents found.");
            }

        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
