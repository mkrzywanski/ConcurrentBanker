package pl.lodz.pl.concurrency.ConcurrentBanker;

import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

public class Banker {

	private static Logger logger = Logger.getLogger(Banker.class);

	private Semaphore semaphore = new Semaphore(1);

	private int maxCapital;
	private int currentCapital;
    private int maximumRejectionsAmount;
	private Integer clientIdWhoBankerIsWaitingFor;

	public Banker(int maxCapital, int maximumRejectionsAmount) {
		this.maxCapital = maxCapital;
		this.currentCapital = maxCapital;
		this.maximumRejectionsAmount = maximumRejectionsAmount;
	}

	public void meetClient(Client client) {
		try {
			this.semaphore.acquire();

			logger.info("Bank capital before client came: " + this.currentCapital + " florens.");
			logger.info("Client " + client.getId() + " entered the bank");
			
			if (client.getCurrentLoan() == 0) {
				this.processClientNewLoan(client);
			} else {
				this.processClientMoneyReturn(client);
			}

			logger.info("Bank capital after operation: " + this.currentCapital + " florens.\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.semaphore.release();
		}
	}

	private void processClientNewLoan(Client client) {
		int newLoanValue = Client.getRandomLoan(this.maxCapital);

		if (this.clientIdWhoBankerIsWaitingFor != null && !this.clientIdWhoBankerIsWaitingFor.equals(client.getId())) {
			logger.info("Cannot make the operation because bank is waiting to process client with id "
					+ this.clientIdWhoBankerIsWaitingFor + " because his request has been rejected " + this.maximumRejectionsAmount + " times already");
		} else {
			if(client.getWantedLoan() != null) {
				newLoanValue = client.getWantedLoan();
			}

			logger.info("Client wants to take a loan: " + newLoanValue);

			if (this.currentCapital >= newLoanValue) {
				this.processLoanAcceptation(client, newLoanValue);
			} else {
				this.processLoanRejection(client, newLoanValue);
			}
		}
	}

	private void processClientMoneyReturn(Client client) {
		logger.info("Client with id " + client.getId() + " wants to give back " + client.getCurrentLoan());
		this.currentCapital += client.getCurrentLoan();
		client.setCurrentLoan(0);
	}

	private void processLoanRejection(Client client, int newLoanValue) {
		client.increaseRejectionCounter();

		if (client.getCurrentRejectionsAmount() == this.maximumRejectionsAmount) {
			this.clientIdWhoBankerIsWaitingFor = client.getId();
		}
		
		if(client.getWantedLoan() == null) {
			client.setWantedLoan(newLoanValue);
		}

		logger.info("Rejected");
	}

	private void processLoanAcceptation(Client client, int newLoanValue) {
		this.currentCapital -= newLoanValue;
		client.setCurrentLoan(newLoanValue);

		if (client.getCurrentRejectionsAmount() >= this.maximumRejectionsAmount) {
			this.clientIdWhoBankerIsWaitingFor = null;
		}

		client.setCurrentRejectionsAmount(0);
		client.setWantedLoan(null);
		client.incrementServiceCounter();

		logger.info("Accepted");
	}

	public int getMaxCapital() {
		return maxCapital;
	}

	public void setMaxCapital(int maxCapital) {
		this.maxCapital = maxCapital;
	}

	public int getCurrentCapital() {
		return currentCapital;
	}

	public void setCurrentCapital(int currentCapital) {
		this.currentCapital = currentCapital;
	}

	public Integer getClientIdWhoBankerIsWaitingFor() {
		return clientIdWhoBankerIsWaitingFor;
	}

	public void setClientIdWhoBankerIsWaitingFor(Integer clientIdWhoBankerIsWaitingFor) {
		this.clientIdWhoBankerIsWaitingFor = clientIdWhoBankerIsWaitingFor;
	}

}
