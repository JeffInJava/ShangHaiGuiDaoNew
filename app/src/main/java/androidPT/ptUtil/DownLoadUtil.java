package androidPT.ptUtil;

import java.io.File;


import android.content.Context;
import android.os.Handler;
import androidPT.ptUtil.internet.HttpResultCallBack;

public class DownLoadUtil {
	private Context mContext;
	private String sessionId;
	private String mIndexFilePath;
	private String path;
	private String folder;
	
	
	public DownLoadUtil(Context context , String sessionId ,String indexFilePath,String path , String folder) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.sessionId = sessionId;
		this.mIndexFilePath = indexFilePath;
		this.path = path;
		this.folder = folder;;
	}
	
	
	public void downLoadIndex(String indexUrl,String indexFolder
			,HttpResultCallBack cb,Handler handler){
		deleteIndex(indexFolder);
		
		DownLoadTask downLoadUtil = new DownLoadTask(
				mContext,
				mIndexFilePath, sessionId, 0 ,cb, handler);
		downLoadUtil.execute(indexUrl,path,folder);
		
	}

	public String getSessionId(){
		return sessionId;
	}
	
	
	private void deleteIndex(String filePath){
		File file = new File(filePath);
		if (file.isDirectory() && file.exists()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				files[i].delete();
			}
		}
	}
}
