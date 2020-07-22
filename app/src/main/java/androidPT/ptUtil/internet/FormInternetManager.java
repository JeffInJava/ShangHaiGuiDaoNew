package androidPT.ptUtil.internet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.content.Context;
import android.os.Handler;
import androidPT.ptUtil.DownLoadTask;
import androidPT.ptUtil.PropertiesUtil;

public class FormInternetManager implements FormInternetImpl {

	private static FormInternetManager mFormHttp;
	private String result;
	private String url;

	protected synchronized static FormInternetManager getInstance() {
		if (mFormHttp == null) {
			mFormHttp = new FormInternetManager();
		}
		return mFormHttp;
	}

	@Override
	public void login(Context context, String userName, String password,
			HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		
		url = PropertiesUtil.getPropertiesURL(context, "loginAction");
		
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j_username", userName));
		params.add(new BasicNameValuePair("j_password", password));

		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void logout(Context context, HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "logoutAction");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getFileList(Context context, HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "haveReaded");
		HttpInstance instance = HttpInstance.getHttpInstance();
		result = instance.getHttpPostResult(url, null);
		cb.onResultCallBack(result);
	}

	@Override
	public void downLoadFile(Context context,String url ,String sessionId ,int position ,HttpResultCallBack cb , Handler handler) {
		// TODO Auto-generated method stub
		String path = PropertiesUtil.getPropertiesURL(context, "filePath");
		DownLoadTask downLoadUtil = new DownLoadTask(context, path, sessionId,position,cb,handler);
		downLoadUtil.execute(url);
	}

	@Override
	public void getUserStudyData(Context context, String tblStudyDataId,String userId,
			HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "getUserStudyDataUrl");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tblStudyDataId", tblStudyDataId));
		params.add(new BasicNameValuePair("userId", userId));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void createUserStudy(Context context, String tblStudyDataId,
			String studiedNum,String userId, HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "createUserStudyUrl");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tblStudyDataId", tblStudyDataId));
		params.add(new BasicNameValuePair("studiedNum", studiedNum));
		params.add(new BasicNameValuePair("userId", userId));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void uploadSignPic(Context context, String tblStudyDataId,
			List<String> filePath,List<String> users ,String userId,String time,HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "uploadSignUrl");
		HttpInstance instance = HttpInstance.getHttpInstance();
		result = instance.uploadSign(url, filePath,users, tblStudyDataId,userId,time);
		cb.onResultCallBack(result);
	}

	@Override
	public void getIndexFileInfo(Context context, String url ,HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getAuth(Context context,String mac, HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "authUrl");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mac", mac));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getDepartments(Context context,int status, HttpResultCallBack cb) {
		// TODO Auto-generated method stub
		url = PropertiesUtil.getPropertiesURL(context, "departmentUrl");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("status", String.valueOf(status)));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getPDFList(Context context, String departmentNo, String search_key, String type, String tabNo, HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "selectPDF");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("departmentNo", departmentNo));
		params.add(new BasicNameValuePair("fileName", search_key));
		params.add(new BasicNameValuePair("tblCategoryId", type));
//		params.add(new BasicNameValuePair("tabNo", tabNo));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getUserList(Context context,String departmentNo ,HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "getCheckUser");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("departmentNo", departmentNo));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void UploadUserList(Context context, String tblStudyDataId, String userIds, HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "uploadCheckUser");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tblStudyDataId", tblStudyDataId));
		params.add(new BasicNameValuePair("userIds", userIds));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getCheckedUserList(Context context, String tblStudyDataId, HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "checkedUserList");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tblStudyDataId", tblStudyDataId));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getTypeList(Context context, HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "filesTreeUrlAdmin");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getAllPDFList(Context context, HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "allPDFList");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}

	@Override
	public void getAppVersion(Context context, String appType, String versionNo, HttpResultCallBack cb) {
		url = PropertiesUtil.getPropertiesURL(context, "getVersion");
		HttpInstance instance = HttpInstance.getHttpInstance();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appType", appType));
		params.add(new BasicNameValuePair("versionNo", versionNo));
		result = instance.getHttpPostResult(url, params);
		cb.onResultCallBack(result);
	}


}
