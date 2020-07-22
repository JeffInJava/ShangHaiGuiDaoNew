package androidPT.ptUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Properties;
import java.util.logging.LogManager;

import android.content.Context;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class PropertiesUtil {
	public static String getPropertiesURL(Context c, String s) {
		  String url = null;
		  Properties properties = new Properties();
		  try {
		   properties.load(c.getAssets().open("property.properties"));
		   url = properties.getProperty(s);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }

		  return url;

		 }

	/**
	 * 获取assets目录下的单个文件
	 *
	 * @param fileName
	 * @return
	 */
	public static File getFileFromAssetsFile(String fileName) {//这种方式不能用，只能用于webview加载，直接取路径是不行的
		String path = "file:///android_asset/" + fileName;
		File file = new File(path);
		return file;

	}


	/**
	 * 获取Https的证书
	 *
	 * @param context 上下文
	 * @return SSL的上下文对象
	 */
	public static SSLContext getSSLContext(Context context) {
		CertificateFactory certificateFactory = null;
		InputStream inputStream = null;
		Certificate cer = null;
		KeyStore keystore = null;
		TrustManagerFactory trustManagerFactory = null;
		SSLContext mSSLContext;
		try {
			certificateFactory = CertificateFactory.getInstance("X.509");
			inputStream = context.getAssets().open("tomcat.keystore");//这里导入SSL证书文件
			try {
				cer = certificateFactory.generateCertificate(inputStream);
			} finally {
				inputStream.close();
			}

			//创建一个证书库，并将证书导入证书库
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(null, null); //双向验证时使用
//			keystore.setCertificateEntry("trust", cer);

			// 实例化信任库
			trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keystore);

			mSSLContext = SSLContext.getInstance("TLS");
			mSSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

			//信任所有证书 （官方不推荐使用）
//         s_sSLContext.init(null, new TrustManager[]{new X509TrustManager() {
//
//              @Override
//              public X509Certificate[] getAcceptedIssuers() {
//                  return null;
//              }
//
//              @Override
//              public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                      throws CertificateException {
//
//              }
//
//              @Override
//              public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                      throws CertificateException {
//
//              }
//          }}, new SecureRandom());

			return mSSLContext;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
