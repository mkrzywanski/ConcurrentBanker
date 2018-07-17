package pl.lodz.pl.concurrency.ConcurrentBanker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class App {
	
	private static Logger logger = Logger.getLogger(App.class);
	
	private static int clientsAmount = 5;
	private static int bankerMaxCapital = 1000;
	private static int clientOperationLimit = 5;
    private static int maximumRejectionsAmount = 5;
	
    public static void main( String[] args ) throws InterruptedException {
    	logger.info("Starting program");
    	logger.info("Clients: " + clientsAmount + " Banker's max capital: " + bankerMaxCapital + " Client's loan operation limit: " + clientOperationLimit);
    	
        Banker banker = new Banker(bankerMaxCapital, maximumRejectionsAmount);

        List<Client> clients = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < clientsAmount; i++) {
            Client client = new Client(i, clientOperationLimit, banker);
        	clients.add(client);

        	Thread clientThread = new Thread(client);
        	threads.add(clientThread);
        	clientThread.start();
        }
        
        for(Thread thread: threads) {
        	thread.join();
        }
        
        for(Client client: clients) {
        	logger.info("Client with id " + client.getId() + " currentLoan: " + client.getCurrentLoan());
        }

        logger.info("Banker's capital: " + banker.getCurrentCapital());
        
    }
}
