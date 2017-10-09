/**
 * 
 */
package de.myfoo.commonj.jboss;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.jboss.system.ServiceMBeanSupport;
import org.jboss.util.naming.NonSerializableFactory;

import de.myfoo.commonj.util.ThreadPool;

/**
 * Abstract JBoss service.
 * 
 * @author Andreas Keldenich
 */
public abstract class FooService extends ServiceMBeanSupport implements FooServiceMBean {

	private static final int QUEUE_LENGTH = 50;
	
	protected int coreThreads = 10;
	protected int maxThreads = 20;
	protected String JNDIName;
	
	protected ThreadPool pool;
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#startService()
	 */
	@Override
	protected void startService() throws Exception {
		super.startService();
		pool = new ThreadPool(coreThreads, maxThreads, QUEUE_LENGTH);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#stopService()
	 */
	@Override
	protected void stopService() throws Exception {
		unbind(JNDIName);
		pool.shutdown();
		pool = null;
		super.stopService();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.WorkManagerServiceMBean#getCoreThreads()
	 */
	public int getCoreThreads() {
		return coreThreads;
	}

	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.WorkManagerServiceMBean#setCoreThreads(int)
	 */
	public void setCoreThreads(int coreThreads) {
		this.coreThreads = coreThreads;
		if (pool != null) {
			// Allow to change the pool size while the service is started.
			pool.setCoreThreads(coreThreads);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.WorkManagerServiceMBean#getMaxThreads()
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.WorkManagerServiceMBean#setMaxThreads(int)
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
		if (pool != null) {
			// Allow to change the pool size while the service is started.
			pool.setMaxThreads(maxThreads);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.WorkManagerServiceMBean#getJNDIName()
	 */
	public String getJNDIName() {
		return JNDIName;
	}

	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.WorkManagerServiceMBean#setJNDIName(java.lang.String)
	 */
	public void setJNDIName(String JNDIName) {
		this.JNDIName = JNDIName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.myfoo.commonj.jboss.FooServiceMBean#getQueueLength()
	 */
	public int getQueueLength() {
		return QUEUE_LENGTH;
	}
	
	/**
	 * Bind an object to its JNDI name.
	 * 
	 * @param object object to bind
	 * @param bindName JNDI name
	 * @throws NamingException if object can't be bound
	 */
	protected void bind(Object object, String bindName) throws NamingException {
		NonSerializableFactory.bind(bindName, object);
		Context ctx = new InitialContext();
		try {
			Name name = ctx.getNameParser("").parse(bindName);
			while (name.size() > 1) {
				String ctxName = name.get(0);
				try {
					ctx = (Context) ctx.lookup(ctxName);
				} catch (NameNotFoundException e) {
					ctx = ctx.createSubcontext(ctxName);
				}
				name = name.getSuffix(1);
			}
			StringRefAddr addr = new StringRefAddr("nns", bindName);
			Reference ref = new Reference(object.getClass().getName(), 
					addr,
					NonSerializableFactory.class.getName(), 
					null);
			ctx.bind(name.get(0), ref);
			log.info("Bound " + object.getClass().getName() + " to: " + JNDIName);
		} 
		finally {
			ctx.close();
		}
	}

	/**
	 * Unbind an object to from JNDI.
	 * 
	 * @param bindName JNDI name
	 * @throws NamingException if object can't be unbound
	 */
	protected void unbind(String bindName) throws NamingException {
		if (bindName != null) {
			InitialContext ctx = new InitialContext();
			try {
				ctx.unbind(bindName);
			}
			finally {
				ctx.close();
			}
			NonSerializableFactory.unbind(bindName);
		}
	}
	
}
