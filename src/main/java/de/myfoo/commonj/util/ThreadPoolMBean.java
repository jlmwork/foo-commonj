package de.myfoo.commonj.util;

/**
 */
public interface ThreadPoolMBean {

    /** Lifecycle method : execute a runnable command */
    void execute(Runnable command) throws InterruptedException;

    /** Lifecycle method : shutdowns the threadpool */
    void shutdown();

    /** Lifecycle method : kills the threadpool */
    void forceShutdown();

    /**
	 * @return the coreThreads
	 */
    int getCoreThreads();

    /**
	 * @param coreThreads the coreThreads to set
	 */
    void setCoreThreads(int coreThreads);
    
    /**
	 * @param maxThreads the maxThreads to set
	 */
    void setMaxThreads(int maxThreads);

    /**
     * Returns the core number of threads.
     */
    int getMinThreads();

    /**
     * Returns the maximum allowed number of threads.
     */
    int getMaxThreads();

    /**
     * Returns the allowed size of the Queue.
     */
    int getQueueLength();

    /**
     * Returns the number of enqueued tasks.
     */
    int getQueueSize();

    /**
     * Returns the approximate number of threads that are actively executing tasks.
     */
    int getActiveCount();

    /**
     * Returns the approximate total number of tasks that have completed execution.
     */
    long getCompletedTaskCount();

    /**
     * Returns the approximate total number of tasks that have ever been scheduled for execution. Because the * states of tasks and threads may change dynamically during computation, the returned value is only an approximation.
     */
    long getTaskCount();

    /**
     * Returns the policy used in case of unexecutable tasks.
     */
    String getRejectionPolicy();
}