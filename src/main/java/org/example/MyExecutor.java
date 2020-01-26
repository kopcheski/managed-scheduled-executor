package org.example;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
public class MyExecutor {

	@Resource
	private ManagedScheduledExecutorService managedScheduledExecutorService;

	private ScheduledFuture future;

	private int count;

	private Runnable myTask = () -> {
		System.out.println("Current count is " + ++count);

		if (count == 3) {
			throw new RuntimeException("Throwing exception to suppress subsequent executions.");
		}

		if (count == 4) {
			throw new RuntimeException("So it should never get here.");
		}
	};

	@PostConstruct
	private void init() {
		future = managedScheduledExecutorService.scheduleWithFixedDelay(
				myTask, 0, 2, TimeUnit.SECONDS);
	}

	@PreDestroy
	private void preDestroy() {
		future.cancel(true);
	}

}