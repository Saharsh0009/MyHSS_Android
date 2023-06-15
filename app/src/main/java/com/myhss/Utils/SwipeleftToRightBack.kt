package com.myhss.Utils

import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by Nikunj Dhokia on 12-06-2023.
 */
class SwipeleftToRightBack {
    companion object {
        private lateinit var gestureDetector: GestureDetector

        fun enableSwipeBack(activity: Activity) {
            gestureDetector = GestureDetector(activity, SwipeGestureListener(activity))
            activity.window.decorView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                false
            }
        }

        fun enableSwipeBackHalf(activity: Activity) {
            gestureDetector = GestureDetector(activity, SwipeGestureListenerHalf(activity))
            activity.window.decorView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                false
            }
        }

        fun enableSwipeBackFullView(activity: Activity) {
            gestureDetector = GestureDetector(activity, SwipeGestureListenerFullView(activity))
        }

        fun dispatchTouchEvent(activity: Activity, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }
    }


    private class SwipeGestureListener(private val activity: Activity) :
        GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y

            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(
                    velocityX
                ) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceX > 0) {
//                    activity.onBackPressed() // Trigger the back press action
                    activity.finish() // Trigger the back press action
                }
                return true
            }
            return false
        }
    }

    private class SwipeGestureListenerHalf(private val activity: Activity) :
        GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        private val screenMiddleX: Float = activity.window.decorView.width.toFloat() / 2

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y

            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(
                    velocityX
                ) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceX > 0 && e1.x <= screenMiddleX) {
                    activity.onBackPressed() // Trigger the back press action
                }
                return true
            }
            return false
        }
    }

    private class SwipeGestureListenerFullView(private val activity: Activity) :
        GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        private val screenMiddleX: Float by lazy {
            activity.window.decorView.width.toFloat() / 2
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y

            if (Math.abs(distanceX) > Math.abs(distanceY) &&
                Math.abs(distanceX) > SWIPE_THRESHOLD &&
                Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceX > 0 && e1.x <= screenMiddleX) {
                    activity.finish() // Trigger the back press action
                }
                return true
            }
            return false
        }
    }


}