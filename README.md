# MUS - Multi-Agent Shop Management System

This project is a **Multi-Agent Shop Management System (MUS)** built using the **JADE framework**. The system is designed to manage a grocery shop environment, where multiple agents (market, client, and delivery) interact with each other to fulfill customer orders efficiently.

## Features

1. **Agent-based architecture:** Utilizes JADE agents to model real-world entities like markets, delivery services, and clients.
2. **Optimal order fulfillment:** Uses dynamic price calculations and market offers to find the most cost-effective way to fulfill the order.
3. **Market discovery and communication:** Dynamically searches for available market agents and retrieves product offers.
4. **Delivery cost estimation:** Calculates the optimal delivery cost by selecting the cheapest combination of products.
5. **Order approval and fulfillment:** Clients receive the calculated offer and select the optimal delivery service.

---

## Project Structure:

           └── Agents/            # Definitions of Client, Delivery, and Market Agents
           └── behaviours/        # Behaviours for agents (communication, price calculation, etc.)

## Sample output:
 
           MarketAgent MarketAgent1 started.
           MarketAgent MarketAgent2 started.
           MarketAgent MarketAgent3 started.
           DeliveryAgent DeliveryAgent started.
           All agents successfully initialized and running.
           ClientAgent ClientAgent started.
           (ORDER REQUEST) Order sent by ClientAgent
           Service registered: grocery-supply - Market-Grocery
           (market) MarketAgent1 offers: {coffee=30, milk=5}
           Service registered: grocery-supply - Market-Grocery
           (market) MarketAgent2 offers: {coffee=25, rice=3}
           DeliveryAgent DeliveryAgent registered in DF with services.
           (approval) Received offer request from ClientAgent: milk, coffee, rice
           (delivery) Order items set: [milk, coffee, rice]
           Service registered: grocery-supply - Market-Grocery
           (market) MarketAgent3 offers: {rice=4}
           (market) Found MarketAgents: 3
           - MarketAgent1
           (message) Request sent to MarketAgent1
           - MarketAgent2
           (message) Request sent to MarketAgent2
           - MarketAgent3
           (message) Request sent to MarketAgent3
           (calculations) Calculating optimal delivery price...
           (approval) MarketAgent2 received request: Get prices and availability
           (approval) MarketAgent3 received request: Get prices and availability
           (approval) MarketAgent1 received request: Get prices and availability
           (approval) MarketAgent3 sent response: Prices: rice (4 zl)
           (approval) MarketAgent2 sent response: Prices: coffee (25 zl), rice (3 zl)
           (approval) MarketAgent1 sent response: Prices: coffee (30 zl), milk (5 zl)
           (Market research) MarketAgents found: 3
           (approval) Received response from MarketAgent3: Prices: rice (4 zl)
           (approval) Received response from MarketAgent1: Prices: coffee (30 zl), milk (5 zl)
           (approval) Received response from MarketAgent2: Prices: coffee (25 zl), rice (3 zl)
           (approval) All responses received. Total responses: 3
           Market offers received: {MarketAgent1={coffee=30, milk=5}, MarketAgent2={coffee=25, rice=3}, MarketAgent3={rice=4}}
           (calculations finished) Total price calculated: 33 zl
           (message) Sent offer to ClientAgent with estimated price: 33 zl
           (approval) Delivery proposal received: Estimated price: 33 zl
           (approval) Offer accepted from: DeliveryAgent
           (approval) Order accepted by ClientAgent
