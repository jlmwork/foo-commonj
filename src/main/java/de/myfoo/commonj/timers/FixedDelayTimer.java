/**
 * #(@) FixedDelayTimer.java Aug 16, 2006
 */
package de.myfoo.commonj.timers;

import commonj.timers.TimerListener;

/**
 * Fixed delay timer.
 *
 * @author Andreas Keldenich
 */
public final class FixedDelayTimer extends FooTimer {

	/**
	 * Creates a new instance of FixedDelayTimer.
	 * 
	 * @param startTime start time
	 * @param period execution period
	 * @param listener the timer listener for this timer.
	 */
	public FixedDelayTimer(long startTime, long period, TimerListener listener) {
		super(startTime, period, listener);
	}

	/**
	 * Compute the next execution time.
	 * 
	 * @see de.myfoo.commonj.timers.FooTimer#computeNextExecutionTime()
	 */
	protected void computeNextExecutionTime() {
		scheduledExcecutionTime = System.currentTimeMillis() + period;
	}

}
