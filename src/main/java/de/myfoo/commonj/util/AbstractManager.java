/**
 * #(@) AbstractManager.java Aug 16, 2006
 */
package de.myfoo.commonj.util;

/**
 * Abstract manger class - parent of all <code>TimerManager</code> and 
 * <code>WorkManagers</code>
 *
 * @author Andreas Keldenich
 */
public abstract class AbstractManager {

	protected ThreadPool pool;

	/**
	 * Creates a new instance of AbstractManager.
	 */
	public AbstractManager(ThreadPool pool) {
		this.pool = pool;
	}
	
	/**
	 * Shutdown the thread pool.
	 */
	public void shutdown() {
		// shutdown the thread pool
		pool.shutdown();
	}
	
	/**
	 * Force shutdown the thread pool.
	 */
	public void forceShutdown() {
		// shutdown the thread pool
		pool.forceShutdown();
	}
	
}
