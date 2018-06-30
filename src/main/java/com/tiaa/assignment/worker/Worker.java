package com.tiaa.assignment.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import com.tiaa.assignment.main.exception.ApplicationException;
import com.tiaa.assignment.model.Bolt;
import com.tiaa.assignment.model.Machine;
import com.tiaa.assignment.model.Material;
import com.tiaa.assignment.model.Output;

public class Worker implements Callable<Output> {

	private BlockingQueue<Material> blockingQueue;
	private int timeRequiredForCreatingProduct;

	public Worker(BlockingQueue<Material> blockingQueue, int timeRequiredForCreatingProduct) {
		this.blockingQueue = blockingQueue;
		this.timeRequiredForCreatingProduct = timeRequiredForCreatingProduct;
	}

	private void process() throws InterruptedException {
		Thread.sleep(timeRequiredForCreatingProduct * 1000l);
	}

	private void readValuesFrom() throws InterruptedException {
		int boltCount = 0;
		int machineCount = 0;
		while (!blockingQueue.isEmpty()) {
			if (boltCount == 2 && machineCount == 1) {
				return;
			}
			Material token = blockingQueue.take();
			if (token instanceof Machine) {
				machineCount = consumeMachine(machineCount, token);
			} else if (token instanceof Bolt) {
				boltCount = consumeBolt(boltCount, token);
			}
		}

	}

	private int consumeBolt(int boltCount, Material token) throws InterruptedException {
		if (boltCount < 2) {
			boltCount++;
		} else {
			blockingQueue.put(token);
		}
		return boltCount;
	}

	private int consumeMachine(int machineCount, Material token) throws InterruptedException {
		if (machineCount < 1) {
			machineCount++;
		} else {
			blockingQueue.put(token);
		}
		return machineCount;
	}

	@Override
	public Output call() throws InterruptedException {
		int productsCreated = 0;
		int timeTakenByWorker = 0;
		if (null == blockingQueue) {
			throw new ApplicationException("No material found");
		}
		while (!blockingQueue.isEmpty()) {
			readValuesFrom();
			process();
			productsCreated++;
			timeTakenByWorker = timeTakenByWorker + timeRequiredForCreatingProduct;

		}
		return new Output(productsCreated, timeTakenByWorker);
	}

}
