package sg.vouch.vouchsdk.utils

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;




// create by deannrchsnl on 2019-12-03
// contact me at deannurchusnulchotimah@gmail.com

abstract class CountdownTimerPause
    (
    /**
     * Real time remaining until timer completes
     */
    private var mMillisInFuture: Long,
    /**
     * The interval in millis that the user receives callbacks
     */
    private val mCountdownInterval: Long,
    /**
     * True if timer was started running, false if not.
     */
    private val mRunAtStart: Boolean
) {

    /**
     * Millis since boot when alarm should stop.
     */
    private var mStopTimeInFuture: Long = 0

    /**
     * Total time on timer at start
     */
    private val mTotalCountdown: Long

    /**
     * The time remaining on the timer when it was paused, if it is currently paused; 0 otherwise.
     */
    private var mPauseTimeRemaining: Long = 0

    /**
     * Tests whether the timer is paused.
     * @return true if the timer is currently paused, false otherwise.
     */
    val isPaused: Boolean
        get() = mPauseTimeRemaining > 0


    val isRunning: Boolean
        get() = !isPaused


    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {

            synchronized(this@CountdownTimerPause) {
                val millisLeft = timeLeft()

                if (millisLeft <= 0) {
                    cancel()
                    onFinish()
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft)
                } else {
                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisLeft)

                    // take into account user's onTick taking time to execute
                    var delay = mCountdownInterval - (SystemClock.elapsedRealtime() - lastTickStart)

                    // special case: user's onTick took more than mCountdownInterval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval

                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }

    init {
        mTotalCountdown = mMillisInFuture
    }

    /**
     * Cancel the countdown and clears all remaining messages
     */
    fun cancel() {
        mHandler.removeMessages(MSG)
    }

    /**
     * Create the timer object.
     */
    @Synchronized
    fun create(): CountdownTimerPause {
        if (mMillisInFuture <= 0) {
            onFinish()
        } else {
            mPauseTimeRemaining = mMillisInFuture
        }

        if (mRunAtStart) {
            resume()
        }

        return this
    }

    /**
     * Pauses the counter.
     */
    fun pause() {
        if (isRunning) {
            mPauseTimeRemaining = timeLeft()
            cancel()
        }
    }

    /**
     * Resumes the counter.
     */
    fun resume() {
        if (isPaused) {
            mMillisInFuture = mPauseTimeRemaining
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
            mHandler.sendMessage(mHandler.obtainMessage(MSG))
            mPauseTimeRemaining = 0
        }
    }

    /**
     * Returns the number of milliseconds remaining until the timer is finished
     * @return number of milliseconds remaining until the timer is finished
     */
    fun timeLeft(): Long {
        var millisUntilFinished: Long
        if (isPaused) {
            millisUntilFinished = mPauseTimeRemaining
        } else {
            millisUntilFinished = mStopTimeInFuture - SystemClock.elapsedRealtime()
            if (millisUntilFinished < 0) millisUntilFinished = 0
        }
        return millisUntilFinished
    }

    /**
     * Returns the number of milliseconds in total that the timer was set to run
     * @return number of milliseconds timer was set to run
     */
    fun totalCountdown(): Long {
        return mTotalCountdown
    }

    /**
     * Returns the number of milliseconds that have elapsed on the timer.
     * @return the number of milliseconds that have elapsed on the timer.
     */
    fun timePassed(): Long {
        return mTotalCountdown - timeLeft()
    }

    /**
     * Returns true if the timer has been started, false otherwise.
     * @return true if the timer has been started, false otherwise.
     */
    fun hasBeenStarted(): Boolean {
        return mPauseTimeRemaining <= mMillisInFuture
    }

    /**
     * Callback fired on regular interval
     * @param millisUntilFinished The amount of time until finished
     */
    abstract fun onTick(millisUntilFinished: Long)

    /**
     * Callback fired when the time is up.
     */
    abstract fun onFinish()

    companion object {


        private val MSG = 1
    }
}