package org.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import org.Agents.DeliveryAgent;

import java.util.*;
import java.util.stream.Collectors;

public class PriceCalculationBehaviour extends OneShotBehaviour {

    private final Map<String, Map<String, Integer>> marketOffers = new HashMap<>();
    private final List<String> orderItems = Arrays.asList("milk", "coffee", "rice");

    public PriceCalculationBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        System.out.println("(calculations) Calculating optimal delivery price...");

        int responseCount = 0;
        int marketAgentsCount = countMarketAgents();
        System.out.println("(Market research) MarketAgents found: " + marketAgentsCount);

        // Getting all of the messages from market
        while (responseCount < marketAgentsCount) {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage response = myAgent.blockingReceive(mt, 3000);  // wait 3 seconds max

            if (response != null) {
                System.out.println("(approval) Received response from " + response.getSender().getLocalName() + ": " + response.getContent());

                Map<String, Integer> prices = parsePrices(response.getContent());
                marketOffers.put(response.getSender().getLocalName(), prices);
                responseCount++;
            } else {
                System.out.println("(error)️ No response received within timeout period (3s). Breaking the loop.");
                break;
            }
        }

        System.out.println("(approval) All responses received. Total responses: " + responseCount);
        System.out.println("Market offers received: " + marketOffers);

        int totalPrice = calculateTotalPrice(marketOffers, orderItems);
        System.out.println("(calculations finished) Total price calculated: " + totalPrice + " zl");

        // Setting calculated cost in agent
        ((DeliveryAgent) myAgent).setCalculatedCost(totalPrice);

        ACLMessage result = new ACLMessage(ACLMessage.PROPOSE);
        result.setContent("Estimated price: " + totalPrice + " zl");
        result.addReceiver(getAgent().getAID("ClientAgent"));
        myAgent.send(result);
        System.out.println("(message) Sent offer to ClientAgent with estimated price: " + totalPrice + " zl");
    }

    private int countMarketAgents() {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("grocery-supply");
            template.addServices(sd);

            DFAgentDescription[] results = DFService.search(myAgent, template);
            return results.length;
        } catch (FIPAException fe) {
            fe.printStackTrace();
            return 0;
        }
    }

    private int calculateTotalPrice(Map<String, Map<String, Integer>> offers, List<String> orderItems) {
        int total = 0;
        Map<String, Integer> selectedItems = new HashMap<>();
        List<String> remainingItems = new ArrayList<>(orderItems);

        // sorting offers based on fitted products and costs
        List<Map.Entry<String, Map<String, Integer>>> sortedOffers = offers.entrySet().stream()
                .sorted((o1, o2) -> {
                    int count1 = countMatchingItems(o1.getValue(), remainingItems);
                    int count2 = countMatchingItems(o2.getValue(), remainingItems);

                    if (count1 != count2) {
                        return Integer.compare(count2, count1); // number of products
                    } else {
                        return Integer.compare(sumPrices(o1.getValue()), sumPrices(o2.getValue())); // Then price
                    }
                })
                .collect(Collectors.toList());

        for (Map.Entry<String, Map<String, Integer>> offer : sortedOffers) {
            Map<String, Integer> prices = offer.getValue();

            // adding products for the given offer
            for (String item : remainingItems) {
                if (prices.containsKey(item)) {
                    selectedItems.put(item, prices.get(item));
                }
            }

            // Usunięcie pokrytych produktów
            remainingItems.removeIf(selectedItems::containsKey);

            // If all products completed then break
            if (remainingItems.isEmpty()) {
                break;
            }
        }

        // Final cost computation
        for (int price : selectedItems.values()) {
            total += price;
        }

        if (!remainingItems.isEmpty()) {
            System.out.println("(error) Not all items covered: " + remainingItems);
        }

        return total;
    }

    // count how many marketagents are there
    private int countMatchingItems(Map<String, Integer> prices, List<String> items) {
        int count = 0;
        for (String item : items) {
            if (prices.containsKey(item)) {
                count++;
            }
        }
        return count;
    }

    private int sumPrices(Map<String, Integer> prices) {
        return prices.values().stream().mapToInt(Integer::intValue).sum();
    }

    private Map<String, Integer> parsePrices(String content) {
        Map<String, Integer> prices = new HashMap<>();
        String[] items = content.replace("Prices: ", "").split(", ");
        for (String item : items) {
            try {
                String[] parts = item.split(" \\(");
                int price = Integer.parseInt(parts[1].replace(" zl)", ""));
                prices.put(parts[0], price);
            } catch (Exception e) {
                System.out.println("(error) Error parsing price: " + item);
            }
        }
        return prices;
    }
}
