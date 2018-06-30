package com.tiaa.assignment.product;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class ProductTest {

	@Test
	public void testProduct() throws InterruptedException, ExecutionException {
		Product product = new Product();
		int createProducts = product.createProducts(12, 6, 3, 1);
		assertEquals(6, createProducts);
	}

	@Test
	public void testInvalidInput() throws InterruptedException, ExecutionException {
		Product product = new Product();
		int createProducts = product.createProducts(11, 6, 3, 1);
		assertEquals(0, createProducts);
	}

	@Test
	public void testInvalidInputZero() throws InterruptedException, ExecutionException {
		Product product = new Product();
		int createProducts = product.createProducts(0, 0, 3, 1);
		assertEquals(0, createProducts);
	}

}
