package androidPT.ptUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import androidPT.ptUtil.PropertiesUtil;

public class ZipTool {
	private Context mContext;
	private String mResult;
	private SharedPreferences sp;
	private String mUserId;
	private String tblStudyDataId;
	private String key_user_studydata;
	public ZipTool(Context context , String result) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mResult = result;
	}
	
	
	/**
	 * @param from
	 *            文件所在目录
	 * @param to
	 *            解压缩后目录
	 */
	public void execute(String from, String to) throws ZipException,
			IOException {
		int index = from.lastIndexOf("/");
		String absFileName= from.substring(index);
		String baseDir = from.substring(0, index);
		File zipFile = getRealFileName(baseDir, absFileName);
		upZipFile(zipFile, to);

	}
	
	
	
	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	private int upZipFile(File zipFile, String folderPath) throws ZipException,
			IOException {
//		sp = mContext.getSharedPreferences(mUserId, Activity.MODE_PRIVATE);
		
//		ZipBean bean = JSON.parseObject(mResult,ZipBean.class);
//		List<ZipVo> zipVos = bean.getAttachList();
		
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> zList = zfile.entries();
		ZipEntry ze = null;
		File fileDir = new File(folderPath);
		if(!fileDir.isDirectory()){
			fileDir.mkdir();
		}
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				Log.d("upZipFile", "ze.getName() = " + ze.getName());
				String dirstr = folderPath + ze.getName();
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes(), "UTF-8");
				Log.d("upZipFile", "str = " + dirstr);
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			//获取pdf名称，拼接成绝对路径
//			String pdfurl = bean.getPdfUrl();
//			int index = pdfurl.lastIndexOf("/");
//			String pdfName = pdfurl.substring(index+1);
//			String pdfPath =PropertiesUtil.getPropertiesURL(mContext, "downloadPath")+ pdfName;
			
			
			
			//向preference中写入文件的绝对路径
//			for(ZipVo vo : zipVos){
//				if(vo.getAttachUrl().equalsIgnoreCase(ze.getName())){
//					String seq = vo.getSeqNum();
//					vo.setAttachUrlShow("file:///"+folderPath+ze.getName());
//					vo.setPdfUrl(pdfPath);
//					tblStudyDataId = vo.getTblStudyDataId();
//				}
//			}
			Log.e("upZipFile", "ze.getName() = " + ze.getName());
			FileOutputStream os = new FileOutputStream(folderPath+ze.getName());
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		//向preference写入修改后的json
//		String resultStr_final = JSON.toJSONString(bean.getAttachList());
//		Log.e("resultStr_final", "resultStr_final:"+resultStr_final);
		
		JSONObject obj_parame_key = new JSONObject();
		try {
			obj_parame_key.put("tblStudyDataId", tblStudyDataId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		zfile.close();
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	
	 public static File getRealFileName(String baseDir, String absFileName){
	        String[] dirs=absFileName.split("/");
	        File ret=new File(baseDir);
	        String substr = null;
	        if(dirs.length>1){
	            for (int i = 0; i < dirs.length-1;i++) {
	                substr = dirs[i];
	                try {
	                    //substr.trim();
	                    substr = new String(substr.getBytes("8859_1"), "GB2312");
	                    
	                } catch (UnsupportedEncodingException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	                ret=new File(ret, substr);
	                
	            }
	            Log.d("upZipFile", "1ret = "+ret);
	            if(!ret.exists())
	                ret.mkdirs();
	            substr = dirs[dirs.length-1];
	            try {
	                //substr.trim();
	                substr = new String(substr.getBytes("8859_1"), "GB2312");
	                Log.d("upZipFile", "substr = "+substr);
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	            ret=new File(ret, substr);
	            Log.d("upZipFile", "2ret = "+ret);
	            return ret;
	        }
	        return ret;
	    }


}
