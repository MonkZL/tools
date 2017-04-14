package ysccc.com.tghp.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ysccc.com.tghp.utils.LogUtils;

/**
 * Created by Mon on 2016/12/21.
 */
public class VisibleScrollView extends ScrollView {

    private OnScrollToRefreshListener onScrollToRefreshListener;
    private int height;
    private int allHeight;
    private boolean bottomFlag;
    private boolean topFlag;
    private boolean otherFlag;
    private int oldY;
    private boolean scrollToTop;
    private boolean scrollToBottom;
    private int nowY;
    private View topChild;
    private View bottomChild;
    private boolean scrollToTopBottom;
    private boolean scrollToBottomTop;


    public VisibleScrollView(Context context) {
        super(context);
        init();
    }

    public VisibleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisibleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        post(new Runnable() {
            @Override
            public void run() {
                height = getMeasuredHeight();

                View childAt = getChildAt(0);
                if (allHeight <= 0)
                    allHeight = childAt.getMeasuredHeight();

                View lastChild = ((ViewGroup) childAt).getChildAt(((ViewGroup) childAt).getChildCount() - 1);
                View inLastChild = ((ViewGroup) lastChild).getChildAt(1);
                ViewGroup.LayoutParams layoutParams = inLastChild.getLayoutParams();
                layoutParams.height = height + height * 2;
                inLastChild.setLayoutParams(layoutParams);
            }
        });
    }

    public void setOnScrollToRefreshListener(OnScrollToRefreshListener listener) {
        onScrollToRefreshListener = listener;
    }

    public interface OnScrollToRefreshListener {
        void OnScrollToRefreshListener();
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (otherFlag && scrollY <= 0) {
            topFlag = true;
        } else if ((allHeight - height) <= scrollY && scrollY < allHeight) {
            bottomFlag = true;
        } else if (!otherFlag && scrollY >= allHeight) {
            scrollToBottom = false;
            for (int i = 0; i < ((ViewGroup) getChildAt(0)).getChildCount() - 1; i++) {
                ((ViewGroup) getChildAt(0)).getChildAt(i).setVisibility(View.GONE);
            }
            scrollTo(0, 0);
            otherFlag = true;
        } else {
            bottomFlag = false;
            if (!otherFlag) {
                if (bottomChild == null)
                    bottomChild = ((ViewGroup) getChildAt(0)).getChildAt(((ViewGroup) getChildAt(0)).getChildCount() - 1);
                if (bottomChild.getVisibility() == View.VISIBLE) {
                    bottomChild.setVisibility(View.GONE);
                }
            }
        }

        scrollToBottom = false;
        scrollToTopBottom = false;
        scrollToBottomTop = false;
        scrollToTop = false;

        if (topChild == null)
            topChild = ((ViewGroup) getChildAt(0)).getChildAt(0);
        if (topChild.getVisibility() == View.VISIBLE) {
            if (scrollY >= allHeight - height + height / 10 && scrollY <= allHeight - height / 10) {
                scrollToBottom = true;
                scrollToTopBottom = false;
                scrollToBottomTop = false;
            } else if (scrollY < allHeight - height + height / 10 && scrollY > allHeight - height) {
                scrollToBottom = false;
                scrollToBottomTop = false;
                scrollToTop = false;
                scrollToTopBottom = true;
            } else if (scrollY < allHeight && scrollY > allHeight - height / 10) {
                scrollToBottomTop = true;
                scrollToTop = false;
                scrollToTopBottom = false;
                scrollToBottom = false;
            }

            if (scrollY >= allHeight - height + height / 10 && scrollY <= allHeight - height / 10 && bottomChild.getVisibility() == View.VISIBLE) {
                scrollToBottomTop = false;
                scrollToTop = true;
                scrollToTopBottom = false;
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (bottomFlag && ((ViewGroup) getChildAt(0)).getChildAt(((ViewGroup) getChildAt(0)).getChildCount() - 1).getVisibility() == View.GONE) {
                    ((ViewGroup) getChildAt(0)).getChildAt(((ViewGroup) getChildAt(0)).getChildCount() - 1).setVisibility(View.VISIBLE);
                    onScrollToRefreshListener.OnScrollToRefreshListener();
                }

                if (topFlag && otherFlag && ((ViewGroup) getChildAt(0)).getChildAt(0).getVisibility() == View.GONE) {
                    for (int i = 0; i < ((ViewGroup) getChildAt(0)).getChildCount() - 1; i++) {
                        ((ViewGroup) getChildAt(0)).getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    otherFlag = false;
                    topFlag = false;
                    scrollTo(0, allHeight);
                }
                break;
            case MotionEvent.ACTION_UP:
                nowY = (int) ev.getY();

                if (nowY > oldY) {
                    //向下滑动
                    scrollToBottom = false;
                } else {
                    scrollToTop = false;
                }


                if (scrollToTop) {
                    scrollToTop = false;
                    scrollToTopBottom = false;
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, 0);
                        }
                    });
                }
                if (scrollToBottom) {
                    scrollToBottom = false;
                    scrollToTopBottom = false;
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, allHeight - 1);
                        }
                    });
                }

                if (scrollToTopBottom) {
                    scrollToTopBottom = false;
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, allHeight - height);
                        }
                    });
                }

                if (scrollToBottomTop) {
                    scrollToBottomTop = false;
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, allHeight - 1);
                        }
                    });
                }

                break;
        }
        return super.onTouchEvent(ev);
    }
}
