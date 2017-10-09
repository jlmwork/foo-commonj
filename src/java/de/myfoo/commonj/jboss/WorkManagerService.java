package de.myfoo.commonj.jboss;

import de.myfoo.commonj.work.FooWorkManager;

/**
 * JBoss JSR 237 WorkManager service.
 * 
 * @author Andreas Keldenich
 */
public class WorkManagerService extends FooService implements
		WorkManagerServiceMBean {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#startService()
	 */
	@Override
	protected void startService() throws Exception {
		super.startService();
		bind(new FooWorkManager(pool), JNDIName);
	}

}