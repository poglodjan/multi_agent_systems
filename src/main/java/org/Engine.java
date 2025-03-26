package org;

import static org.JADEEngine.runAgent;
import static org.JADEEngine.runGUI;
import org.exceptions.JadePlatformInitializationException;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Engine {

    private static final ExecutorService jadeExecutor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        final Runtime runtime = Runtime.instance();
        final Profile profile = new ProfileImpl();

        try {
            final ContainerController container = jadeExecutor.submit(() -> runtime.createMainContainer(profile)).get();
            runGUI(container);

            // run agents
            runAgent(container, "MarketAgent1", "MarketAgent");
            runAgent(container, "MarketAgent2", "MarketAgent");
            runAgent(container, "MarketAgent3", "MarketAgent");
            runAgent(container, "DeliveryAgent", "DeliveryAgent");
            runAgent(container, "ClientAgent", "ClientAgent");

            System.out.println("All agents successfully initialized and running.");
        } catch (final InterruptedException | ExecutionException e) {
            throw new JadePlatformInitializationException(e);
        }
    }
}