/**
 * #(@) FooTimerManagerFactory.java Aug 15, 2006
 */
package de.myfoo.commonj.timers;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;

import commonj.timers.TimerManager;

import de.myfoo.commonj.util.AbstractFactory;
import de.myfoo.commonj.util.ThreadPool;


/**
 * Factory class for <code>TimerManager</code>s. 
 *
 * @author Andreas Keldenich
 */
public final class FooTimerManagerFactory extends AbstractFactory {
	
	/**
	 * Factory method that returns an instance of the requested 
	 * <code>TimerManager</code>.
	 * 
	 * @param obj The possibly <code>null</code> object containing location or 
	 * 				reference information that can be used in creating an 
	 * 				object.
	 * @param name The name of this object relative to <code>nameCtx</code>,
	 *				or <code>null</code> if no name is specified.
	 * @param nameCtx The context relative to which the <code>name</code> 
	 * 				parameter is specified, or <code>null</code> if 
	 * 				<code>name</code> is relative to the default initial 
	 * 				context.
	 * @param environment The possibly <code>null</code> environment that is #
	 * 				used in creating the object.
	 * @return The requested TimerManager
	 * @throws Exception if this object factory encountered an exception while 
	 * 				attempting to create an object, and no other object 
	 * 				factories are to be tried.
	 */
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, 
			Hashtable environment) throws Exception {
		
		// get timer manager from map
		FooTimerManager timerManager = (FooTimerManager) managers.get(name);
		if (timerManager == null) {
			// lazy inititalization
			int minThreads = 2; 
		    int maxThreads = 10;
		    int queueLength = 10;
		    
		    // get config values
		    Reference ref = (Reference) obj;
		    Enumeration addrs = ref.getAll();
		    while (addrs.hasMoreElements()) {
		    	RefAddr addr = (RefAddr) addrs.nextElement();
		    	String addrName = addr.getType();
		    	String addrValue = (String) addr.getContent();
		        
		    	if (addrName.equals(CONFIG_MAX_THREADS)) {
		    		maxThreads = getValue(addrName, addrValue);
		    	}
		    	else if (addrName.equals(CONFIG_MIN_THREADS)) {
		    		minThreads = getValue(addrName, addrValue);
		        }
		    	else if (addrName.equals(CONFIG_QUEUE_LENGTH)) {
		    		queueLength = getValue(addrName, addrValue);
		        }
		    }
		    
		    // more sanity checks on config values
		    if (minThreads < 1) {
		    	throw new NamingException("minThreads can not be < 1.");
		    }
		    if (minThreads >= maxThreads) {
		    	throw new NamingException("minThreads can not be >= maxThreads.");
		    }
		    
		    // create the thread pool for this work manager
		    ThreadPool pool = new ThreadPool(minThreads, maxThreads, queueLength);
		    
		    // create the timer manager
		    timerManager = new FooTimerManager(pool);
		    managers.put(name, timerManager);
		}
		
		return (TimerManager) timerManager;
	}

}
