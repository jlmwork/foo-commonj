/**
 * #(@) FixedRateTimer.java Aug 16, 2006
 */
package de.myfoo.commonj.timers;

import commonj.timers.TimerListener;

/**
 * Fixed rate timer.
 *
 * @author Andreas Keldenich
 */
public final class FixedRateTimer extends FooTimer {

	/**
	 * Creates a new instance of FixedRateTimer.
	 * 
	 * @param startTime start time
	 * @param period execution period
	 * @param listener the timer listener for this timer.
	 */
	public FixedRateTimer(long startTime, long period, TimerListener listener) {
		super(startTime, period, listener);
	}

	/**
	 * Compute the next execution time.
	 * 
	 * @see de.myfoo.commonj.timers.FooTimer#computeNextExecutionTime()
	 */
	protected void computeNextExecutionTime() {
		long currentTime = System.currentTimeMillis();
		long execTime = scheduledExecutionTime + period;
		
		while (execTime <= currentTime) {
			execTime += period;
		}
		
		scheduledExecutionTime = execTime;
	}

}
