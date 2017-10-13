/**
 * #(@) ThreadPool.java Aug 16, 2006
 */
package de.myfoo.commonj.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Thread pool implementation to execute <code>Work</code> and 
 * <code>Timer</code>s.
 *
 * @author Andreas Keldenich
 */
public final class ThreadPool implements ThreadPoolMBean {

	private InstrumentedThreadPoolExecutor poolExecutor;
	
	private int queueLength = 20;
	
	/**
	 * Creates a new instance of ThreadPool.
	 * 
	 * @param minThreads minimum number of threads
	 * @param maxThreads maximum number of threads
	 * @param queueLength length of the execution queue
	 */
	public ThreadPool(int minThreads, int maxThreads, int queueLength) {
		this.queueLength = queueLength;
		poolExecutor = new InstrumentedThreadPoolExecutor(minThreads, maxThreads, 
				20, TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(queueLength),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * @return the coreThreads
	 */
	public int getMinThreads() {
		return poolExecutor.getCorePoolSize();
	}

	/**
	 * @return the coreThreads
	 */
	public int getCoreThreads() {
		return poolExecutor.getCorePoolSize();
	}

	/**
	 * @param coreThreads the coreThreads to set
	 */
	public void setCoreThreads(int coreThreads) {
		poolExecutor.setCorePoolSize(coreThreads);
	}

	/**
	 * @return the maxThreads
	 */
	public int getMaxThreads() {
		return poolExecutor.getMaximumPoolSize();
	}

	/**
	 * @param maxThreads the maxThreads to set
	 */
	public void setMaxThreads(int maxThreads) {
		poolExecutor.setMaximumPoolSize(maxThreads);
	}

	/**
	 * @return the queueLength
	 */
	public int getQueueLength() {
		return poolExecutor.getQueue().size() + poolExecutor.getQueue().remainingCapacity();
	}

	/**
     * Returns the number of enqueued tasks.
     */
    public int getQueueSize() {
		return this.poolExecutor.getQueue().size();
	}

    /**
     * Returns the approximate number of threads that are actively executing tasks.
     */
    public int getActiveCount() {
		return this.poolExecutor.getActiveCount();
	}

    /**
     * Returns the approximate total number of tasks that have completed execution.
     */
    public long getCompletedTaskCount() {
		return this.poolExecutor.getCompletedTaskCount();
	}

    /**
     * Returns the approximate total number of tasks that have ever been scheduled for execution. Because the * states of tasks and threads may change dynamically during computation, the returned value is only an approximation.
     */
    public long getTaskCount() {
		return this.poolExecutor.getTaskCount();
	}

    /**
     * Returns the policy used in case of unexecutable tasks.
     */
    public String getRejectionPolicy() {
		return this.poolExecutor.getRejectedExecutionHandler().getClass().getName();
	}

	/**
	 * Arrange for the given command to be executed by a thread in this
	 * pool. The method normally returns when the command has been
	 * handed off for (possibly later) execution.
	 * 
	 * @param command command to execute
	 * @throws InterruptedException if execution fails
	 */
	public void execute(Runnable command) throws InterruptedException {
		poolExecutor.execute(command);
	}
	
	/**
	 * Shutdown the pool after processing the currently queue tasks.
	 */
	public void shutdown() {
		poolExecutor.shutdown();
	}

	/**
	 * Force shutdown the pool immediately.
	 */
	public void forceShutdown() {
		poolExecutor.shutdownNow();
	}

	long fromNanoToSeconds(long nanos) {
		return TimeUnit.NANOSECONDS.toSeconds(nanos);
	}

	@Override
	public double getRequestPerSecondRetirementRate() {
		return (double) poolExecutor.getNumberOfRequestsRetired()
				/ fromNanoToSeconds(poolExecutor.getAggregateInterRequestArrivalTime());
	}

	@Override
	public double getAverageServiceTime() {
		return fromNanoToSeconds(poolExecutor.getTotalServiceTime())
				/ (double) poolExecutor.getNumberOfRequestsRetired();
	}

	@Override
	public double getAverageTimeWaitingInPool() {
		return fromNanoToSeconds(poolExecutor.getTotalPoolTime())
				/ (double) poolExecutor.getNumberOfRequestsRetired();
	}

	@Override
	public double getAverageResponseTime() {
		return this.getAverageServiceTime() + this.getAverageTimeWaitingInPool();
	}

	@Override
	public double getEstimatedAverageNumberOfActiveRequests() {
		return getRequestPerSecondRetirementRate() * (getAverageServiceTime() + getAverageTimeWaitingInPool());
	}

	@Override
	public double getRatioOfDeadTimeToResponseTime() {
		double poolTime = (double) poolExecutor.getTotalPoolTime();
		return poolTime / (poolTime + (double) poolExecutor.getTotalServiceTime());
	}

	@Override
	public double v() {
		return getEstimatedAverageNumberOfActiveRequests() / (double) Runtime.getRuntime().availableProcessors();
	}
}
