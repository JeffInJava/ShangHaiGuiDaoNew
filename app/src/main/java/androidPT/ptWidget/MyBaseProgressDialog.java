package androidPT.ptWidget;





import com.ineasnet.shanghaiguidao2.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class MyBaseProgressDialog extends Dialog{

	public MyBaseProgressDialog(Context context, String strMessage) {
		this(context, R.style.CustomProgressDialog, strMessage);  
		
		// TODO Auto-generated constructor stub
	}
	public MyBaseProgressDialog(Context context, int theme, String strMessage) {  
        super(context, theme);  
        this.setContentView(R.layout.activity_progress);  
        this.getWindow().getAttributes().gravity = Gravity.CENTER;  
        TextView tvMsg = (TextView) this.findViewById(R.id.tv_update_name);  
        if (tvMsg != null) {  
            tvMsg.setText(strMessage);  
        }  
    }  
	 @Override  
	    public void onWindowFocusChanged(boolean hasFocus) {  
	  
	        if (!hasFocus) {  
	            dismiss();  
	        }  
	    }  
}
