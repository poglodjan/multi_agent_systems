package org.Agents;

import jade.core.Agent;
import org.behaviours.ProductOfferingBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class MarketAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("MarketAgent " + getLocalName() + " started.");

        // Register in DF
        registerMarketService("grocery-supply", "Market-Grocery");

        addBehaviour(new ProductOfferingBehaviour(this)); // collecting offer about products
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("MarketAgent " + getLocalName() + " successfully deregistered.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("MarketAgent " + getLocalName() + " terminated.");
    }

    // Register in DF
    private void registerMarketService(String serviceType, String serviceName) {
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());

            ServiceDescription sd = new ServiceDescription();
            sd.setType(serviceType);
            sd.setName(serviceName);
            dfd.addServices(sd);

            DFService.register(this, dfd);
            System.out.println("Service registered: " + serviceType + " - " + serviceName);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
