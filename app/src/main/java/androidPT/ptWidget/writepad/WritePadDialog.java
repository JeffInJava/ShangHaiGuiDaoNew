package androidPT.ptWidget.writepad;


import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.vo.UserBitmap;
import com.ineasnet.shanghaiguidao2.fragments.BitmapFragment;
import com.ineasnet.shanghaiguidao2.view.NoScrollViewPager;
import com.orhanobut.logger.Logger;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class WritePadDialog extends Dialog{
	private Context mContext;  
    private WriteDialogListener mWriteDialogListener;  
    private PaintView mPaintView;
    private FrameLayout mFrameLayout;
    private Button mBtnOK, mBtnClear,mBtnNext, mBtnCancel;
//    private TextView tv_user;
    private String name;
    private List<String> nameList = new ArrayList<String>();
    private List<UserBitmap> userBitmapList = new ArrayList<UserBitmap>();
    private TabLayout tablet_layout;
    private int userPosition = 0;
    private List<View> paintViews = new ArrayList<View>();
    private Map<Integer,PaintView> mapView=new HashMap<>();
    private int screenWidth;
    private int screenHeight;


    public WritePadDialog(Context context,List<UserBitmap> name,
            WriteDialogListener writeDialogListener) {
        super(context);
        this.userBitmapList = name;
        this.mContext = context;  
        this.mWriteDialogListener = writeDialogListener;
        for (int i=0;i<userBitmapList.size();i++){
            nameList.add(userBitmapList.get(i).getName());
        }
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题  
        setContentView(R.layout.write_pad);  
  
        mFrameLayout = (FrameLayout) findViewById(R.id.tablet_view);
        tablet_layout = (TabLayout) findViewById(R.id.tablet_layout);
//        for (int i=0;i<userBitmapList.size();i++){
//            if (i==0){
//                TabLayout.Tab tab1 = tablet_layout.newTab().setText(userBitmapList.get(i).getName());
//                tablet_layout.addTab(tab1,i,true);
//            }else{
//                TabLayout.Tab tab1 = tablet_layout.newTab().setText(userBitmapList.get(i).getName());
//                tablet_layout.addTab(tab1);
//            }
//
//        }
        for (int i=0;i<userBitmapList.size();i++){
            TabLayout.Tab tab1 = tablet_layout.newTab().setText(userBitmapList.get(i).getName());
            tablet_layout.addTab(tab1);
        }
        tablet_layout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        // 获取屏幕尺寸
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;
        mPaintView = new PaintView(mContext, screenWidth, screenHeight);
        paintViews.add(mPaintView);
        mFrameLayout.addView(mPaintView);
        mapView.put(userPosition,mPaintView);
//        mFrameLayout.addView(mPaintView);

        mBtnOK = (Button) findViewById(R.id.write_pad_ok);  
        mBtnOK.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {
                //下一个tab选中
                int checkedPosition=tablet_layout.getSelectedTabPosition();
                if(checkedPosition!=userBitmapList.size()-1){
                    tablet_layout.getTabAt(checkedPosition+1).select();
                }
            }
        });  
  
        mBtnClear = (Button) findViewById(R.id.write_pad_clear);  

  
        mBtnCancel = (Button) findViewById(R.id.write_pad_cancel);  
        mBtnCancel.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                cancel();  
            }  
        });  
        
        mBtnNext = (Button) findViewById(R.id.write_pad_next);
//        tv_user = (TextView) findViewById(R.id.tv_user);
//        tv_user.setText(name);
        mBtnNext.setOnClickListener(new View.OnClickListener() {  
        	  
            @Override  
            public void onClick(View v) {
                //下一个tab选中
                int checkedPosition=tablet_layout.getSelectedTabPosition();
                if(checkedPosition!=userBitmapList.size()-1){
                    tablet_layout.getTabAt(checkedPosition+1).select();
                }else{
//                    UserBitmap lastUserBitmap = userBitmapList.get(checkedPosition);
//                    lastUserBitmap.setBitmap(mPaintView.getPaintBitmap());
//                    userBitmapList.set(checkedPosition,lastUserBitmap);

                    mWriteDialogListener.onNextPaint(mapView);
                    mPaintView.clear();
                }
            }
        });

//        // 设置TabLayout模式
//        tablet_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 设置Tab的选择监听
        tablet_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                userPosition = position;
                setButtonText();
//                mPaintView.clear();
//                Bitmap bitmap = userBitmapList.get(position).getBitmap();
//                if (bitmap != null){
//                    mPaintView.setBitmap(bitmap,screenWidth,screenHeight);
//                }
                loadCurrentView();
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                UserBitmap lastUserBitmap = userBitmapList.get(tab.getPosition());
                if (mPaintView.getPaintBitmap()!=null){
//                    lastUserBitmap.setBitmap(mPaintView.getPaintBitmap());
//                    userBitmapList.set(userPosition,lastUserBitmap);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Logger.d("Tab:",String.valueOf(tab.getPosition()));
            }
        });

        mBtnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Iterator<Integer> iterator = mapView.keySet().iterator();
                while (iterator.hasNext()) {
                    int key = iterator.next();
                    if (key==userPosition) {
                        iterator.remove();
                    }
                }
                mPaintView.clear();
            }
        });


    }

    private void setButtonText(){
        if (userPosition!=userBitmapList.size()-1){
            mBtnNext.setText("继续签名");
        }else{
            mBtnNext.setText("完成");
        }
    }


    /**
     * 根据位置获取绘画view，判断是否存在，
     * 并添加到当前父容器中
     */
    private void loadCurrentView(){
        mPaintView=mapView.get(userPosition);
        if(null==mPaintView){
            mPaintView= new PaintView(mContext, screenWidth, screenHeight);
            mapView.put(userPosition,mPaintView);
        }

        //清除之前，添加最新的
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mPaintView);
    }


}
