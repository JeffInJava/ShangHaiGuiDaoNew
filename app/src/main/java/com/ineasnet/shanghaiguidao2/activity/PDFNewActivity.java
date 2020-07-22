package com.ineasnet.shanghaiguidao2.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.PDFListListAdapter;
import com.ineasnet.shanghaiguidao2.adapters.UserAdapter;
import com.ineasnet.shanghaiguidao2.dao.FileDao;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.bean.UserListCheckedBean;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserBitmap;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.QueryUtil;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.ineasnet.shanghaiguidao2.util.WaterMarkTextUtil;
import com.ineasnet.shanghaiguidao2.view.UserPopupWindow;
import com.shockwave.pdfium.PdfDocument;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;
import androidPT.ptUtil.storage.StorageUtil;
import androidPT.ptWidget.writepad.PaintView;
import androidPT.ptWidget.writepad.WriteDialogListener;
import androidPT.ptWidget.writepad.WritePadDialog;

/**
 * Created by Administrator on 2017/7/2.
 */

public class PDFNewActivity extends MyBaseActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = PDFNewActivity.class.getSimpleName();

    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;

    public static final String SAMPLE_FILE = "sample.pdf";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    PDFView pdfView;

    Uri uri;

    Integer pageNumber = 0;

    String pdfFileName;
    private String title;
    private Context mContext;
    private String userId;
    private String result;
    private String url, key;
    private String path;
    private String userName;
    private StorageUtil storageUtil;
    private String filePath, tblStudyDataId;

    private UserAdapter adapter;
    private List<User> list = new ArrayList<User>();
    private UserPopupWindow popupWindow;
    private User user;
    private WritePadDialog mWritePadDialog;
    private FileDao fileDao;
    private List<FileDao> fileDaoList = new ArrayList<FileDao>();
    private String fileName;
    private int buttonState = 0;
    private List<String> nameList = new ArrayList<String>();
    private List<UserBitmap> userBitmapList = new ArrayList<UserBitmap>();
    private List<UserBitmap> userBitmapShow = new ArrayList<UserBitmap>();
    private List<UserBitmap> uploadBitmapShow = new ArrayList<UserBitmap>();
    private List<String> bitmapPathList = new ArrayList<String>();
    private TextView tv_pdfview_name;
    private int count = 0;
    private Map<Integer,PaintView> map = new HashMap<>();
    private String time;
    private WaterMarkTextUtil waterMarkTextUtil;
    private TextView tv_pdfview_id;
    private int i=0;
    private boolean isFromIndex;
    private AlertDialog dialog;
    private int pageCount = 0;
    private String timeNow;
    private DbManager.DaoConfig config;
    private MyApplication application;
    private DbManager db;
    //工号
    private TextView tv_pdfnew_id;
    //姓名
    private TextView tv_pdfnew_name;
    //附件
    private Button bt_pdf_new;
    private Button bt_pdf_parent;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String status = String.valueOf(msg.obj);
                    if (status.equals("1")) {
                        showWritePadDialog();
                    } else if (status.equals("2")) {
                        showWritePadDialog();
                    }
                    break;
                case 2:
                    Toast.makeText(PDFNewActivity.this, "您已完成学习", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 3:
                    break;
                default:
                    break;
            }

        }

        ;
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pdf);
        application = (MyApplication) getApplication();
        config = application.getDaoConfig();
        db = x.getDb(config);
        afterViews();
        initView();
        initUser();
        displayFromUri(Uri.parse(filePath));
    }

    @Override
    protected void handleHttpResult() {

    }

    @Override
    protected void initView() {
        gson = new Gson();
        mContext = PDFNewActivity.this;

        filePath = "file://"+getIntent().getStringExtra("filePath");
        userName = SettingUtils.get(PDFNewActivity.this,"userName");
        tblStudyDataId = String.valueOf(getIntent().getIntExtra("tblStudyDataId",0));
        userId = SettingUtils.get(mContext,"userId");
        fileName = getIntent().getStringExtra("title");

        isFromIndex = getIntent().getBooleanExtra("isFromIndex", false);
        path = PropertiesUtil.getPropertiesURL(this, "filePath") + userId + File.separator + tblStudyDataId + File.separator;
        storageUtil = new StorageUtil(PDFNewActivity.this);
        tv_head = (TextView) findViewById(R.id.tv_title_content);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        tv_pdfnew_id = (TextView) findViewById(R.id.tv_pdfnew_id);
        tv_pdfnew_name = (TextView) findViewById(R.id.tv_pdfnew_name);
        bt_pdf_parent = (Button) findViewById(R.id.bt_pdf_parent);
        bt_pdf_new = (Button) findViewById(R.id.bt_pdf_new);
        bt_pdf_new.setOnClickListener(this);
        bt_pdf_parent.setOnClickListener(this);
        String account = SettingUtils.get(mContext,"Account");
        tv_pdfnew_id.setText("工号:"+account);
        tv_pdfnew_name.setText("姓名:"+userName);
        pdfView.setBackgroundColor(Color.LTGRAY);
        tv_head.setText("文章阅读");
        Calendar c = Calendar.getInstance();
        //取得系统日期:
        String Month,Day,Hour,Minute,Second;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        if (month < 10) {
            Month = "0"+String.valueOf(month);
        }else{
            Month = String.valueOf(month);
        }
        if (day < 10) {
            Day = "0"+String.valueOf(day);
        }else{
            Day = String.valueOf(day);
        }

        if (hour < 10) {
            Hour = "0"+String.valueOf(hour);
        }else{
            Hour = String.valueOf(hour);
        }

        if (minute < 10) {
            Minute = "0"+String.valueOf(minute);
        }else{
            Minute = String.valueOf(minute);
        }
        if (second < 10) {
            Second = "0"+String.valueOf(second);
        }else{
            Second = String.valueOf(second);
        }

        time = String.valueOf(year)+Month+Day;
        timeNow = String.valueOf(year)+"-"+Month+"-"+Day+"m"+Hour+":"+Minute+":"+Second;
        TreeVo vo = QueryUtil.QueryByID(mContext,db,tblStudyDataId);
        if (!vo.getParentId().equals("null")){
            bt_pdf_parent.setVisibility(View.VISIBLE);
        }else{
            bt_pdf_parent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_pdf_new:
                List<TreeVo> secondList = QueryUtil.QueryByPDFList(mContext,db,tblStudyDataId);
                if (secondList.size()>1){
                    setPDFList(secondList);
                }else{
                    TreeVo vo = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
                    UserListCheckedBean userListCheckedBean = (UserListCheckedBean) getIntent().getSerializableExtra("users");
                    String pdfName = vo.getPdfName();
                    String path = "file://"+FileName.FILEPATH + "ftpIndexAll/" + vo.getPdfName();
//                    Intent intent = new Intent(PDFNewActivity.this,PDFNewActivity.class);
//                    intent.putExtra("users",userListCheckedBean);
//                    intent.putExtra("filePath", path);
//                    intent.putExtra("tblStudyDataId", vo.getId());
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("isFromIndex", isFromIndex);
//                    intent.putExtra("page", vo.getPageCount());
//                    intent.putExtra("departmentNo",MainActivity.departmentNo);
//                    intent.putExtra("fileName",pdfName);
//                    intent.putExtra("title",vo.getName());
//                    startActivity(intent);
//                    finish();
                    tblStudyDataId = String.valueOf(vo.getId());
                    fileName = pdfName;
                    title = vo.getName();
                    filePath = path;
                    TreeVo voNew = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
                    if (voNew !=null){
                        bt_pdf_new.setVisibility(View.VISIBLE);
                    }else{
                        bt_pdf_new.setVisibility(View.GONE);
                    }
                    if (vo.getParentId().equals("null")){
                        bt_pdf_parent.setVisibility(View.GONE);
                    }else{
                        bt_pdf_parent.setVisibility(View.VISIBLE);
                    }

                    displayFromUri(Uri.parse(filePath));
                }
                break;
            case R.id.bt_pdf_parent:
                TreeVo voSecond = QueryUtil.QueryByID(mContext,db,tblStudyDataId);
                TreeVo vo = QueryUtil.QueryByID(mContext,db,voSecond.getParentId());
                UserListCheckedBean userListCheckedBean = (UserListCheckedBean) getIntent().getSerializableExtra("users");
                String pdfName = vo.getPdfName();
                String path = "file://"+FileName.FILEPATH + "ftpIndexAll/" + vo.getPdfName();
//                Intent intent = new Intent(PDFNewActivity.this,PDFNewActivity.class);
//                intent.putExtra("users",userListCheckedBean);
//                intent.putExtra("filePath", path);
//                intent.putExtra("tblStudyDataId", vo.getId());
//                intent.putExtra("userId", userId);
//                intent.putExtra("isFromIndex", isFromIndex);
//                intent.putExtra("page", vo.getPageCount());
//                intent.putExtra("departmentNo",MainActivity.departmentNo);
//                intent.putExtra("fileName",pdfName);
//                intent.putExtra("title",vo.getName());
//                startActivity(intent);
//                finish();

                tblStudyDataId = String.valueOf(vo.getId());
                fileName = pdfName;
                title = vo.getName();
                filePath = path;
//                TreeVo voNew = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
//                TreeVo secondVo = QueryUtil.QueryByID(mContext,db,tblStudyDataId);
                if (vo !=null){
                    bt_pdf_new.setVisibility(View.VISIBLE);
                }else{
                    bt_pdf_new.setVisibility(View.GONE);
                }
//                if (secondVo.getParentId().equals("null")){
                    bt_pdf_parent.setVisibility(View.GONE);
//                }else{
//                    bt_pdf_parent.setVisibility(View.VISIBLE);
//                }
                displayFromUri(Uri.parse(filePath));
                break;
        }
    }


    private void setPDFList(final List<TreeVo> list){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pdflist,null);
        ListView lv = (ListView) view.findViewById(R.id.lv_pdflist);
        PDFListListAdapter adapter = new PDFListListAdapter(mContext,list);
        lv.setAdapter(adapter);
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景透明 ，对于AlertDialog 就不管用了
        dialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                TreeVo vo = list.get(position);
                UserListCheckedBean userListCheckedBean = (UserListCheckedBean) getIntent().getSerializableExtra("users");
                String pdfName = vo.getPdfName();
//                String path = FileName.FILEPATH + "ftpIndexAll/" + vo.getPdfName();
//                Intent intent = new Intent(PDFNewActivity.this,PDFNewActivity.class);
//                intent.putExtra("users",userListCheckedBean);
//                intent.putExtra("filePath", path);
//                intent.putExtra("tblStudyDataId", vo.getId());
//                intent.putExtra("userId", userId);
//                intent.putExtra("isFromIndex", isFromIndex);
//                intent.putExtra("page", vo.getPageCount());
//                intent.putExtra("departmentNo",MainActivity.departmentNo);
//                intent.putExtra("fileName",pdfName);
//                intent.putExtra("title",vo.getName());
//                startActivity(intent);
//                finish();
                String path = "file://"+FileName.FILEPATH + "ftpIndexAll/" + vo.getPdfName();
//                    Intent intent = new Intent(PDFNewActivity.this,PDFNewActivity.class);
//                    intent.putExtra("users",userListCheckedBean);
//                    intent.putExtra("filePath", path);
//                    intent.putExtra("tblStudyDataId", vo.getId());
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("isFromIndex", isFromIndex);
//                    intent.putExtra("page", vo.getPageCount());
//                    intent.putExtra("departmentNo",MainActivity.departmentNo);
//                    intent.putExtra("fileName",pdfName);
//                    intent.putExtra("title",vo.getName());
//                    startActivity(intent);
//                    finish();
                tblStudyDataId = String.valueOf(vo.getId());
                fileName = pdfName;
                title = vo.getName();
                filePath = path;
                TreeVo voNew = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
                if (voNew !=null){
                    bt_pdf_new.setVisibility(View.VISIBLE);
                }else{
                    bt_pdf_new.setVisibility(View.GONE);
                }
                if (vo.getParentId().equals("null")){
                    bt_pdf_parent.setVisibility(View.GONE);
                }else{
                    bt_pdf_parent.setVisibility(View.VISIBLE);
                }

                displayFromUri(Uri.parse(filePath));
            }
        });
    }


    /**
     * 通过文件路径读取pdf
     * @param uri
     */
    private void displayFromUri(Uri uri) {
//        pdfFileName = getFileName(uri);
        pdfFileName = title;
        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .load();
    }


    void afterViews() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );

            return;
        }
    }

    private void createSignFile(Bitmap bitmap, int position) {
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        String fileName = "";
        File file = null;
        try {
            fileName = time+"_"+userId + "_" + tblStudyDataId + "_" + i + ".png";

            File fileFolder = new File(path);
            if (!fileFolder.isDirectory()) {
                fileFolder.mkdirs();
            }
            file = new File(path + fileName);
            if (file.exists()){
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            baos = new ByteArrayOutputStream();
            // 如果设置成Bitmap.compress(CompressFormat.JPEG, 100, fos) 图片的背景都是黑色的
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            if (b != null) {
                fos.write(b);
            }
            fos.close();
            baos.close();
            bitmap.recycle();
            UserBitmap userBitmap = userBitmapList.get(position);
            userBitmap.setFilaPath(file.getPath());
            userBitmapList.set(position,userBitmap);
//            bitmapPathList.add(path + fileName);
            i++;

        } catch (IOException e) {
            e.printStackTrace();
        }
//		finally {
//			try {
//				if (fos != null) {
//					fos.close();
//				}
//				if (baos != null) {
//					baos.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
    }


    private void showWritePadDialog() {
        mWritePadDialog = new WritePadDialog(mContext, userBitmapShow,
                new WriteDialogListener() {

                    @Override
                    public void onPaintDone(Object object) {
                    }

                    @Override
                    public void onNextPaint(Object object) {
                        // TODO Auto-generated method stub
//                        mSignBitmap = (Bitmap) object;
                        mWritePadDialog.dismiss();
//                        userBitmapList = (List<UserBitmap>) object;
                        map = (Map<Integer, PaintView>) object;
                        if (map!=null){
                            handler.sendEmptyMessage(LOADING);
                            //创建文件
//                            Thread thread = new Thread(){
//                                @Override
//                                public void run() {
//                                    super.run();
                            //创建文件
                            for (Map.Entry<Integer, PaintView> entry : map.entrySet()){
                                PaintView paintView = entry.getValue();
                                Bitmap bitmap = paintView.getPaintBitmap();
                                if (bitmap!=null&&paintView.isDraw()){
                                    createSignFile(bitmap,entry.getKey());
                                }
                            }
                            final List<String> filePath = new ArrayList<String>();
                            final List<String> users = new ArrayList<>();
                                for (int i=0;i<userBitmapList.size();i++){
                                    String path = userBitmapList.get(i).getFilaPath();
                                    if (path!=null&&!path.equals("")){
                                        filePath.add(path);
                                        users.add(userBitmapList.get(i).getUserId());
                                    }
                                }
                                if (isOpenNetwork()){
                                    TreeVo resultTree = QueryUtil.QueryByID(mContext,db,tblStudyDataId);
                                    String studyId = "";
                                    if (!resultTree.getParentId().equals("null")){
                                        String parentId = resultTree.getParentId();
                                        TreeVo parentVo = QueryUtil.QueryByID(mContext,db,parentId);
                                        studyId = String.valueOf(parentVo.getId());
                                    }else{
                                        studyId = String.valueOf(resultTree.getId());
                                    }
                                    TreeVo finalVo = QueryUtil.QueryByID(mContext,db,studyId);
                                    finalVo.setRead(true);
                                    finalVo.setStudyTime(System.currentTimeMillis());
                                    try {
                                        db.saveOrUpdate(finalVo);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                    MyWebWraper.getInstance().uploadSign(mContext, studyId, filePath,users, userId,timeNow,new HttpResultCallBack() {

                                        @Override
                                        public void onResultCallBack(Object... objs) {
                                            // TODO Auto-generated method stub
                                            String result = String.valueOf(objs[0]);
                                            handler.sendEmptyMessage(LOADING_END);
                                            mHandler.sendEmptyMessage(2);
                                        }
                                    });
                                }//无网络存入库中 等到有网络时上传
                                else{
                                    for (int i=0;i<userBitmapList.size();i++){
                                        String path = userBitmapList.get(i).getFilaPath();
                                        if (path!=null&&!path.equals("")){
                                            try {
                                                db.save(userBitmapList.get(i));
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    handler.sendEmptyMessage(LOADING_END);
                                    mHandler.sendEmptyMessage(2);
                                }




                        }

//                            };
//                            thread.start();
                    }


//                    }

                });
        mWritePadDialog.show();
    }


    /**
     * 文件下载弹框
     */
    private void initMessageDialog( String title){
        final TreeVo vo = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
            // 创建构建器
            AlertDialog.Builder builder = new AlertDialog.Builder(PDFNewActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            // 设置参数
            builder.setTitle(title);
            builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    if (pageNumber != pageCount-1){
                        finish();
                    }else if (pageNumber == pageCount-1&&vo!=null){
                        finish();
                    }
                    else{
                        showWritePadDialog();
                    }
                    // TODO Auto-generated method stub
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    // TODO Auto-generated method stub
                    if (pageNumber != pageCount-1){
                        dialog.dismiss();
                    }else if (pageNumber == pageCount-1&&vo!=null){
                        dialog.dismiss();
                    }
                    else{
                        dialog.dismiss();
                        finish();
                    }
                }
            });
            dialog = builder.create();
        dialog.show();
    }




    /**
     * 获取组员信息
     */
    private void initUser(){
        list.clear();
        userBitmapList.clear();
        UserListCheckedBean userListCheckedBean = (UserListCheckedBean) getIntent().getSerializableExtra("users");
        list = userListCheckedBean.getUserList();
        if (list!=null&&list.size()>0){
            for (int i=0;i<list.size();i++){
                User user = list.get(i);
                UserBitmap userBitmap = new UserBitmap();
                userBitmap.setUserId(user.getCode());
                userBitmap.setName(user.getName());
                userBitmap.setCreateTime(timeNow);
                userBitmap.setTblStudyDataId(tblStudyDataId);
                userBitmapList.add(userBitmap);
                userBitmapShow.add(userBitmap);
            }
        }
    }



    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        this.pageCount = pageCount;
        setTitle(String.format("%s %s / %s", fileName, page + 1, pageCount));
        if (page == pageCount-1){
            TreeVo vo = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
            if (vo !=null){
                bt_pdf_new.setVisibility(View.VISIBLE);
            }
        }else{
            bt_pdf_new.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                TreeVo vo = QueryUtil.QueryByPDF(mContext,db,tblStudyDataId);
                if (pageNumber != pageCount-1){
                    initMessageDialog("您还没有学习完毕，是否退出");
                }else if (pageNumber == pageCount-1&&vo !=null){
                    initMessageDialog("您还没有学习附件,是否退出");
                }
                else{
                    initMessageDialog("您已经学习完毕，是否签名");
                }
                break;
        }
        return false;
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }



    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
}
