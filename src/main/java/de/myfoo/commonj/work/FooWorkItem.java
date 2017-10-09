/**
 * #(@) FooWorkItem.java May 19, 2006
 */
package de.myfoo.commonj.work;

import java.util.List;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;

/**
 * Implementation of the work item.
 *
 * @author  Andreas Keldenich
 */
public class FooWorkItem implements WorkItem, Runnable {

	private int status = WorkEvent.WORK_ACCEPTED;
	protected Work work = null;
	private WorkListener wl = null;
	private WorkException ex = null;
	private ResultCollector resultCollector = null;
	private List daemons = null;
	
	/**
	 * Creates a new instance of FooWorkItem.
	 * @param work the work
	 */
	public FooWorkItem(Work work, WorkListener wl) {
		this.work = work;
		this.wl = wl;
	}
	
	/**
	 * Get the result of the work.
	 * @return the work.
	 * @see commonj.work.WorkItem#getResult()
	 */
	public Work getResult() throws WorkException {
		if (ex != null) {
			throw ex;
		}
		return work;
	}

	/**
	 * Returns the status of the in-flight work.
	 * @see commonj.work.WorkItem#getStatus()
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Set the status of the work.
	 * @param status status of the work.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Compare to another work item.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		return 0;
	}

	/**
	 * Wrapper around the run method of the work.
	 */
	public void run() {
		
		// check if we want to reject it
		if (work == null) {
			status = WorkEvent.WORK_REJECTED;
			if (wl != null) {
				wl.workRejected(new FooWorkEvent(this, WorkEvent.WORK_REJECTED));
			}
			return;
		}
		status = WorkEvent.WORK_ACCEPTED;
		if (wl != null) {
			wl.workAccepted(new FooWorkEvent(this, WorkEvent.WORK_ACCEPTED));
		}
		
		// start the work
		status = WorkEvent.WORK_STARTED;
		if (wl != null) {
			wl.workStarted(new FooWorkEvent(this, WorkEvent.WORK_STARTED));
		}
		
		// run the work
		FooWorkEvent event = new FooWorkEvent(this, WorkEvent.WORK_COMPLETED);
		try {
			work.run();
		}
		catch (Throwable th) {
			ex = new WorkException("Failed to execute work: " + th.toString(), th);
			event.setException(th);
		}
		finally {
			status = WorkEvent.WORK_COMPLETED;
			if (wl != null) {
				wl.workCompleted(event);
			}
			
			// notify the result collector that the work is done.
			if (resultCollector != null) {
				resultCollector.workDone();
			}
			
			// remove from daemon list
			if (daemons != null) {
				daemons.remove(this);
			}
		}
		
	}

	/**
	 * Set the ResultCollector to support notification
	 * 
	 * @param collector the result collector
	 */
	public void setResultCollector(ResultCollector collector) {
		this.resultCollector = collector;
	}

	/**
	 * Setter for daemon list.
	 * 
	 * @param daemons The daemons to set.
	 */
	public void setDaemons(List daemons) {
		this.daemons = daemons;
	}

}
