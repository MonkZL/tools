package zl.com.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

/**
 * Created by Mon on 2016/12/8.
 */
public class SuperRadioButton extends RadioButton implements CompoundButton.OnCheckedChangeListener {

    private final int DEFAULT_TITLE_COLOR = Color.BLACK;
    private final int DEFAULT_TITLE_CHECKED_COLOR = Color.RED;
    private final int DEFAULT_BACKGROUND_UNCHECKED_COLOR = Color.WHITE;
    private final int DEFAULT_BACKGROUND_CHECKED_COLOR = Color.GRAY;
    private final int BORDER_COLOR = Color.BLACK;
    private int mFirstTitleColor;
    private int mSecondTitleColor;
    private String mFirstTitle = "mFirstTitle";
    private String mSecondTitle = "mSecondTitle";
    private Paint paint;
    private final int FIRST_TITLE_SIZE = 20;
    private final int SECOND_TITLE_SIZE = 15;
    private final int OFFSET = 20;
    private int mFirstTitleSize;
    private int mSecondTitleSize;
    private int width;
    private int height;
    private Rect mFirstTitleBound;
    private Rect mSecondTitleBound;
    private int mOffset;
    private int mFirstTitleCheckedColor;
    private int mSecondTitleCheckedColor;
    private int mBackgroundUnchecked;
    private int mBackgroundChecked;
    private int RADIUS = 10;
    private int BORDER_WIDTH = 5;
    private int mRadius;
    private int mBorderWidth;
    private int mBorderColor;
    private boolean weightFlagWidth = false;
    private boolean weightFlagHeight = false;

    public SuperRadioButton(Context context) {
        super(context);
        init(context, null);
    }

    public SuperRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public SuperRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        paint = new Paint();

        paint.setAntiAlias(true);
        handleTypedArray(context, attrs);
        setOnCheckedChangeListener(this);

    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperRadioButton);
        //获取第一段没被选中的文字颜色
        mFirstTitleColor = typedArray.getColor(R.styleable.SuperRadioButton_first_title_color, DEFAULT_TITLE_COLOR);
        //获取第二段没被选中的文字颜色
        mSecondTitleColor = typedArray.getColor(R.styleable.SuperRadioButton_second_title_color, DEFAULT_TITLE_COLOR);
        //获取第一段文字
        mFirstTitle = typedArray.getString(R.styleable.SuperRadioButton_first_title);
        //获取第二段文字
        mSecondTitle = typedArray.getString(R.styleable.SuperRadioButton_second_title);
        //获取第一段文字宽度
        mFirstTitleSize = typedArray.getDimensionPixelSize(R.styleable.SuperRadioButton_first_title_size, FIRST_TITLE_SIZE);
        paint.setTextSize(mFirstTitleSize);
        mFirstTitleBound = new Rect();
        paint.getTextBounds(mFirstTitle, 0, mFirstTitle.length(), mFirstTitleBound);
        //获取第二段文字宽度
        mSecondTitleSize = typedArray.getDimensionPixelSize(R.styleable.SuperRadioButton_second_title_size, SECOND_TITLE_SIZE);
        paint.setTextSize(mSecondTitleSize);
        mSecondTitleBound = new Rect();
        paint.getTextBounds(mSecondTitle, 0, mSecondTitle.length(), mSecondTitleBound);
        //获取第一段文字被选中的颜色
        mFirstTitleCheckedColor = typedArray.getColor(R.styleable.SuperRadioButton_first_title_checked_color, DEFAULT_TITLE_CHECKED_COLOR);
        //获取第一段文字被选中的颜色
        mSecondTitleCheckedColor = typedArray.getColor(R.styleable.SuperRadioButton_second_title_checked_color, DEFAULT_TITLE_CHECKED_COLOR);
        //获取没被选中的背景颜色
        mBackgroundUnchecked = typedArray.getColor(R.styleable.SuperRadioButton_background_unchecked, DEFAULT_BACKGROUND_UNCHECKED_COLOR);
        //获取被选中的背景颜色
        mBackgroundChecked = typedArray.getColor(R.styleable.SuperRadioButton_background_checked, DEFAULT_BACKGROUND_CHECKED_COLOR);
        //获取角度
        mRadius = typedArray.getDimensionPixelSize(R.styleable.SuperRadioButton_radius, RADIUS);
        //获取两段文字的偏移量
        mOffset = typedArray.getDimensionPixelSize(R.styleable.SuperRadioButton_offset, OFFSET);
        //获取边界宽度
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.SuperRadioButton_border_width, BORDER_WIDTH);
        //获取边界颜色
        mBorderColor = typedArray.getColor(R.styleable.SuperRadioButton_border_color, BORDER_COLOR);


        typedArray.recycle();
    }


    private int getMax(int one, int two) {
        return one > two ? one : two;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        width = 0;
        if (modeWidth == MeasureSpec.AT_MOST) {
            width = getPaddingLeft() + getMax(mFirstTitleBound.width(), mSecondTitleBound.width()) + getPaddingRight();
            width += mBorderWidth * 2;
        } else if (modeWidth == MeasureSpec.EXACTLY) {
            width = getPaddingLeft() + MeasureSpec.getSize(widthMeasureSpec) + getPaddingRight();
            width += mBorderWidth * 2;
            int childCount_w = ((ViewGroup) getParent()).getChildCount();
            int windowWidth = ((ViewGroup) getParent()).getWidth();
            int rePlaceWidth = windowWidth / childCount_w;
            if (rePlaceWidth <= width) {
                width = rePlaceWidth;
            }
        }

        height = 0;
        if (modeHeight == MeasureSpec.AT_MOST) {
            height = getPaddingTop() + getPaddingBottom() + mFirstTitleBound.height() + mSecondTitleBound.height() + mOffset;
            height += mBorderWidth * 2;
        } else if (modeHeight == MeasureSpec.EXACTLY) {
            int childCount_h = ((ViewGroup) getParent()).getChildCount();
            int rePlaceHeight = ((ViewGroup) getParent()).getHeight() / childCount_h;
            height = getPaddingTop() + getPaddingBottom() + MeasureSpec.getSize(heightMeasureSpec) + mOffset;
            height += mBorderWidth * 2;
            if (rePlaceHeight <= height) {
                this.height = rePlaceHeight;
            }
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            //画背景
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(isChecked() ? mBackgroundChecked : mBackgroundUnchecked);
            canvas.drawRoundRect(mBorderWidth, mBorderWidth, width - mBorderWidth, height - mBorderWidth, mRadius, mRadius, paint);

            //画边框
            paint.setStrokeWidth(mBorderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(mBorderColor);
            canvas.drawRoundRect(mBorderWidth, mBorderWidth, width - mBorderWidth, height - mBorderWidth, mRadius, mRadius, paint);

            //画第一个title
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(isChecked() ? mFirstTitleCheckedColor : mFirstTitleColor);
            paint.setTextSize(mFirstTitleSize);
            canvas.drawText(mFirstTitle, width / 2 - mFirstTitleBound.width() / 2 - (mFirstTitleBound.width() / mFirstTitle.length() / 10), height / 2 - mOffset / 2, paint);

            //画第二个title
            paint.setColor(isChecked() ? mSecondTitleCheckedColor : mSecondTitleColor);
            paint.setTextSize(mSecondTitleSize);
            canvas.drawText(mSecondTitle, width / 2 - mSecondTitleBound.width() / 2 - (mSecondTitleBound.width() / mSecondTitle.length() / 10), height / 2 + mFirstTitleBound.height() + mOffset / 2, paint);

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        invalidate();
    }
}
