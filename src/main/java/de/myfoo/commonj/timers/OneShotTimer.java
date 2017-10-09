/**
 * #(@) OneShotTimer.java Aug 16, 2006
 */
package de.myfoo.commonj.timers;

import commonj.timers.TimerListener;

/**
 * A timer that expires once and stops execution.
 *
 * @author Andreas Keldenich
 */
public final class OneShotTimer extends FooTimer {

	/**
	 * Creates a new instance of OneShotTimer.
	 * 
	 * @param startTime start time
	 * @param listener the timer listener
	 */
	public OneShotTimer(long startTime, TimerListener listener) {
		super(startTime, 0L, listener);
	}

	/**
	 * Compute the next execution time - never again.
	 * 
	 * @see de.myfoo.commonj.timers.FooTimer#computeNextExecutionTime()
	 */
	protected void computeNextExecutionTime() {
		// empty
	}

}
