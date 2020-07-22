package com.ineasnet.shanghaiguidao2.activity;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.R.id;
import com.ineasnet.shanghaiguidao2.R.layout;
import com.ineasnet.shanghaiguidao2.R.menu;
import com.ineasnet.shanghaiguidao2.fragments.IndexFragment;
import com.ineasnet.shanghaiguidao2.fragments.IndexNewFragment;
import com.ineasnet.shanghaiguidao2.fragments.PersonFragment;
import com.ineasnet.shanghaiguidao2.fragments.ReadStatusFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpInstance;

public class MainActivity extends MyBaseActivity {

	private LinearLayout btn_index, btn_person,btn_read;
	private ImageView iv_main,iv_my,iv_read;
	private TextView tv_main,tv_my,tv_read;
	private FragmentManager fm;
	private IndexNewFragment fragment_index;
	private PersonFragment fragment_person;
	private ReadStatusFragment fragment_read;

	private Fragment mContent;

	public static String userId, userName,departmentNo;
	public static boolean isFromPro;
	private boolean flag = true;
	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;
	private AlertDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		userId = getIntent().getStringExtra("userId");
		isFromPro = getIntent().getBooleanExtra("isFromPro", false);
		userName = getIntent().getStringExtra("userName");
		departmentNo = getIntent().getStringExtra("departmentNo");

	}

	@Override
	protected void handleHttpResult() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		fm = getFragmentManager();


		fragment_index = new IndexNewFragment();
		fragment_person = new PersonFragment();
		fragment_read = new ReadStatusFragment();

		btn_index = (LinearLayout) findViewById(R.id.btn_index);
		btn_person = (LinearLayout) findViewById(R.id.btn_person);
		btn_read = (LinearLayout) findViewById(id.btn_read);
		iv_main = (ImageView) findViewById(id.iv_main);
		iv_my = (ImageView) findViewById(id.iv_my);
		iv_read = (ImageView) findViewById(id.iv_read);
		tv_main = (TextView) findViewById(id.tv_main);
		tv_my = (TextView) findViewById(id.tv_my);
		tv_read = (TextView) findViewById(id.tv_read);


		btn_index.setOnClickListener(this);
		btn_person.setOnClickListener(this);
		btn_read.setOnClickListener(this);

		mContent = fragment_index;
		switchConent(fragment_index, fragment_index.getClass().getSimpleName());
		selectLayout(0);

	}

	public void switchConent(Fragment fragment, String tag) {
		FragmentTransaction transaction = fm.beginTransaction();
		if (mContent != fragment) {
			if (!fragment.isAdded()) { // 先判断是否被add过
				transaction.hide(mContent)
						.replace(R.id.content_frame, fragment, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mContent).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
			}
			mContent = fragment;
		} else {
			if (!fragment.isAdded()) { // 先判断是否被add过
				transaction.replace(R.id.content_frame, fragment, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.show(fragment).replace(R.id.content_frame, fragment, tag).commit(); // 隐藏当前的fragment，显示下一个
			}
			mContent = fragment;
		}

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void switchToIndex() {
		switchConent(fragment_index, fragment_index.getClass().getSimpleName());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_index:
			if(null == fragment_index){
				fragment_index = new IndexNewFragment();
			}
			switchConent(fragment_index, fragment_index.getClass()
					.getSimpleName());
			selectLayout(0);
			fragment_index.setBack();
			mContent = fragment_index;
			break;
		case R.id.btn_person:
			switchConent(fragment_person, fragment_person.getClass()
					.getSimpleName());
			selectLayout(1);
			mContent = fragment_person;
			break;
			case id.btn_read:
				switchConent(fragment_read, fragment_read.getClass()
						.getSimpleName());
				selectLayout(2);
				mContent = fragment_read;
				break;
		default:
			break;
		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (flag){
					if (fragment_index.isBack()){
						initMessageDialog("是否退出程序");
					}else{
						fragment_index.getLastList();
					}
				}else{
					initMessageDialog("是否退出程序");
				}
				break;
		}
		return false;
	}

	private void selectLayout(int state){
		switch (state){
			//选中首页
			case 0:
				flag = true;
				iv_main.setImageResource(R.drawable.main_checked);
				iv_my.setImageResource(R.drawable.my_unckecked);
				iv_read.setImageResource(R.drawable.read_uncheck);
				tv_main.setTextColor(getResources().getColor(R.color.main_text_color));
				tv_my.setTextColor(getResources().getColor(R.color.main_text_unckeck_color));
				tv_read.setTextColor(getResources().getColor(R.color.main_text_unckeck_color));
				break;
			//选中我的信息
			case 1:
				flag = false;
				iv_main.setImageResource(R.drawable.main_unckecked);
				iv_my.setImageResource(R.drawable.my_checked);
				iv_read.setImageResource(R.drawable.read_uncheck);
				tv_main.setTextColor(getResources().getColor(R.color.main_text_unckeck_color));
				tv_my.setTextColor(getResources().getColor(R.color.main_text_color));
				tv_read.setTextColor(getResources().getColor(R.color.main_text_unckeck_color));
				break;
			//选中待阅
			case 2:
				flag = false;
				iv_main.setImageResource(R.drawable.main_unckecked);
				iv_my.setImageResource(R.drawable.my_unckecked);
				iv_read.setImageResource(R.drawable.read_check);
				tv_main.setTextColor(getResources().getColor(R.color.main_text_unckeck_color));
				tv_my.setTextColor(getResources().getColor(R.color.main_text_unckeck_color));
				tv_read.setTextColor(getResources().getColor(R.color.main_text_color));
				break;
		}
	}



	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
		} else {

			System.exit(0);
		}
	}


	/**
	 * 文件下载弹框
	 */
	private void initMessageDialog(String title){
		if (dialog == null){
			// 创建构建器
			AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// 设置参数
			builder.setTitle(title);
			builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					System.exit(0);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog = builder.create();
		}
		dialog.show();
	}

}
