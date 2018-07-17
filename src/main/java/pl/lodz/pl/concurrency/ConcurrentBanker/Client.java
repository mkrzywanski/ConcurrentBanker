package pl.lodz.pl.concurrency.ConcurrentBanker;

import java.util.Random;

public class Client implements Runnable {

	private static int maxSleepTimeMs = 500;
	
	private Random random = new Random();
	private int currentLoan;
	private int id;
	private int currentRejectionsAmount;
	private int maxOperationLimit;
	private int currentOperationCounter;
	private Integer wantedLoan;

	private Banker banker;
	
	public Client(int id, int maxOperationLimit, Banker banker) {
		this.id = id;
		this.maxOperationLimit = maxOperationLimit;
		this.banker = banker;
	}
	
	public static int getRandomLoan(int maxLoanValue) {
		return (int) (maxLoanValue * Math.random());
	}

	public void run() {
		while(currentOperationCounter < maxOperationLimit){
			banker.meetClient(this);
			try {
				Thread.sleep(random.nextInt(maxSleepTimeMs));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(currentLoan != 0) {
			banker.meetClient(this);
		}
		
	}

	public void increaseRejectionCounter() {
		this.currentRejectionsAmount++;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getCurrentRejectionsAmount() {
		return currentRejectionsAmount;
	}

	public void setCurrentRejectionsAmount(int currentRejectionsAmount) {
		this.currentRejectionsAmount = currentRejectionsAmount;
	}
	
	public int getCurrentLoan() {
		return currentLoan;
	}

	public void setCurrentLoan(int currentLoan) {
		this.currentLoan = currentLoan;
	}
	
	public Integer getWantedLoan() {
		return wantedLoan;
	}

	public void setWantedLoan(Integer wantedLoan) {
		this.wantedLoan = wantedLoan;
	}
	
	public int getCurrentOperationCounter() {
		return currentOperationCounter;
	}
	public void incrementServiceCounter() {
		currentOperationCounter++;
	}

	
}
