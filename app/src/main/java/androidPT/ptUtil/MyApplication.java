package androidPT.ptUtil;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.FileUtil;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

import okhttp3.OkHttpClient;

public class MyApplication extends MultiDexApplication {
	private String userId;
	private boolean isAuthed = false;
	public DbManager.DaoConfig daoConfig;
	private static MyApplication instance;

	public boolean isAuthed() {
		return isAuthed;
	}

	public void setAuthed(boolean isAuthed) {
		this.isAuthed = isAuthed;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Logger.init("SHGD");
		x.Ext.init(this);
		x.Ext.setDebug(false); //输出debug日志，开启会影响性能
		String folder = FileName.FILEPATH+"indexAll/";
		FileUtil.createParentFile(folder);
		String folder_pro = FileName.FILEPATH+"ftpIndexAll/";
		FileUtil.createParentFile(folder_pro);
		String path = FileName.FILEPATH+"indexAll.zip";
		FileUtil.createNewFile(path);
		setDB();
		Stetho.initializeWithDefaults(this);
		new OkHttpClient.Builder()
				.addNetworkInterceptor(new StethoInterceptor())
				.build();
		instance = this;
	}


	private void setDB(){
		if(daoConfig==null){
			daoConfig=new DbManager.DaoConfig()
					.setDbName("shanghaiguidaonew.db")
					.setDbVersion(2)
					.setAllowTransaction(true)
					.setDbOpenListener(new DbManager.DbOpenListener() {
						@Override
						public void onDbOpened(DbManager db) {
							// 开启WAL, 对写入加速提升巨大
							db.getDatabase().enableWriteAheadLogging();
						}
					})
					.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
						@Override
						public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
						}
					});
		}
	}


	public DbManager.DaoConfig getDaoConfig(){
		return daoConfig;
	}

	public static MyApplication getInstance(){
		return instance;
	}

}
