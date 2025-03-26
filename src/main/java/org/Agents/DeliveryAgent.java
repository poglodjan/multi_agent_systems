package org.Agents;

import jade.core.Agent;
import org.behaviours.MarketCommunicationBehaviour;
import org.behaviours.OfferResponseBehaviour;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.List;
import java.util.ArrayList;

public class DeliveryAgent extends Agent {

    private int calculatedCost = -1; // storage for cost calculated value
    private int deliveryFee = 10;
    private List<String> orderItems = new ArrayList<>(); // storage for order items

    @Override
    protected void setup() {
        System.out.println("DeliveryAgent " + getLocalName() + " started.");

        // Register in DF
        registerDeliveryService();

        // Behaviours
        addBehaviour(new OfferResponseBehaviour(this));
        addBehaviour(new MarketCommunicationBehaviour(this)); // communication with market agent
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("Agent " + getLocalName() + " deregistered.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Agent " + getLocalName() + " terminated.");
    }

    public void setOrderItems(String[] items) {
        orderItems.clear();
        for (String item : items) {
            orderItems.add(item.trim());
        }
        System.out.println("(delivery) Order items set: " + orderItems);
    }

    public List<String> getOrderItems() {
        return orderItems;
    }

    // setter and getter
    public void setCalculatedCost(int cost) {
        this.calculatedCost = cost + deliveryFee;
    }
    public int getCalculatedCost() {
        return calculatedCost;
    }

    private void registerDeliveryService() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("delivery");
        sd.setName("Grocery-Delivery");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println("DeliveryAgent " + getLocalName() + " registered in DF with services.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
