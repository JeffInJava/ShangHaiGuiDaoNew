package com.ineasnet.shanghaiguidao2.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ineasnet.shanghaiguidao2.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * @创建者     wjh
 * @创建时间   2016/8/5 21:22
 * @描述        ${自定义下载的进度提示框}
 */
public class CommonProgressDialog extends AlertDialog implements View.OnClickListener {


    private ProgressBar mProgress;
    private TextView mProgressNumber;
    private TextView mProgressPercent;
    private TextView mProgressMessage;

    private Handler mViewUpdateHandler;
    private int          mMax;
    private CharSequence mMessage;
    private boolean      mHasStarted;
    private int          mProgressVal;

    private String TAG = "CommonProgressDialog";
    private String mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;
    private TextView tvCancel;
    //是否显示
    private boolean isVisible = true;

    public CommonProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initFormats();
    }


    public void setIsVisible(boolean flag){
        this.isVisible = flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_progress_dialog);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgressNumber = (TextView) findViewById(R.id.progress_number);
        mProgressPercent = (TextView) findViewById(R.id.progress_percent);
        mProgressMessage = (TextView) findViewById(R.id.progress_message);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        //      LayoutInflater inflater = LayoutInflater.from(getContext());
        mViewUpdateHandler = new Handler() {


            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                int progress = mProgress.getProgress();
                int max = mProgress.getMax();
                double dProgress = Double.valueOf(getMaxDouble(progress));
                double dMax = Double.valueOf(getMaxDouble(max));
                if (isVisible){
                    if (mProgressNumberFormat != null) {
                        String format = mProgressNumberFormat;
                        mProgressNumber.setText(String.format(format, dProgress, dMax));
                    } else {
                        mProgressNumber.setText("");
                    }
                }else{
                    mProgressNumber.setVisibility(View.GONE);
                }

                if (mProgressPercentFormat != null) {
                    double percent = (double) progress / (double) max;
                    SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
                    tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                            0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mProgressPercent.setText(tmp);
                } else {
                    mProgressPercent.setText("");
                }
            }

        };
        onProgressChanged();
        if (mMessage != null) {
            setMessage(mMessage);
        }
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }

        //设置监听
        tvCancel.setOnClickListener(this);

    }
    private void initFormats() {
        mProgressNumberFormat = "%1.2fM/%2.2fM";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }
    private void onProgressChanged() {
        mViewUpdateHandler.sendEmptyMessage(0);


    }
    public void setProgressStyle(int style) {
        //mProgressStyle = style;
    }
    public int getMax() {
        if (mProgress != null) {
            return mProgress.getMax();
        }
        return mMax;
    }
    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }
    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        }
        //      else {
        //            mIndeterminate = indeterminate;
        //        }
    }
    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }


    @Override
    public void setMessage(CharSequence message) {
        // TODO Auto-generated method stub
        //super.setMessage(message);
        if(mProgressMessage!=null){
            mProgressMessage.setText(message);
        }
        else{
            mMessage = message;
        }
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mHasStarted = true;
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mHasStarted = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel://取消
                if (mOnOkAndCancelListener!=null) {
                    mOnOkAndCancelListener.onCancel(v);
                }
                break;
            default:
                break;
        }
    }

    /**声明成员变量*/
    public OnOkAndCancelListener mOnOkAndCancelListener;
    /**暴露接口取消方法,如果需要确定,另行添加*/
    public interface OnOkAndCancelListener {
        void onCancel(View v);
    }
    /**暴露方法,设置监听*/
    public void setOnOkAndCancelListener(OnOkAndCancelListener mOnOkAndCancelListener) {
        this.mOnOkAndCancelListener = mOnOkAndCancelListener;
    }


    private String getMaxDouble(int value){
        String result ;
        float num= (float)value/1024;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        num = num/1024;
        result = df.format(num);//返回的是String类型
        return result;
    }

}