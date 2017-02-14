package ceritadongeng.dongenganakindonesia.dongeng.widget;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
//import android.view.View.OnClickListener;

/**
 * Detects left and right swipes across a view.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private boolean mIsScrolling = false;
    public OnSwipeTouchListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            // i'm only scrolling along the X axis
//            mIsScrolling = true;
//            onScrool();
////            handleScroll(Math.round((e2.getX() - e1.getX())));
//            return true;
//        }

        @Override
        public boolean onSingleTapConfirmed (MotionEvent e) {
            View view = null;
            onClick(view, e);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            View view = null;
            onDoubleClick(view , e);
            return super.onDoubleTap(e);
       }

        @Override
        public void onLongPress(MotionEvent e) {
            View view = null;
            onLongClick(view, e);
            super.onLongPress(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, final float distanceX, float distanceY) {
            OnScrool();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }

    public void onClick(View view, MotionEvent e) {

    }

public void OnScrool(){

}
    public void onDoubleClick(View view, MotionEvent e) {

    }

    public void onLongClick(View view, MotionEvent e) {

    }
}