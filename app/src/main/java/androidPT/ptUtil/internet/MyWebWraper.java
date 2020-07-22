package androidPT.ptUtil.internet;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;

public class MyWebWraper {
	private static ExecutorService mThreadPool = null;

	private static FormInternetManager sFormInternetManager;

	private static MyWebWraper sMywebWraper;

	private final static int MAX_THREADS = 100;

	private MyWebWraper() {
	}

	public static MyWebWraper getInstance() {
		if (sMywebWraper == null) {
			sMywebWraper = new MyWebWraper();
		}
		sFormInternetManager = FormInternetManager.getInstance();
		return sMywebWraper;
	}

	private ExecutorService getThreadPool() {
		synchronized (MyWebWraper.class) {
			if (mThreadPool == null) {
				mThreadPool = Executors.newFixedThreadPool(MAX_THREADS);
			}
			return mThreadPool;
		}
	}

	public void login(final Context context, final String userName,
			final String password, final HttpResultCallBack cb) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.login(context, userName, password, cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void logout(final Context context, final HttpResultCallBack cb) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.logout(context, cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void getFileList(final Context context, final HttpResultCallBack cb) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getFileList(context, cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void downLoadFile(final Context context,final String url ,final String sessionId ,final int position ,final HttpResultCallBack cb,final Handler handler) {
		sFormInternetManager.downLoadFile(context,url, sessionId,position,cb,handler);
	}

	public void getUserStudyData(final Context context,
			final String tblStudyDataId, final String userId ,final HttpResultCallBack cb) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getUserStudyData(context, tblStudyDataId,userId,
						cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void createUserStudy(final Context context,
			final String tblStudyDataId, final String studiedNum,final String userId,
			final HttpResultCallBack cb) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.createUserStudy(context, tblStudyDataId,
						studiedNum,userId, cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void uploadSign(final Context context, final String tblStudyDataId,
						   final List<String> filePath,final List<String> users ,final String userId,final String time ,final HttpResultCallBack cb) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.uploadSignPic(context, tblStudyDataId,
						filePath,users ,userId,time,cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void getIndexFileInfo(final Context context,final String url ,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getIndexFileInfo(context, url ,cb);
			}
		};
		getThreadPool().execute(runnable);
	}
	
	public void getAuth(final Context context,final String mac ,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getAuth(context, mac ,cb);
			}
		};
		getThreadPool().execute(runnable);
	}
	public void getDepartments(final Context context,final int status ,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getDepartments(context,status, cb);
			}
		};
		getThreadPool().execute(runnable);
	}


	public void getPDFList(final Context context,final String departmentNo,final String search_key,final String type,final String tabNo,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getPDFList(context,departmentNo,search_key,type,tabNo,cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void getUserList(final Context context, final String departmentNo, final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getUserList(context,departmentNo,cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void uploadUserList(final Context context,final String tblStudyDataId,final String userIds,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.UploadUserList(context,tblStudyDataId,userIds,cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void getCheckedUserList(final Context context,final String tblStudyDataId,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getCheckedUserList(context,tblStudyDataId,cb);
			}
		};
		getThreadPool().execute(runnable);
	}


	public void getTypeList(final Context context,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getTypeList(context,cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void getAllPDFList(final Context context,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getAllPDFList(context,cb);
			}
		};
		getThreadPool().execute(runnable);
	}

	public void getAppVersion(final Context context,final String appType, final String versionNo,final HttpResultCallBack cb){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sFormInternetManager.getAppVersion(context,appType,versionNo,cb);
			}
		};
		getThreadPool().execute(runnable);
	}
}
