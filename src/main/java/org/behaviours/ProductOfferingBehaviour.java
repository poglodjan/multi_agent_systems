package org.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;

public class ProductOfferingBehaviour extends CyclicBehaviour {

    private final Map<String, Map<String, Integer>> marketProducts = new HashMap<>();

    public ProductOfferingBehaviour(Agent agent) {
        super(agent);
        initializeMarketProducts();
    }

    private void initializeMarketProducts() {
        String agentName = myAgent.getLocalName();
        Map<String, Integer> products = new HashMap<>();

        switch (agentName) {
            case "MarketAgent1":
                products.put("milk", 5);
                products.put("coffee", 30);
                break;
            case "MarketAgent2":
                products.put("coffee", 25);
                products.put("rice", 3);
                break;
            case "MarketAgent3":
                products.put("rice", 4);
                break;
        }

        marketProducts.put(agentName, products);
        System.out.println("(market) " + agentName + " offers: " + products);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            System.out.println("(approval) " + myAgent.getLocalName() + " received request: " + msg.getContent());

            Map<String, Integer> products = marketProducts.get(myAgent.getLocalName());
            StringBuilder prices = new StringBuilder("Prices: ");
            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                prices.append(entry.getKey()).append(" (").append(entry.getValue()).append(" zl), ");
            }
            String responseContent = prices.toString().replaceAll(", $", "");

            // Creating response
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent(responseContent);
            myAgent.send(reply);

            System.out.println("(approval) " + myAgent.getLocalName() + " sent response: " + responseContent);
        } else {
            block();
        }
    }
}
