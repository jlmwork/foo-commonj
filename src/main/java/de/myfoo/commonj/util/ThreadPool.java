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
public final class ThreadPool {

	private ThreadPoolExecutor pool;
	
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
		pool = new ThreadPoolExecutor(minThreads, maxThreads, 
				20, TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(queueLength),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * @return the coreThreads
	 */
	public int getCoreThreads() {
		return pool.getCorePoolSize();
	}

	/**
	 * @param coreThreads the coreThreads to set
	 */
	public void setCoreThreads(int coreThreads) {
		pool.setCorePoolSize(coreThreads);
	}

	/**
	 * @return the maxThreads
	 */
	public int getMaxThreads() {
		return pool.getMaximumPoolSize();
	}

	/**
	 * @param maxThreads the maxThreads to set
	 */
	public void setMaxThreads(int maxThreads) {
		pool.setMaximumPoolSize(maxThreads);
	}

	/**
	 * @return the queueLength
	 */
	public int getQueueLength() {
		return queueLength;
	}

	/**
	 * @param queueLength the queueLength to set
	 */
	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
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
		pool.execute(command);
	}
	
	/**
	 * Shutdown the pool after processing the currently queue tasks.
	 */
	public void shutdown() {
		pool.shutdown();
	}

	/**
	 * Force shutdown the pool immediately.
	 */
	public void forceShutdown() {
		pool.shutdownNow();
	}

}
