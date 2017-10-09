package de.myfoo.commonj.jboss;

import commonj.timers.TimerManager;

import de.myfoo.commonj.timers.FooTimerManager;

/**
 * JBoss JSR 236 <code>TimerManager</code> service.
 * 
 * @author Andreas Keldenich
 */
public class TimerManagerService extends FooService implements
		WorkManagerServiceMBean {

	private TimerManager timerManager;
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#startService()
	 */
	@Override
	protected void startService() throws Exception {
		super.startService();
		timerManager = new FooTimerManager(pool);
		bind(timerManager, JNDIName);
	}

}