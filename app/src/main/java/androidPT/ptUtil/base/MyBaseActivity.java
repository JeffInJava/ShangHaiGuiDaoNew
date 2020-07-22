package androidPT.ptUtil.base;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.activity.ReadStatusActivity;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.x;

import androidPT.ptUtil.MyApplication;
import androidPT.ptWidget.MyBaseProgressDialog;

public abstract class MyBaseActivity extends FragmentActivity implements OnClickListener{
	public TextView tv_head;
	public String str_head;
	
	public final static int LOADING = 0 ;
	public final static int LOADING_END = 1 ;
	public final static int SHOWTOAST = 2 ;
	
	public MyBaseProgressDialog dialog;
	public MyApplication application;
	public DbManager db;



	public Gson gson;
	public AlertDialog errorDialog = null;
	
	public Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADING:
				showLoading();
				break;
			case LOADING_END:
				dismissLoading();
				break;
			case SHOWTOAST:
				Activity activity = (Activity) msg.obj;
				initErrorDialog(activity,"服务器异常，请稍后重试");
				break;
			default:
				break;
			}
			
		};
	};
	public interface MyOnTouchListener {
		public boolean onTouch(MotionEvent ev);
	}

	public ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
			10);

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.add(myOnTouchListener);
	}

	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.remove(myOnTouchListener);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		application = (MyApplication) getApplication();
		db = x.getDb(application.getDaoConfig());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
	}
	
	public static void showToast(Context context ,String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
	public boolean handleException(Activity context,String result){
		
		if(TextUtils.isEmpty(result)){
			Message msg = new Message();
			msg.what = SHOWTOAST;
			msg.obj = context;
			handler.sendMessage(msg);
			return false;
		}else{
			if(result.contains("exceptionCode")){
				
				
				
//				try {
//					JSONObject jsonObject = JSON.parseObject(result);
//					String exceptionCode = jsonObject.getString("exceptionCode");
//					
//					switch (exceptionCode) {
//					case "99":
//						
//						break;
//					case "80":
////						Intent intent = new Intent(context,MainActivity.class);
////						context.startActivity(intent);
////						context.finish();
//						
//						break;
//						
//					default:
//						break;
//					}
//					
//				} catch (Exception e) {
//					// TODO: handle exception
//					Message msg = new Message();
//					msg.what = SHOWTOAST;
//					msg.obj = context;
//					handler.sendMessage(msg);
//				}
				return false;
			}
			else{
				return true;
			}
			
		}
		
		
	}
	
	protected abstract void handleHttpResult();
	
	private void showLoading() {

		dialog = new MyBaseProgressDialog(this,getResources().getString(R.string.loading));
		dialog.show();

	}

	// 关闭loading
	private void dismissLoading() {

		if (null != dialog) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
	
	public boolean isOpenNetwork() {  
	    ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(connManager.getActiveNetworkInfo() != null) {  
	        return connManager.getActiveNetworkInfo().isAvailable();  
	    }  
	  
	    return false;  
	}  
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		for (MyOnTouchListener listener : onTouchListeners) {
//			listener.onTouch(ev);
//		}
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	protected abstract void initView();


	/**
	 * 文件下载弹框
	 */
	public void initErrorDialog(Activity activity,String title){

		if (errorDialog == null){
			// 创建构建器
			AlertDialog.Builder builder = new AlertDialog.Builder(activity,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// 设置参数
			builder.setTitle(title);
			final AlertDialog finalErrorDialog = errorDialog;
			builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					doNext();
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					doDismiss();
				}
			});
			errorDialog = builder.create();
		}
		errorDialog.show();
	}


	public void doNext(){
		if (dialog!=null){
			dialog.dismiss();
		}

	}
	public void doDismiss(){
		dialog.dismiss();
	}
}
