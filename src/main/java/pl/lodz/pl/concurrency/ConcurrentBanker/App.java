package pl.lodz.pl.concurrency.ConcurrentBanker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class App {
	
	private static Logger logger = Logger.getLogger(App.class);
	
	private static final int CLIENTS_AMOUNT = 5;
	private static final int BANKER_MAX_CAPITAL = 1000;
	private static final int CLIENT_OPERATION_LIMIT = 5;
    private static final int MAXIMUM_REJECTIONS_AMOUNT = 5;
	
    public static void main( String[] args ) throws InterruptedException {
    	logger.info("Starting program");
    	logger.info("Clients: " + CLIENTS_AMOUNT + " Banker's max capital: " + BANKER_MAX_CAPITAL + " Client's loan operation limit: " + CLIENT_OPERATION_LIMIT);
    	
        Banker banker = new Banker(BANKER_MAX_CAPITAL, MAXIMUM_REJECTIONS_AMOUNT);

        List<Client> clients = new ArrayList<>(CLIENTS_AMOUNT);
        List<Thread> threads = new ArrayList<>(CLIENTS_AMOUNT);

        for(int i = 0; i < CLIENTS_AMOUNT; i++) {
            Client client = new Client(i, CLIENT_OPERATION_LIMIT, banker);
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
