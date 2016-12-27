package ysccc.com.tghp.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;

import ysccc.com.tghp.R;
import ysccc.com.tghp.utils.LogUtils;

/**
 * 张梁 2016/11/16.
 */
public class NumChooseView extends LinearLayout implements View.OnClickListener {

    //减按钮
    private Button mCut;
    //加按钮
    private Button mPlus;
    //数字显示textView
    private TextView mNum;
    //记录目前这个NumChooseView的数字
    private int num;
    //记录所有NumChooseView的数字
    public static int totalNum;
    //记录使用过NumChooseView的ListView中操作过的position和对应的数字
    public static TreeMap<VpPosition, Integer> NumMap = new TreeMap<>();
    //注册的vpPosition
    private int vpPosition = -1;
    //注册的listPosition;
    private int listPosition = -1;

    //回调接口
    private TotalNumChanged totalNumChanged;

    public NumChooseView(Context context) {
        super(context);
    }

    public NumChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_num_choose, this, false);

        mCut = ((Button) view.findViewById(R.id.cut));
        mPlus = ((Button) view.findViewById(R.id.plus));
        mNum = ((TextView) view.findViewById(R.id.num));

        mCut.setOnClickListener(this);
        mPlus.setOnClickListener(this);

        addView(view);
    }

    public NumChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cut:
                if (judgeNum()) {
                    num--;
                    totalNum--;
                    NumMap.put(new VpPosition(vpPosition, listPosition), num);
                    totalNumChanged.getTotalNum(totalNum);
                    if (!judgeNum())
                        mCut.setTextColor(Color.rgb(200, 200, 200));
                }

                mNum.setText(num + "");
                break;

            case R.id.plus:
                num++;
                totalNum++;
                NumMap.put(new VpPosition(vpPosition, listPosition), num);
                totalNumChanged.getTotalNum(totalNum);
                if (judgeNum())
                    mCut.setTextColor(Color.rgb(0, 0, 0));
                mNum.setText(num + "");
                break;
        }
    }


    //注册位置
    public void setPosition(int vpPosition, int listPosition) {
        this.vpPosition = vpPosition;
        this.listPosition = listPosition;
        if (vpPosition != -1 && listPosition != -1)
            num = NumMap.get(new VpPosition(vpPosition, listPosition)) == null ? 0 : NumMap.get(new VpPosition(vpPosition, listPosition));
    }

    //判断数字是否为0
    private boolean judgeNum() {

        if (num == 0) {
            return false;
        } else {
            return true;
        }


    }

    //得到数字
    public int getNum() {
        return num;
    }

    //设置回调
    public void setTotalNumChanged(TotalNumChanged totalNumChanged) {
        this.totalNumChanged = totalNumChanged;
    }

    //设置数字
    public void setNum(int num) {
        if (num == 0) {
            mCut.setTextColor(Color.rgb(200, 200, 200));
        } else {
            mCut.setTextColor(Color.rgb(0, 0, 0));
        }
        mNum.setText(num + "");
    }

    public interface TotalNumChanged {
        void getTotalNum(int totalNum);
    }


    public static class VpPosition implements Comparable<VpPosition> {
        private int vpPositon;
        private int listPosition;

        public VpPosition() {
        }

        public VpPosition(int vpPositon, int listPosition) {
            this.vpPositon = vpPositon;
            this.listPosition = listPosition;
        }

        public int getVpPositon() {
            return vpPositon;
        }

        public void setVpPositon(int vpPositon) {
            this.vpPositon = vpPositon;
        }

        public int getListPosition() {
            return listPosition;
        }

        public void setListPosition(int listPosition) {
            this.listPosition = listPosition;
        }

        @Override
        public int hashCode() {
            LogUtils.log("hashcode");
            return vpPositon + listPosition;
        }

        @Override
        public boolean equals(Object o) {
            LogUtils.log("equals");

            if (vpPositon == ((VpPosition) o).getVpPositon() && listPosition == ((VpPosition) o).getListPosition()) {
                return true;
            }
            return false;
        }

        @Override
        public int compareTo(VpPosition another) {
            int vpPositon = another.getVpPositon();
            int listPosition = another.getListPosition();

            int numP = this.vpPositon - vpPositon;
            int numL = this.listPosition - listPosition;

            if (numP != 0) {
                return numP;
            } else {
                return numL;
            }
        }
    }
}
