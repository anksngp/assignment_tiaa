package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

public class CreateProduct {

	public static void main(String[] args) throws InterruptedException {

		// Total bolts and machine available in warehouse
		int totalBolts = 12;
		int totalMachines = 6;
		int numberOfWorkers = 3;
		int timeRequiredForCreatingProduct = 1;

		for (int i = 0; i < 1; i++) {
			createProducts(totalBolts, totalMachines, numberOfWorkers, timeRequiredForCreatingProduct);
			
		}
	}

	private static void createProducts(int totalBolts, int totalMachines, int numberOfWorkers,
			int timeRequiredForCreatingProduct) {
		// Check if items are adequate quantity
		if (totalBolts % totalMachines != 0) {
			System.out.println("Invalid Input");
			return;
		}

		// Initialise list of material
		BlockingQueue<Material> blockingQueue = new LinkedBlockingQueue<>();
		for (int i = 0; i < totalMachines; i++) {
			try {
				blockingQueue.put(new Bolt());
				blockingQueue.put(new Machine());
				blockingQueue.put(new Bolt());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Total products that would be made
		System.out.println("Total products to be made " + blockingQueue.size() / 3);

		// Creating worker threads
		Worker conveyorBelt = new Worker(blockingQueue, timeRequiredForCreatingProduct);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkers);

		List<Future<Output>> futures = new ArrayList<>();
		// Submit task to Executor
		for (int i = 1; i <= numberOfWorkers; i++) {
			Future<Output> submit = executor.submit(conveyorBelt);
			futures.add(submit);
		}

		int totalProductsCreated = 0;
		int maxTimeTaken = 0;
		for (Future<Output> f : futures) {
			try {
				Output output = f.get();
//				System.out.println(output.getProductsCreated());
				totalProductsCreated = totalProductsCreated + (output.getProductsCreated());
				int timetaken = output.getTimetaken();
				if (timetaken > maxTimeTaken) {
					maxTimeTaken = timetaken;

				}
			} catch (InterruptedException | ExecutionException ex) {
			}

		}

		Assert.assertEquals(6, totalProductsCreated);
		Assert.assertEquals(2, maxTimeTaken);
		System.out.println("Total products created " + totalProductsCreated);
//		System.out.println("Time taken "+ maxTimeTaken);
		// Shutdown Executor

		executor.shutdown();
		try {
			if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException ex) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}

		System.out.println("Stopped");
	}

}
