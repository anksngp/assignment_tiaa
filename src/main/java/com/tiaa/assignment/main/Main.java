package com.tiaa.assignment.main;

import com.tiaa.assignment.product.Product;

public class Main {

	public static void main(String[] args) {

		// Total bolts and machine available in warehouse
		int totalBolts = 6;
		int totalMachines = 3;
		int numberOfWorkers = 3;
		int timeRequiredForCreatingProduct = 1;

		Product createProduct = new Product();
		createProduct.createProducts(totalBolts, totalMachines, numberOfWorkers, timeRequiredForCreatingProduct);

	}

}
