package com.tiaa.assignment.product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.tiaa.assignment.model.Bolt;
import com.tiaa.assignment.model.Machine;
import com.tiaa.assignment.model.Material;
import com.tiaa.assignment.model.Output;
import com.tiaa.assignment.worker.Worker;

public class Product {

	public int createProducts(int totalBolts, int totalMachines, int numberOfWorkers,
			int timeRequiredForCreatingProduct) throws InterruptedException, ExecutionException {
		int totalProductsCreated = 0;
		int maxTimeTaken = 0;
		// Check if items are adequate quantity
		if (totalBolts == 0 || totalMachines == 0 || totalBolts % totalMachines != 0) {
			return 0;
		}

		// Initialise list of material
		BlockingQueue<Material> blockingQueue = new LinkedBlockingQueue<>();
		for (int i = 0; i < totalMachines; i++) {
			blockingQueue.put(new Bolt());
			blockingQueue.put(new Machine());
			blockingQueue.put(new Bolt());
		}

		// Creating worker threads
		Worker conveyorBelt = new Worker(blockingQueue, timeRequiredForCreatingProduct);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkers);

		List<Future<Output>> futures = new ArrayList<>();
		// Submit task to Executor
		for (int i = 1; i <= numberOfWorkers; i++) {
			Future<Output> submit = executor.submit(conveyorBelt);
			futures.add(submit);
		}

		// Getting result
		for (Future<Output> f : futures) {
			Output output = f.get();
			totalProductsCreated = totalProductsCreated + (output.getProductsCreated());
			int timetaken = output.getTimetaken();
			if (timetaken > maxTimeTaken) {
				maxTimeTaken = timetaken;

			}
		}

		System.out.println("Total products created " + totalProductsCreated);
		System.out.println("Time taken " + maxTimeTaken);

		// Shutdown Executor
		executor.shutdown();
		if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
			executor.shutdownNow();
		}
		return totalProductsCreated;
	}

}
