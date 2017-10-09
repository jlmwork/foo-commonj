/**
 * 
 */
package de.myfoo.commonj.jboss;

import java.util.concurrent.ThreadPoolExecutor;

import org.jboss.system.ServiceMBean;

/**
 * Abstract service MBean - contains the thread pool and
 * JNDI settings.
 * 
 * @author Andreas Keldenich
 */
public interface FooServiceMBean extends ServiceMBean {

	/**
	 * Get the core size of the <code>WorkManager</code> thread pool. 
	 * @see ThreadPoolExecutor
	 * @return the core size
	 */
    public int getCoreThreads();

    /**
     * Set the core size of the <code>WorkManager</code> thread pool.
     * @param coreThreads new core size
     */
    public void setCoreThreads(int coreThreads);

    /**
     * Get the max size of the <code>WorkManager</code> thread pool. 
	 * @see ThreadPoolExecutor
	 * @return the max size
     */
    public int getMaxThreads();

    /**
     * Set the max size of the <code>WorkManager</code> thread pool.
     * @param maxThreads new max size
     */
    public void setMaxThreads(int maxThreads);

    /**
     * Get the JNDI name of the <code>WorkManager</code>.
     * @return JNDI name
     */
    public String getJNDIName();

    /**
     * Set the JNDI name of the <code>WorkManager</code>.
     * @param JNDIName new JNDI name
     */
    public void setJNDIName(String JNDIName);
    
    /**
     * Get the length of the thread pool queue.
     * @return the length
     */
    public int getQueueLength();
    
}
