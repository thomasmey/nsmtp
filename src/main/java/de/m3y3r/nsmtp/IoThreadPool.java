package de.m3y3r.nsmtp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum IoThreadPool {

	INSTANCE;

	private ExecutorService executor;

	private IoThreadPool() {
		this.executor = Executors.newFixedThreadPool(5);
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}

	public <T> Future<T> execute(Callable<T> task) {
		return executor.submit(task);
	}
}
