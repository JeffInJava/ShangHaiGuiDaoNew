package androidPT.ptUtil.internet;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;

public interface FormInternetImpl {
	
	public void getAuth(Context context, String mac, HttpResultCallBack cb);
	/**
	 * 登陆
	 * @param context
	 * @param url
	 * @param params
	 * @param cb
	 */
	public void login(Context context, String userName, String password, HttpResultCallBack cb);
	
	/**
	 * 登出
	 * @param context
	 * @param url
	 * @param params
	 * @param cb
	 */
	public void logout(Context context, HttpResultCallBack cb);
	
	/**
	 * 
	 * @param context
	 * @param url
	 * @param cb
	 */
	public void getFileList(Context context, HttpResultCallBack cb);
	
	
	public void downLoadFile(Context context, String url, String sessionId, int position, HttpResultCallBack cb, Handler handler);
	
	public void getUserStudyData(Context context, String tblStudyDataId,String userId, HttpResultCallBack cb);
	
	public void createUserStudy(Context context, String tblStudyDataId, String studiedNum,String userId, HttpResultCallBack cb);
	
	public void uploadSignPic(Context context, String tblStudyDataId, List<String> filePath, List<String> users,String userId,String time,HttpResultCallBack cb);
	
	public void getIndexFileInfo(Context context, String url, HttpResultCallBack cb);
	
	public void getDepartments(Context context, int status, HttpResultCallBack cb);

	public void getPDFList(Context context, String departmentNo,String search_key,String type,String tabNo, HttpResultCallBack cb);

	public void getUserList(Context context,String departmentNo,HttpResultCallBack cb);

	public void UploadUserList(Context context, String tblStudyDataId,String userIds, HttpResultCallBack cb);

	public void getCheckedUserList(Context context, String tblStudyDataId, HttpResultCallBack cb);

	public void getTypeList(Context context,HttpResultCallBack cb);

	public void getAllPDFList(Context context,HttpResultCallBack cb);

	public void getAppVersion(Context context,String appType,String versionNo,HttpResultCallBack cb);
	
}
