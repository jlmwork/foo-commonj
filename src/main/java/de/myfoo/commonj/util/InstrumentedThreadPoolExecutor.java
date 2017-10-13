package de.myfoo.commonj.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InstrumentedThreadPoolExecutor extends ThreadPoolExecutor {

    // Keep track of all of the request times
    private final ConcurrentHashMap<Runnable, Long> timeOfRequest =
            new ConcurrentHashMap<>();
    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private long lastArrivalTime;
    // other variables are AtomicLongs and AtomicIntegers
    private AtomicInteger numberOfRequests = new AtomicInteger();
    private AtomicInteger numberOfRequestsRetired = new AtomicInteger();
    private AtomicLong aggregateInterRequestArrivalTime = new AtomicLong();
    private AtomicLong totalServiceTime = new AtomicLong();
    private AtomicLong totalPoolTime = new AtomicLong();

    public InstrumentedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public InstrumentedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public InstrumentedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public InstrumentedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread worker, Runnable task) {
        super.beforeExecute(worker, task);
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable task, Throwable t) {
        try {
            totalServiceTime.addAndGet(System.nanoTime() - startTime.get());
            totalPoolTime.addAndGet(startTime.get() - timeOfRequest.remove(task));
            numberOfRequestsRetired.incrementAndGet();
        } finally {
            super.afterExecute(task, t);
        }
    }

    @Override
    public void execute(Runnable task) {
        long now = System.nanoTime();

        numberOfRequests.incrementAndGet();
        synchronized (this) {
            if (lastArrivalTime != 0L) {
                aggregateInterRequestArrivalTime.addAndGet(now - lastArrivalTime);
            }
            lastArrivalTime = now;
            timeOfRequest.put(task, now);
        }
        super.execute(task);
    }
    
    /**
	 * @return the aggregateInterRequestArrivalTime
	 */
	public long getAggregateInterRequestArrivalTime() {
		return aggregateInterRequestArrivalTime.get();
	}

	/**
	 * @return the totalPoolTime
	 */
	public long getTotalPoolTime() {
		return totalPoolTime.get();
	}

	/**
	 * @return the totalServiceTime
	 */
	public long getTotalServiceTime() {
		return totalServiceTime.get();
	}

	/**
	 * @return the numberOfRequests
	 */
	public int getNumberOfRequests() {
		return numberOfRequests.get();
	}

	/**
	 * @return the numberOfRequestsRetired
	 */
	public int getNumberOfRequestsRetired() {
		return numberOfRequestsRetired.get();
	}
 }