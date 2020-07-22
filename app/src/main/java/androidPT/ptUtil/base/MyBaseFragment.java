package androidPT.ptUtil.base;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.activity.MainActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.x;

import androidPT.ptUtil.MyApplication;
import androidPT.ptWidget.MyBaseProgressDialog;

public abstract class MyBaseFragment extends Fragment {

	MyBaseActivity.MyOnTouchListener myOnTouchListener;
	private MyApplication application;
	private DbManager db;
	protected MainActivity mActivity;
	private static final int SUCCESS = 0;
	private static final int FAILURE = 1;
	public final static int SHOWTOAST = 2 ;
	public static final int LOADING = 9;
	public static final int LOADING_END = 10;
	public static final int NOTICE = 11;
	public static final int CLOSE = 12;
	protected JSONObject jsObject = null;
	public Object object;
	private boolean isUpdateing = false;
	private MyBaseProgressDialog dialog;

	protected boolean isVisible;
	private String dialogStr;
	
	
	public TextView tv_title;
	public String str_title;
	public Gson gson;

//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//
//		if (getUserVisibleHint()) {
//			isVisible = true;
//			onVisible();
//		} else {
//			isVisible = false;
//			onInvisible();
//		}
//	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				// 刷新数据
//				System.out.println("success");
//				mhandler.sendEmptyMessage(LOADING_END);
//				try {
//					showData();
//				} catch (Exception ex) {
//					showError(ex.getLocalizedMessage());
//				}
				break;
			case LOADING:
				// loading
				showLoading();
				break;
			case LOADING_END:
				// loading_end
				dismissLoading();
				break;
			case FAILURE:
				// 报错
				handler.sendEmptyMessage(LOADING_END);
				break;
			case NOTICE:
//				notice();
				dismissLoading();
				break;
			case CLOSE:
//				close();
//				Intent intent = new Intent("com.inseanet.lexingshanghai.Main");
//		        intent.putExtra("notification", false);
//		        getActivity().sendBroadcast(intent);
				dismissLoading();
				break;
			default:
				break;
			}
			isUpdateing = false;
		}
	};
//	protected Runnable runnable = new Runnable() {
//		public void run() {
//			if (isUpdateing)
//				return;
//			isUpdateing = true;
//			boolean b = false;
//			Message msg = new Message();
//			Bundle data = new Bundle();
//			mhandler.sendEmptyMessage(LOADING);
//			try {
//
//				b = getDatas(object);
//			} catch (Exception ex) {
//				String errorInfo = "";
//				if (ex instanceof HttpHostConnectException) {
//					errorInfo = "抱歉，由于网络连接问题，无法为您更新数据，请检查您的网络链接或稍后再试";
//				} else if (ex instanceof ConnectTimeoutException) {
//					errorInfo = "网络连接超时，请检查您的网络链接或稍后再试";
//				} else {
//					errorInfo = ex.getLocalizedMessage();
//				}
//				data.putString("errorinfo", errorInfo);
//				msg.setData(data);
//			}
//			if (b)
//				msg.what = SUCCESS;
//			else
//				msg.what = FAILURE;
//			mhandler.sendMessage(msg);
//		}
//	};
	
	public boolean isOpenNetwork() {  
	    ConnectivityManager connManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(connManager.getActiveNetworkInfo() != null) {  
	        return connManager.getActiveNetworkInfo().isAvailable();  
	    }  
	  
	    return false;  
	}  

	protected boolean showError(String strErrorInfo) {
		if (strErrorInfo == null || strErrorInfo.equals(""))
			return false;
		Toast.makeText(getActivity(), strErrorInfo, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		dialogStr = getActivity().getResources().getString(R.string.loading);

		application = (MyApplication) getActivity().getApplication();
		db = x.getDb(application.getDaoConfig());
		myOnTouchListener = new MyBaseActivity.MyOnTouchListener() {

			@Override
			public boolean onTouch(MotionEvent ev) {
				// TODO Auto-generated method stub
				if (ev.getAction() == MotionEvent.ACTION_DOWN) {

					// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
					View v = mActivity.getCurrentFocus();

					if (isShouldHideInput(v, ev)) {
						hideSoftInput(v.getWindowToken());
					}
					return false;
				} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;

			}

		};
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	protected abstract void init();
	
public boolean handleException(Activity context,String result){
		
		if(TextUtils.isEmpty(result)){
			Message msg = new Message();
			msg.what = SHOWTOAST;
			msg.obj = context;
			handler.sendMessage(msg);
			return false;
		}else{
			if(result.contains("exception")){
				
				
				
				try {
					JSONObject jsonObject = JSON.parseObject(result);
					String exceptionCode = jsonObject.getString("exceptionCode");
					
					switch (exceptionCode) {
					case "99":
						
						break;
					case "80":
						Intent intent = new Intent(context,MainActivity.class);
						context.startActivity(intent);
						context.finish();
						
						break;
						
					default:
						break;
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					Message msg = new Message();
					msg.what = SHOWTOAST;
					msg.obj = context;
					handler.sendMessage(msg);
				}
				return false;
			}
			else{
				return true;
			}
			
		}
		
		
	}
	
	protected abstract void handleHttpResult();

//	protected abstract boolean getDatas(Object object) throws Exception;
//
//	protected abstract void showData() throws Exception;
//
//	protected void notice() {
//	};
//	protected void close(){};

	public void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	private void showLoading() {
		dialog = new MyBaseProgressDialog(getActivity(),dialogStr );
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

//	protected void onVisible() {
//		lazyLoad();
//	}
//
//	/**
//	 * 不可见
//	 */
//	protected void onInvisible() {
//
//	}
//
//	/**
//	 * 延迟加载 子类必须重写此方法
//	 */
//	protected abstract void lazyLoad();
}
