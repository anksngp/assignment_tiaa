package com.tiaa.assignment.worker;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.tiaa.assignment.main.exception.ApplicationException;
import com.tiaa.assignment.model.Bolt;
import com.tiaa.assignment.model.Machine;
import com.tiaa.assignment.model.Material;
import com.tiaa.assignment.model.Output;

public class WorkerTest {

	@Test
	public void testCall() throws Exception {
		BlockingQueue<Material> blockingQueue = new LinkedBlockingQueue<>();
		for (int i = 0; i < 2; i++) {
			try {
				blockingQueue.put(new Bolt());
				blockingQueue.put(new Machine());
				blockingQueue.put(new Bolt());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Worker worker = new Worker(blockingQueue, 1);
		Output call = worker.call();
		assertEquals(2, call.getTimetaken());
		assertEquals(2, call.getProductsCreated());
	}

	@Test
	public void testCallNoProducts() throws Exception {
		BlockingQueue<Material> blockingQueue = new LinkedBlockingQueue<>();
		Worker worker = new Worker(blockingQueue, 1);
		Output call = worker.call();
		assertEquals(0, call.getTimetaken());
		assertEquals(0, call.getProductsCreated());
	}

	@Test(expected = ApplicationException.class)
	public void testCallNullQueue() throws Exception {
		BlockingQueue<Material> blockingQueue = null;
		Worker worker = new Worker(blockingQueue, 1);
		Output call = worker.call();
		assertEquals(10, call.getTimetaken());
		assertEquals(10, call.getProductsCreated());
	}
	
	

}
