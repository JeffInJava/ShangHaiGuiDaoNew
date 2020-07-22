package com.ineasnet.shanghaiguidao2.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.activity.MainActivity;
import com.ineasnet.shanghaiguidao2.activity.PDFNewActivity;
import com.ineasnet.shanghaiguidao2.activity.UserCheckActivity;
import com.ineasnet.shanghaiguidao2.activity.UserCheckAddActivity;
import com.ineasnet.shanghaiguidao2.activity.UserSelectActivity;
import com.ineasnet.shanghaiguidao2.adapters.ArticleAdapter;
import com.ineasnet.shanghaiguidao2.adapters.MyGridAdapter;
import com.ineasnet.shanghaiguidao2.adapters.MyIndexListAdapter;
import com.ineasnet.shanghaiguidao2.adapters.TypeAdapter;
import com.ineasnet.shanghaiguidao2.adapters.UserCheckPopAdapter;
import com.ineasnet.shanghaiguidao2.dao.FileDao;
import com.ineasnet.shanghaiguidao2.entity.bean.CheckBean;
import com.ineasnet.shanghaiguidao2.entity.bean.FileEntity;
import com.ineasnet.shanghaiguidao2.entity.bean.ListVoBean;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.bean.UserListCheckedBean;
import com.ineasnet.shanghaiguidao2.entity.vo.CheckFileVo;
import com.ineasnet.shanghaiguidao2.entity.vo.DepartmentVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserHistoryVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserUpload;
import com.ineasnet.shanghaiguidao2.entity.vo.UserVo;
import com.ineasnet.shanghaiguidao2.entity.vo.VersionTreeVo;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.ListUtil;
import com.ineasnet.shanghaiguidao2.util.QueryHistoryVo;
import com.ineasnet.shanghaiguidao2.util.QueryUtil;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.ineasnet.shanghaiguidao2.util.TypeQueryUtil;
import com.ineasnet.shanghaiguidao2.view.CommonProgressDialog;
import com.ineasnet.shanghaiguidao2.view.MyGridView;
import com.orhanobut.logger.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;
import androidPT.ptUtil.base.MyBaseFragment;
import androidPT.ptUtil.internet.HttpInstance;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;
import androidPT.ptUtil.storage.StorageUtil;
import androidPT.ptWidget.spinner.AbstractSpinerAdapter;
import androidPT.ptWidget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import androidPT.ptWidget.spinner.SpinerPopWindow;

public class IndexFragment extends MyBaseFragment implements OnClickListener,CommonProgressDialog.OnOkAndCancelListener {
    private View v;

    // 查询控件
    private RelativeLayout spinner_dep;
    private RadioGroup radioGroup;
    private RadioButton nameBtn, docBtn, ruleBtn, qwjsBtn;
    private EditText edit_search_key;
    private Button btn_seach;
    private TextView tv_search_dep;
    private ImageView iv_search_type;

    // 列表控件
    private MyGridView gridView;
    private GridView gridView2;
    public static  GridView gridView_sec;
    public static  ListView listView;
    private ListView listView_index;// 全文检索得到的结果列表（段落）
    private List<TreeVo> showList = new ArrayList<TreeVo>();
    private List<TreeVo> showListNum = new ArrayList<TreeVo>();

    // 导航控件
    public static HorizontalScrollView hScroll_guide;
    public static LinearLayout layout_guide;
    private TextView tv_guide_index;
    /***********************************************/

    private Context mContext;

    /**
     * 索引文件位置
     */
    private String indexFolderPath;

    /**
     * 所有数据的list
     */
    private List<TreeVo> allList;

    /**
     * 第一级List
     */
    private List<TreeVo> headList;

    /**
     * 被查询的list对象;
     */
    private List<TreeVo> searchedList;

    /**
     * 查询得到的list
     */
    private List<TreeVo> searchResultList;

    /**
     * 全文检索得到的结果集
     */
    private List<Map<String, Object>> indexSearchResultList = new ArrayList<Map<String, Object>>();

    /**
     * 下方列表是否是查询得到的
     */
    private static boolean isSearched;

    /**
     * 是否是全文检索
     */
    private boolean isIndexSearch;

    /**
     * 各级数据Map集合
     */
    private Map<String, List<TypeVo>> map = new HashMap<String, List<TypeVo>>();

    /**
     * 叶节点/叶子节点list
     */
    private List<TreeVo> childList;

    /**
     * 存放导航条的textview key 父节点名称
     */
    public static Map<String, TextView> map_tab = new HashMap<String, TextView>();

    /**
     * 存放tab的顺序 用于删除
     */
    public static Map<String, Integer> map_tab_order = new HashMap<String, Integer>();

    /**
     * 被点击的tab的序号 大于该序号的tab都要删除
     */
    private int current_tabNo = 0;

    boolean isFromPro = false;

    /**
     * 部门状态 0：企业标准 2：专家评审
     */
    private int depStatus;

    private List<DepartmentVo> depList;

    private String userId;

    private StorageUtil storageUtil;

    private MyGridAdapter gridAdapter;
    private MyGridAdapter gridAdapter2;
    private MyGridAdapter gridAdapter_sec;
    private ArticleAdapter listAdapter;
    private MyIndexListAdapter indexListAdapter;

    private String userName;

    private AbstractSpinerAdapter mAdapter;

    private SpinerPopWindow mSpinerPopWindow;

    private List<TreeVo> downloadList = new ArrayList<TreeVo>();

    /**
     * 部门编号
     */
    private String depNo = "";

    /**
     * 搜索条件
     */
    private String search_key;

    /**
     * 文件存放路径
     */
    private String path;

    private String sessionId;

    /**
     * listview点击的条目位置
     */
    private int pos_clicked;

    /**
     * listview 是否可以点击（为防止未下载完成时点击跳转报错）
     */
    private boolean clickAble = true;

    /**
     * true 下载完成 false 下载未完成
     */
    private boolean isFinished;

    private boolean isReload = true;
    private Gson gson;
    public static TypeVo titleVo;
    DepartmentVo value;
    private MyApplication application;
    List<FileDao> fileDaoList = new ArrayList<>();
    private String type;
    private TreeVo resultTree;

    private boolean isFromIndexflag;
    private List<User> userList = new ArrayList<User>();
    //选中的用户
    private List<User> checkedList = new ArrayList<User>();
    private PopupWindow window;
    private PopupWindow typeWindow;
    private PopupWindow searchTypeWindow;

    Button bt_check = null;
    Button bt_cancel = null;
    Button bt_search = null;
    ListView list_user = null;
    Button bt_check2 = null;
    Button bt_cancel2 = null;
    ListView list_user2 = null;
    ListView list_user3 = null;
    UserCheckPopAdapter userCheckAdapter = null;
    private TypeAdapter typeAdapter;
    private TypeAdapter searchAdapter;

    private CommonProgressDialog mDialog;
    private int dataCount = 1;// 记录累加的值
    private boolean isCancel; // 用来判断是否点击了取消
    private AlertDialog dialog;
    private TreeVo dialogVo = null;
    private TreeVo secondVo = null;
    private int dialogPosition = 0;
    private List<VersionTreeVo> versionList = new ArrayList<>();
    private DbManager db;
    //类别数据
    private Map<Integer,List<TypeVo>> typeMap = new HashMap<>();
    private List<TypeVo> typeVoList = new ArrayList<>();
    //类型标识符
    private String folderName = "";
    public static  ScrollView sv_index;
    private List<CheckBean> typeList = new ArrayList<>();
    private List<CheckBean> searchList = new ArrayList<>();
    private ImageView iv_searchtype;
    private int typeCount = 0;
    //是否是全局搜索
    private int allSearch = 0;
    private Button bt_type;
    private LinearLayout ll_main_bg;
    private View userView = null;
    private List<User> userListData = new ArrayList<>();
    private List<UserHistoryVo> listUsers = new ArrayList<>();
    /**
     * 规章制度vo
     */

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    gridView_sec.setVisibility(View.VISIBLE);
                    sv_index.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    break;
                case 5:
                    TypeVo listVo = (TypeVo) msg.obj;
                    showChild(listVo);
                    break;
                case 1:
                    gridAdapter = new MyGridAdapter(mContext, typeMap.get(1));
                    gridView.setAdapter(gridAdapter);
                    break;
                case 2:
//                    String name = String.valueOf(msg.obj);
//                    showChildList(map.get(name));
                    break;
                case 4:
                    int progress = msg.arg1;
                    int pos = msg.arg2;
                    // int pos = pos_clicked - listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(pos);
                    ProgressBar bar = (ProgressBar) v
                            .findViewById(R.id.list_item_progress);
                    TextView tv = (TextView) v.findViewById(R.id.list_item_tv);
                    bar.setVisibility(View.VISIBLE);
                    bar.setProgress(progress);
                    if (progress == 100) {
                        clickAble = true;
                        bar.setVisibility(View.GONE);
                        tv.setTextColor(mContext.getResources().getColor(
                                android.R.color.black));
                        downloadList.remove(listView.getItemAtPosition(pos));
                    }

                    break;
                case 3:

                    mAdapter = new AbstractSpinerAdapter(mContext);
                    mAdapter.refreshData(depList, 0);
                    mSpinerPopWindow = new SpinerPopWindow(mContext);
                    mSpinerPopWindow.setAdatper(mAdapter);
                    mSpinerPopWindow.setItemListener(new IOnItemSelectListener() {

                        @Override
                        public void onItemClick(int pos) {
                            // TODO Auto-generated method stub
                            setDep(pos);
                        }
                    });

                    showSpinWindow();
                    break;
                //获取书籍列表
                case 7:
                    if (searchResultList.size() > 0) {
                        showChildList(searchResultList);
                    } else {
                        listView.setVisibility(View.GONE);
                        sv_index.setVisibility(View.GONE);
                        Toast.makeText(mContext, "未查询到结果", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //该文件已分配
                case 8:
                    List<CheckFileVo> fileVos = (List<CheckFileVo>) msg.obj;
                    List<User> userList = new ArrayList<User>();
                    for (int i=0;i<fileVos.size();i++){
                        User user = new User();
                        user.setCode(fileVos.get(i).getUserId());
                        user.setName(fileVos.get(i).getUserName());
                        userList.add(user);
                    }
                    UserVo userVo = new UserVo();
                    userVo.setPsmList(userList);
                    userVo.setDeptCode("");
                    String pdfName = resultTree.getPdfName();
                    String path = FileName.FILEPATH + "ftpIndexAll/" + resultTree.getPdfName();
                    Intent intent = new Intent(mContext, UserSelectActivity.class);
                    intent.putExtra("filePath", path);
                    intent.putExtra("tblStudyDataId", resultTree.getId());
                    intent.putExtra("userId", userId);
                    intent.putExtra("isFromIndex", isFromIndexflag);
                    intent.putExtra("page", resultTree.getPageCount());
                    intent.putExtra("departmentNo",MainActivity.departmentNo);
                    intent.putExtra("bean",userVo);
                    intent.putExtra("fileName",pdfName);
                    startActivity(intent);
                    break;
                //该文件未分配
                case 9:
                String pdfName2 = resultTree.getPdfName();
                 String path2 = FileName.FILEPATH + "ftpIndexAll/" + resultTree.getPdfName();
		        Intent intent2 = new Intent(mContext, UserCheckActivity.class);
		        intent2.putExtra("filePath", path2);
		        intent2.putExtra("tblStudyDataId", resultTree.getId());
		        intent2.putExtra("userId", userId);
		        intent2.putExtra("isFromIndex", isFromIndexflag);
		        intent2.putExtra("page", resultTree.getPageCount());
		        intent2.putExtra("departmentNo",MainActivity.departmentNo);
		        startActivity(intent2);
                    break;
                //等待中
                case 10:
                    if (listAdapter!=null){
                        listAdapter.notifyDataSetChanged(showList);
                    }else{
                        listAdapter = new ArticleAdapter(mContext,showList);
                        listView.setAdapter(listAdapter);
                    }

                    break;
                //下载完毕
                case 11:
                    TreeVo vo = (TreeVo) msg.obj;
                    vo.setDownloading(false);
                    showList.set(msg.arg1,vo);
                    FileDao dao = new FileDao();
                    dao.setPdfName(vo.getPdfName());
                    dao.setPdfNameUrl(vo.getPdfNameUrl());
                    try {
                        db.save(dao);
                        fileDaoList = db.selector(FileDao.class).findAll();
                        listAdapter.notifyDataSetChanged(showList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(mContext, "下载完毕", Toast.LENGTH_SHORT).show();
                    break;
                //阅读PDF
                case 12:
                    pdfName = resultTree.getPdfName();
                    path = FileName.FILEPATH + "ftpIndexAll/" + resultTree.getPdfName();
                    UserListCheckedBean userList2 = new UserListCheckedBean();
                    userList2.setUserList(checkedList);
                    intent = new Intent(mContext,PDFNewActivity.class);
                    intent.putExtra("users",userList2);
                    intent.putExtra("filePath", path);
                    intent.putExtra("tblStudyDataId", resultTree.getId());
                    intent.putExtra("userId", userId);
                    intent.putExtra("isFromIndex", isFromIndexflag);
                    intent.putExtra("page", resultTree.getPageCount());
                    intent.putExtra("departmentNo",MainActivity.departmentNo);
                    intent.putExtra("fileName",pdfName);
                    intent.putExtra("title",resultTree.getName());
                    mContext.startActivity(intent);
                    closePopupWindow();
                    break;
                default:
                    break;
            }

        }

        ;
    };

    private void setDep(int pos) {
        value = depList.get(pos);
        String depName = value.getDepartmentName();
        if (pos > 0 && pos <= depList.size()) {
            depNo = value.getDepartmentNo();
            tv_search_dep.setTextColor(getActivity().getResources().getColor(
                    android.R.color.black));
        } else if (pos == 0) {
            depNo = "";
            tv_search_dep.setTextColor(getActivity().getResources().getColor(
                    R.color.color_spinner_unselect));
            ;
        }
        tv_search_dep.setText(depName);

        try {
            fileDaoList = db.selector(FileDao.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(HttpInstance.ACTION_READ);
        filter.addAction(HttpInstance.ACTION_UNREAD);
        filter.addAction(HttpInstance.ACTION_NONET);
        filter.addAction(HttpInstance.ACTION_BACK);
        filter.addAction(HttpInstance.ACTION_DOWNLOADFINISH);
//        activity.registerReceiver(mReceiver, filter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.fragment_index, null);
        application = (MyApplication) getActivity().getApplication();
        db = x.getDb(application.getDaoConfig());
        try {
            fileDaoList = db.selector(FileDao.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        mContext = getActivity();
        storageUtil = new StorageUtil(mContext);

        userId = MainActivity.userId;
        userName = MainActivity.userName;
        isFromPro = MainActivity.isFromPro;

        tv_title = (TextView) v.findViewById(R.id.tv_title_content);
        iv_search_type = (ImageView) v.findViewById(R.id.iv_search_type);
        spinner_dep = (RelativeLayout) v.findViewById(R.id.spinner_dep);
        tv_search_dep = (TextView) v.findViewById(R.id.tv_search_dep);
        radioGroup = (RadioGroup) v.findViewById(R.id.layout_search_type);
        docBtn = (RadioButton) v.findViewById(R.id.tv_search_docNum);
        ruleBtn = (RadioButton) v.findViewById(R.id.tv_search_ruleNum);
        edit_search_key = (EditText) v.findViewById(R.id.edit_search_content);
        btn_seach = (Button) v.findViewById(R.id.btn_search);
        listView = (ListView) v.findViewById(R.id.listview_content);
        listView_index = (ListView) v
                .findViewById(R.id.listview_indexSearch_result);
        gridView = (MyGridView) v.findViewById(R.id.gridview_content);
        gridView2 = (GridView) v.findViewById(R.id.gridview_content2);
        ll_main_bg = (LinearLayout) v.findViewById(R.id.ll_main_bg);
        gridView_sec = (GridView) v.findViewById(R.id.gridview_sec_content);
        sv_index = (ScrollView) v.findViewById(R.id.sv_index);
        hScroll_guide = (HorizontalScrollView) v
                .findViewById(R.id.hScroll_guide);
        layout_guide = (LinearLayout) v.findViewById(R.id.layout_guide);
        tv_guide_index = (TextView) v.findViewById(R.id.tv_guide_index);
        bt_type = (Button) v.findViewById(R.id.bt_type);
        iv_searchtype = (ImageView) v.findViewById(R.id.iv_searchtype);

        ListVoBean listVoBean = (ListVoBean) getActivity().getIntent().getSerializableExtra("versionBean");
        versionList = QueryHistoryVo.QueryVersionAll(mContext,db);
//        if (listVoBean!=null&&listVoBean.getList().size()>0){
//            versionList = listVoBean.getList();
//        }
        // 数据初始化
        isSearched = false;
        path = PropertiesUtil.getPropertiesURL(mContext, "filePath");
        sessionId = storageUtil.getSessionId(userId);
        if (isFromPro) {
            docBtn.setVisibility(View.GONE);
            ruleBtn.setVisibility(View.GONE);
            indexFolderPath = PropertiesUtil.getPropertiesURL(mContext,
                    "proIndexFolderPath");
        } else {
            docBtn.setVisibility(View.VISIBLE);
            ruleBtn.setVisibility(View.VISIBLE);
            indexFolderPath = PropertiesUtil.getPropertiesURL(mContext,
                    "stanIndexFolderPath");
        }

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        tv_guide_index.setOnClickListener(this);
        spinner_dep.setOnClickListener(this);
        btn_seach.setOnClickListener(this);
        bt_type.setOnClickListener(this);
        iv_searchtype.setOnClickListener(this);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TypeVo vo = (TypeVo) gridView.getItemAtPosition(arg2);
                folderName = vo.getName();
                titleVo = vo;
                dataCount+=1;
                if (!isFromPro) {
                    showSecGridView(vo);
                }
            }
        });
        gridView2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TypeVo vo = (TypeVo) gridView2.getItemAtPosition(arg2);
                folderName = vo.getName();
                titleVo = vo;
                dataCount+=1;
                if (!isFromPro) {
                    showSecGridView(vo);
                }
            }
        });

        gridView_sec.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TypeVo vo = (TypeVo) gridView_sec.getItemAtPosition(arg2);
                titleVo = vo;
                folderName = vo.getName();
                showChild(vo);
            }
        });

        listView.setOnItemClickListener(listViewClick);
        listView.setOnItemLongClickListener(longClickListener);
        listView_index.setOnItemClickListener(indexListViewClick);

        if (isReload) {
            init();
        }
        initUsers();
        initUserPop();
        initDialog();
        initTypeInfo();
        initType();
        initSearchTypeInfo();
        initSearchType();
        return v;
    }


    private void initUsers(){
        gson = new Gson();
        userId = SettingUtils.get(mContext,"userId");
//        String usersJson = SettingUtils.get(mContext,"users"+userId);
//        userList = gson.fromJson(usersJson, new TypeToken<List<User>>(){}.getType());
        try {
            userList = db.findAll(User.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        List<User> checkOffUserList = new ArrayList<>();
        List<User> checkOnUserList = new ArrayList<>();
        for (int i=0;i<userList.size();i++){
            if (!userList.get(i).isCheck()){
                checkOffUserList.add(userList.get(i));
            }else{
                checkOnUserList.add(userList.get(i));
            }
        }
        userList.clear();
        userList.addAll(checkOnUserList);
        userList.addAll(checkOffUserList);
    }

    OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // TODO Auto-generated method stub
            Log.e("22222", "2222222222222222222222222222");
            return false;
        }
    };

    /**
     * listview OnItemClickListener
     */
    OnItemClickListener listViewClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            TreeVo vo = showList.get(arg2);
            boolean isLast = true;

            if (clickAble) {
                if (vo.getPdfNameUrl() == null) {
                    Toast.makeText(mContext, "没有文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                isFinished = false;

                String pdfName = vo.getPdfName();
                boolean flag = false;
                File file = new File(path+"/ftpIndexAll/" + pdfName);
                    if (!file.exists()){
                        flag = true;
                    }
                    //本地无文件并且处于非下载状态
                if (flag) {
                    dialogVo = vo;
                    dialogPosition = arg2;
                    initMessageDialog(vo,arg2,"该文件还未下载，是否下载该文件");
                }
                else if(vo.isNew() == false){
                    dialogVo = vo;
                    dialogPosition = arg2;
                    initMessageDialog(vo,arg2,"该文件已有新版本，请下载新版本阅读");
                }
                else {
                    int position = arg2-listView.getFirstVisiblePosition();
                    jumpToPDFShow(vo, position, false, 0,arg1);
                }

            } else {
                Toast.makeText(mContext, "请等待下载完成", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    /**
     * 全文检索 段落列表的OnItemClickListener
     */
    OnItemClickListener indexListViewClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            Map<String, Object> map_result = (Map<String, Object>) listView_index
                    .getItemAtPosition(arg2);
            int page = Integer.parseInt(String.valueOf(map_result.get("page")));
            TreeVo vo = (TreeVo) map_result.get("vo");

            jumpToPDFShow(vo, arg2, true, page,arg1);
        }
    };

    private void jumpToPDFShow(TreeVo vo, int position, boolean isFromIndex,
                               int page,View view) {

        ImageView iv_article = (ImageView) view.findViewById(R.id.iv_article);
        isFromIndexflag = isFromIndex;
        resultTree = vo;
        startPopupWindow(vo,iv_article,position);
    }



    /**
     * @param file 需要打开的文件
     * @return 文件字节数
     * @throws Exception
     */
    @SuppressWarnings("resource")
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    @Override
    protected void init() {
        try {
            typeVoList = db.findAll(TypeVo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        if (isOpenNetwork()) {
            handler.sendEmptyMessage(LOADING);

            if (isFromPro) {
                depStatus = 2;
            } else {
                depStatus = 0;
            }
            initDatas();
//            MyWebWraper.getInstance().getDepartments(getActivity(), depStatus,
//                    cb_getDepartment);// 获取部门列表

            // }

        } else {
            String depStr = storageUtil.getDepList(userId);
            depList = initDepartment(depStr);

            String fileListStr = storageUtil.getFileList(userId);
            initDatas();
        }
    }

    private HttpResultCallBack download_cb = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            isFinished = true;
        }
    };

    //文件索引目录
    private HttpResultCallBack selectPDF = new HttpResultCallBack() {
        @Override
        public void onResultCallBack(Object... objs) {
            if (String.valueOf(objs[0])!=null&&!String.valueOf(objs[0]).equals("")){
                String result = String.valueOf(objs[0]);
                Logger.json(result);
                if (!result.contains("exception")) {
                    FileEntity entity = gson.fromJson(result, FileEntity.class);
                    if (entity != null) {
                        searchResultList = entity.getRows();
                        if (searchResultList.size()>10){
                            for (int i=0;i<11;i++){
                                showList.add(searchResultList.get(i));
                            }
                        }else{
                            showList.addAll(searchResultList);
                        }
                        mHandler.sendEmptyMessage(7);
                    }

                }
            }

        }
    };

    private HttpResultCallBack cb = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            handler.sendEmptyMessage(LOADING_END);
            String result = String.valueOf(objs[0]);
            boolean isException = handleException(getActivity(), result);
            if (isException) {

                try {
//                    initDatas(result);

                    // if (isSearched) {
                    // mDatas.clear();
                    // search();
                    // }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    };

    /**
     * 初始化类别
     */
    private void initDatas() {
            handler.sendEmptyMessage(LOADING_END);
            if (isFromPro) {
                map.put("father", TypeQueryUtil.QueryByType(mContext,db,"1000018"));
                typeMap.put(dataCount, TypeQueryUtil.QueryByType(mContext,db,"1000018"));
                mHandler.sendEmptyMessage(1);
            } else {
                typeMap.put(dataCount, TypeQueryUtil.QueryByType(mContext,db,"1000018"));
                gridAdapter = new MyGridAdapter(mContext, typeMap.get(1));
                gridView.setAdapter(gridAdapter);
                gridAdapter2 = new MyGridAdapter(mContext, TypeQueryUtil.QueryByType(mContext,db,"1000081"));
                gridView2.setAdapter(gridAdapter2);
                sv_index.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                gridView_sec.setVisibility(View.GONE);
                listView_index.setVisibility(View.GONE);

            }
    }

    @Override
    protected void handleHttpResult() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            //点击首页
            case R.id.tv_guide_index:
                // clear map_tab_order、map_tab、layout_guide
                isSearched = false;

                Iterator iterator = map_tab.keySet().iterator();
                while (iterator.hasNext()) {
                    String tabName_temp = iterator.next().toString();
                    layout_guide.removeView(map_tab.get(tabName_temp));
                    iterator.remove();
                    map_tab.remove(tabName_temp);
                }
                titleVo = null;
                map_tab_order.clear();

                // hide guidebar
                hScroll_guide.setVisibility(View.GONE);
                // hide gridview_sec
                gridView_sec.setVisibility(View.GONE);
                // hide listview
                listView.setVisibility(View.GONE);
                // show gridview
                sv_index.setVisibility(View.VISIBLE);
                getBg(true);
                break;
            case R.id.spinner_dep:
                Message msg = new Message();
                msg.what = 3;
                msg.obj = depList;
                mHandler.sendMessage(msg);
                break;
            case R.id.btn_search:
                showList.clear();
                search_key = edit_search_key.getText().toString().trim();
                int radioBtnId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) this.v
                        .findViewById(radioBtnId);
                type = radioBtn.getText().toString().trim();
                isSearched = true;
                //			//获取文件索引
                String departmentNo;
                if (value != null) {
                    departmentNo = value.getDepartmentNo();
                } else {
                    departmentNo = "";
                }
                String treeNo;
                if (titleVo != null) {
                    treeNo = String.valueOf(titleVo.getId());
                } else {
                    treeNo = "";
                }
                //全局搜索
                if (allSearch == 0){
                    Query();
                }else{
                    Query(getTabName());
                }

                break;
            case R.id.bt_type:
                startTypePopupWindow();
                break;
            case R.id.iv_searchtype:
                startSearchTypePopupWindow();
                break;
            default:
                break;
        }
    }


    private void Query(){
//        List<TreeVo> resultTree = new ArrayList<>();
//        switch (typeCount){
//            case 0:
//                if (QueryUtil.QueryByPDFName(getActivity(),db,search_key,0) == null||QueryUtil.QueryByPDFName(getActivity(),db,search_key).size()<1){
//                    showList.clear();
//                }else{
//                    resultTree = QueryUtil.QueryByPDFName(getActivity(),db,search_key);
//                }
//                break;
//            case 1:
//                if (QueryUtil.QueryByPDFDocNum(getActivity(),db,search_key) == null||QueryUtil.QueryByPDFDocNum(getActivity(),db,search_key).size()<1){
//                    showList.clear();
//                }else{
//                    resultTree = QueryUtil.QueryByPDFDocNum(getActivity(),db,search_key);
//                }
//                break;
//            case 2:
//                if (QueryUtil.QueryByPDFRuleNum(getActivity(),db,search_key) == null||QueryUtil.QueryByPDFRuleNum(getActivity(),db,search_key).size()<1){
//                    showList.clear();
//                }else{
//                    resultTree = QueryUtil.QueryByPDFRuleNum(getActivity(),db,search_key);
//                }
//                break;
//        }
//        showChildList(resultTree);
    }

    private void Query(String type){
//        List<TreeVo> resultTree = new ArrayList<>();
//        switch (typeCount){
//            case 0:
//                if (QueryUtil.QueryByPDFName(getActivity(),db,search_key,type) == null||QueryUtil.QueryByPDFName(getActivity(),db,search_key,type).size()<1){
//                    showList.clear();
//                }else{
//                    resultTree = QueryUtil.QueryByPDFName(getActivity(),db,search_key,type);
//                }
//                break;
//            case 1:
//                if (QueryUtil.QueryByPDFDocNum(getActivity(),db,search_key,type) == null||QueryUtil.QueryByPDFDocNum(getActivity(),db,search_key,type).size()<1){
//                    showList.clear();
//                }else{
//                    resultTree = QueryUtil.QueryByPDFDocNum(getActivity(),db,search_key,type);
//                }
//                break;
//            case 2:
//                if (QueryUtil.QueryByPDFRuleNum(getActivity(),db,search_key,type) == null||QueryUtil.QueryByPDFRuleNum(getActivity(),db,search_key,type).size()<1){
//                    showList.clear();
//                }else{
//                    resultTree = QueryUtil.QueryByPDFRuleNum(getActivity(),db,search_key,type);
//                }
//                break;
//        }
//        showChildList(resultTree);
    }

    private HttpResultCallBack cb_getDepartment = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            // MyWebWraper.getInstance().getFileList(mContext, cb);
            String result = String.valueOf(objs[0]);
            boolean isException = handleException(getActivity(), result);

            if (isException) {
                storageUtil.storeDepartments(userId, result);
                depList = initDepartment(result);
                MyWebWraper.getInstance().getFileList(mContext, cb);
            }
        }
    };

    private List<DepartmentVo> initDepartment(String depStr) {
        List<DepartmentVo> depList = new ArrayList<DepartmentVo>();
        Type type = new TypeToken<ArrayList<DepartmentVo>>() {
        }.getType();
        gson = new Gson();
        try {
            List<DepartmentVo> depList_temp = gson.fromJson(depStr,
                    type);

            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepartmentName("所有部门");
            departmentVo.setDepartmentNo(null);
            depList.add(departmentVo);

            depList.addAll(depList_temp);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return depList;
    }

    private List<TreeVo> secList;

    private void showSecGridView(TypeVo vo) {
        List<TypeVo> typeVoListNew = new ArrayList<>();
        typeVoListNew = TypeQueryUtil.QueryByType(mContext,db,vo.getId());
        addGuideTab(vo.getName());
        hScroll_guide.setVisibility(View.VISIBLE);
        gridAdapter_sec = new MyGridAdapter(mContext, typeVoListNew);
        gridView_sec.setAdapter(gridAdapter_sec);

        sv_index.setVisibility(View.GONE);
        gridView_sec.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        listView_index.setVisibility(View.GONE);
    }

    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(spinner_dep.getWidth());
        mSpinerPopWindow.showAsDropDown(spinner_dep);
    }

    /**
     * 显示子节点列表
     * <p>
     * 父节点名称
     */
    private void showChildList(List<TreeVo> child) {
        showList.clear();
        showList = child;
        showListNum.clear();
        getBg(false);
        sv_index.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        gridView_sec.setVisibility(View.GONE);
        showList = ListUtil.removeSameList(showList);
        showList = ListUtil.removeSecondList(showList);
        showListNum.addAll(showList);
        if (showList != null && showList.size() > 0) {
//            if (listAdapter == null){
            listAdapter = new ArticleAdapter(mContext, showList);
            listView.setAdapter(listAdapter);
//            }else{
//                listAdapter.notifyDataSetChanged(showList);
//            }


        } else {
            listView.setVisibility(View.GONE);
            Toast.makeText(mContext, "该目录下没有文件", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param vo listViewItem 返回的参数，取其ID作为parentId去查找其子节点
     */
    private void showChild(TypeVo vo) {
        List typeVoListNew = null;
        if (TypeQueryUtil.QueryByType(mContext,db,vo.getId())!=null&&TypeQueryUtil.QueryByType(mContext,db,vo.getId()).size()>0){
            typeVoListNew = TypeQueryUtil.QueryByType(mContext,db,vo.getId());
        }
        //是否为类别最底层
        if (typeVoListNew!=null&&typeVoListNew.size() != 0) {
            map.put(vo.getName(), typeVoListNew);
            addGuideTab(vo.getName());
            hScroll_guide.setVisibility(View.VISIBLE);

            for (Map.Entry<String, TextView> entry : map_tab.entrySet()) {// 遍历guidetab
                // 的map
                // 取出所有TextView
                // 将不是当前节点的tab字体颜色改成黑色
                if (!entry.getKey().equals(vo.getName())) {
                    entry.getValue().setTextColor(
                            getResources().getColor(
                                    R.color.color_text_guide_unselected));
                }
            }
            gridAdapter_sec.notifyDataSetChanged(typeVoListNew);
        } else {
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            final TextView tv_tab = new TextView(mContext);
            tv_tab.setTextSize(20);
            tv_tab.setGravity(Gravity.CENTER_VERTICAL);
            tv_tab.setPadding(20, 0, 10, 0);
            tv_tab.setTextColor(getResources().getColor(
                    R.color.color_text_guide_selected));
            tv_tab.setText(vo.getName() + "/");
            tv_tab.setLayoutParams(params);

            map_tab.put(vo.getName(), tv_tab);// 存入textview
            map_tab_order.put(vo.getName(), current_tabNo);// 存入序号
            layout_guide.addView(tv_tab);
            for (Map.Entry<String, TextView> entry : map_tab.entrySet()) {// 遍历guidetab
                // 的map
                // 取出所有TextView
                // 将不是当前节点的tab字体颜色改成黑色
                if (!entry.getKey().equals(vo.getName())) {
                    entry.getValue().setTextColor(
                            getResources().getColor(
                                    R.color.color_text_guide_unselected));
                }
            }
            Query(vo.getName());
        }

    }

    /**
     * 获取类别内容
     * @return
     */
    private String getTabName(){
        String tabName = "";
        for (Map.Entry<String, TextView> entry : map_tab.entrySet()) {// 遍历guidetab
            // 的map
            // 取出所有TextView
            // 将不是当前节点的tab字体颜色改成黑色
            tabName +=entry.getValue().getText().toString().trim();
        }
        return tabName;
    }

    /**
     * 添加导航tab
     *
     * @param tabName 导航条每个tab的名称 以父节点命名
     */
    private void addGuideTab(String tabName) {

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        final TextView tv_tab = new TextView(mContext);
        tv_tab.setTextSize(20);
        tv_tab.setGravity(Gravity.CENTER_VERTICAL);
        tv_tab.setPadding(20, 0, 10, 0);
        tv_tab.setTextColor(getResources().getColor(
                R.color.color_text_guide_selected));
        tv_tab.setText(tabName + "  /");
        tv_tab.setLayoutParams(params);

        map_tab.put(tabName, tv_tab);// 存入textview
        map_tab_order.put(tabName, current_tabNo);// 存入序号
        layout_guide.addView(tv_tab);

        current_tabNo++;
        tv_tab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                isSearched = false;
                tv_tab.setTextColor(getResources().getColor(
                        R.color.color_text_guide_selected));

                // 取出text 以“/”分割 trim后得到key
                String key = tv_tab.getText().toString().trim();
                String[] keys = key.split("/");
                String name = keys[0].trim();

                // 删除子级的tab
                int order = map_tab_order.get(name);

                Iterator iterator = map_tab.keySet().iterator();
                while (iterator.hasNext()) {
                    String tabName_temp = iterator.next().toString();
                    int tabNo = map_tab_order.get(tabName_temp);
                    if (tabNo > order) {// 需要删除的tab
                        layout_guide.removeView(map_tab.get(tabName_temp));
                        iterator.remove();
                        map_tab.remove(tabName_temp);
                    }
                }
                TypeVo vo = TypeQueryUtil.QueryByTypeVo(mContext,db,name);
                titleVo = vo;
                List typeVoListNew = null;
                if (TypeQueryUtil.QueryByType(mContext,db,vo.getId())!=null&&TypeQueryUtil.QueryByType(mContext,db,vo.getId()).size()>0){
                    typeVoListNew = TypeQueryUtil.QueryByType(mContext,db,vo.getId());
                }
                gridAdapter_sec.notifyDataSetChanged(typeVoListNew);
                sv_index.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                listView_index.setVisibility(View.GONE);
                gridView_sec.setVisibility(View.VISIBLE);
                getBg(true);
            }
        });
    }



    private void downFile(final TreeVo vo, final int position) {
        String path = FileName.FILEPATH + "ftpIndexAll/" + dialogVo.getPdfName();
        RequestParams params = new RequestParams(dialogVo.getPdfNameUrl());
        params.setHeader("Cookie", SettingUtils.get(getActivity(), "cookie"));
        params.setSaveFilePath(path);
        final boolean[] flag = {true};
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                mDialog.show();
                //设置窗口的大小
                mDialog.getWindow().setLayout(900, 600);

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                String result = current + "/" + total;
                int totalNum = Integer.valueOf(String.valueOf(total));
                int currentNum = Integer.valueOf(String.valueOf(current));
                Log.d("下载进度", result);
                if (flag[0]){
                    mDialog.setMax(totalNum);
                    flag[0] = false;
                }

                if (isDownloading){
                    dialogVo.setDownloading(true);
                    showList.set(dialogPosition,dialogVo);
                    mHandler.sendEmptyMessage(10);
                    mDialog.setProgress(currentNum);
                }

            }

            @Override
            public void onSuccess(File result) {
                if (mDialog.isShowing()){
                    mDialog.dismiss();
                }
                showList = showListNum;
                VersionTreeVo versionTreeVo = QueryHistoryVo.QueryVersionById(mContext,db,String.valueOf(vo.getId()));
                if (versionTreeVo!=null){
                    vo.setVersion(versionTreeVo.getVersion());
                    vo.setNew(true);
                    try {
                        db.saveOrUpdate(vo);
                        db.delete(versionTreeVo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                //更新版本
                if ( QueryUtil.QueryByPDF(mContext,db,String.valueOf(vo.getId())) == null) {
                    Message msg = new Message();
                    msg.what = 11;
                    msg.obj = vo;
                    msg.arg1 = position;
                    mHandler.sendMessage(msg);
                }else{
                    secondVo = QueryUtil.QueryByPDF(mContext,db,String.valueOf(vo.getId()));
                    downFileSecond(QueryUtil.QueryByPDF(mContext,db,String.valueOf(vo.getId())));
                    Message msg = new Message();
                    msg.what = 11;
                    msg.obj = vo;
                    msg.arg1 = position;
                    mHandler.sendMessage(msg);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!ex.getMessage().equals("Invalid index 0, size is 0")){
//                    Toast.makeText(mContext, "文件下载失败", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }


    private void downFileSecond(final TreeVo vo) {
        String path = FileName.FILEPATH + "ftpIndexAll/" + secondVo.getPdfName();
        RequestParams params = new RequestParams(secondVo.getPdfNameUrl());
        params.setHeader("Cookie", SettingUtils.get(getActivity(), "cookie"));
        params.setSaveFilePath(path);
        final boolean[] flag = {true};
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                mDialog.show();
                //设置窗口的大小
                mDialog.getWindow().setLayout(900, 600);

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                String result = current + "/" + total;
                int totalNum = Integer.valueOf(String.valueOf(total));
                int currentNum = Integer.valueOf(String.valueOf(current));
                Log.d("下载进度", result);
                if (flag[0]){
                    mDialog.setMax(totalNum);
                    flag[0] = false;
                }

                if (isDownloading){
                    dialogVo.setDownloading(true);
                    showList.set(dialogPosition,dialogVo);
                    mHandler.sendEmptyMessage(10);
                    mDialog.setProgress(currentNum);
                }

            }

            @Override
            public void onSuccess(File result) {
                if (mDialog.isShowing()){
                    mDialog.dismiss();
                }
                //更新版本
                VersionTreeVo versionTreeVo = QueryHistoryVo.QueryVersionById(mContext,db,String.valueOf(vo.getId()));
                if (versionTreeVo!=null){
                    vo.setVersion(versionTreeVo.getVersion());
                    vo.setNew(true);
                    try {
                        db.saveOrUpdate(vo);
                        db.delete(versionTreeVo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext, "文件下载失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 人员选择框
     */
    private void initUserPop(){
        //TODO
        if (window == null){
            LayoutInflater lay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            userView = lay.inflate(R.layout.pop_user, null);
            userView.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_users_bg));
            bt_check = (Button) userView.findViewById(R.id.bt_check_pop);
            bt_cancel = (Button) userView.findViewById(R.id.bt_check_cancel_pop);
            bt_search = (Button)userView.findViewById(R.id.bt_check_search_pop);
            list_user = (ListView) userView.findViewById(R.id.list_user_pop);
            bt_search.setVisibility(View.VISIBLE);
            userCheckAdapter = new UserCheckPopAdapter(mContext,userList);
            list_user.setAdapter(userCheckAdapter);
            window = new PopupWindow(userView, 800,600);
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_users_bg));
        }
        list_user.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                if (user.isCheck()){
                    user.setCheck(false);
                }else{
                    user.setCheck(true);
                }
                userList.set(i,user);
                userCheckAdapter.notifyDataSetChanged(userList);
            }

        });

        bt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupWindow();
            }
        });
        bt_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedList.clear();
                for (int i=0;i<userList.size();i++){
                    if (userList.get(i).isCheck()){
                        checkedList.add(userList.get(i));
                    }
                }
                if(checkedList.size()>0){
                    String tblStudyDataId = String.valueOf(resultTree.getId());
                    MyWebWraper.getInstance().uploadUserList(mContext,tblStudyDataId,getCheckList(),cb_uploadUserList);// 获取部门列表
                }else{
                    Toast.makeText(mContext, "您还没有选择组员", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tblStudyDataId = String.valueOf(resultTree.getId());
                Intent intent = new Intent(getActivity(),UserCheckAddActivity.class);
                intent.putExtra("tblStudyDataId",tblStudyDataId);
                startActivityForResult(intent,1);
            }
        });
        //使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。
        //如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。
        //listview的话就没有了作用。
        window.setFocusable(true);//如果不设置setFocusable为true，popupwindow里面是获取不到焦点的，那么如果popupwindow里面有输入框等的话就无法输入。
        window.setOutsideTouchable(false);
        window.update();
    }


    private void initTypeInfo(){
        typeList.clear();
        CheckBean checkBean1 = new CheckBean();
        checkBean1.setName("文件名");
        checkBean1.setCheck(true);
        CheckBean checkBean2 = new CheckBean();
        checkBean2.setName("文号");
        CheckBean checkBean3 = new CheckBean();
        checkBean3.setName("企标号");
        CheckBean checkBean4 = new CheckBean();
        checkBean4.setName("全文搜索");
        typeList.add(checkBean1);
        typeList.add(checkBean2);
        typeList.add(checkBean3);
//        typeList.add(checkBean4);
    }

    /**
     * 设置搜索类型数据
     */
    private void initSearchTypeInfo(){
        searchList = new ArrayList<CheckBean>();
        CheckBean checkBean1 = new CheckBean();
        checkBean1.setName("全局搜索");
        checkBean1.setCheck(true);
        CheckBean checkBean2 = new CheckBean();
        checkBean2.setName("局部搜索");
        searchList.add(checkBean1);
        searchList.add(checkBean2);
    }


    /**
     * 类别选择框
     */
    private void initType(){
        //TODO
        if (typeWindow == null){
            LayoutInflater lay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = lay.inflate(R.layout.dialog_type, null);
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_type_bg));
            list_user2 = (ListView) v.findViewById(R.id.lv_dialog);
            typeAdapter = new TypeAdapter(mContext,typeList);
            list_user2.setAdapter(typeAdapter);
            typeWindow = new PopupWindow(v, 300,300);
            typeWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_type_bg));
        }
        list_user2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeCount = i;
                for (int j = 0;j<typeList.size();j++){
                    if (j == i){
                        CheckBean checkBean = typeList.get(i);
                        checkBean.setCheck(true);
                        typeList.set(i,checkBean);
                        bt_type.setText(checkBean.getName());
                    }else{
                        CheckBean checkBean = typeList.get(j);
                        checkBean.setCheck(false);
                        typeList.set(j,checkBean);
                    }
                }
                typeAdapter.notifyDataSetChanged();
                typeWindow.dismiss();
            }

        });

        //使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。
        //如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。
        //listview的话就没有了作用。
        typeWindow.setFocusable(true);//如果不设置setFocusable为true，popupwindow里面是获取不到焦点的，那么如果popupwindow里面有输入框等的话就无法输入。
        typeWindow.setOutsideTouchable(false);
        typeWindow.update();
    }



    /**
     * 查询类别选择框
     */
    private void initSearchType(){
        //TODO
        if (searchTypeWindow == null){
            LayoutInflater lay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = lay.inflate(R.layout.dialog_type, null);

            list_user3 = (ListView) v.findViewById(R.id.lv_dialog);
            searchAdapter = new TypeAdapter(mContext,searchList);
            list_user3.setAdapter(searchAdapter);
            searchTypeWindow = new PopupWindow(v, 300,200);
            searchTypeWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_message_bg));
        }
        list_user3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                allSearch = i;
                for (int j = 0;j<searchList.size();j++){
                    if (j == i){
                        CheckBean checkBean = searchList.get(i);
                        checkBean.setCheck(true);
                        searchList.set(i,checkBean);
                    }else{
                        CheckBean checkBean = searchList.get(j);
                        checkBean.setCheck(false);
                        searchList.set(j,checkBean);
                    }
                }
                searchAdapter.notifyDataSetChanged();
                searchTypeWindow.dismiss();
            }

        });

        //使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。
        //如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。
        //listview的话就没有了作用。
        searchTypeWindow.setFocusable(true);//如果不设置setFocusable为true，popupwindow里面是获取不到焦点的，那么如果popupwindow里面有输入框等的话就无法输入。
        searchTypeWindow.setOutsideTouchable(false);
        searchTypeWindow.update();
    }




    private void closePopupWindow(){
        if (window != null && window.isShowing()) {
            window.dismiss();
//            popupWindow = null;
            WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
            params.alpha=1f;
            getActivity().getWindow().setAttributes(params);
        }
    }

    private void startPopupWindow(TreeVo vo,View view,int position){
        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
        params.alpha=1f;
        getActivity().getWindow().setAttributes(params);
        resultTree = vo;
        getUserData(String.valueOf(resultTree.getId()));
        if (position<4){
            userView.setBackground(getActivity().getResources().getDrawable(R.drawable.dialog_users_bg_up));
            window.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.dialog_users_bg_up));
        }else{
            userView.setBackground(getActivity().getResources().getDrawable(R.drawable.dialog_users_bg));
            window.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.dialog_users_bg));
        }
        userCheckAdapter.notifyDataSetChanged(userList);
        window.showAsDropDown(view,200,0);
//        window.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
    }


    private void startTypePopupWindow(){
        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
        params.alpha=1f;
        getActivity().getWindow().setAttributes(params);
//        typeWindow.showAtLocation(bt_type, Gravity.CENTER_VERTICAL, 0, 0);
        typeWindow.showAsDropDown(iv_search_type,-60,0);
    }

    private void startSearchTypePopupWindow(){
        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
        params.alpha=1f;
        getActivity().getWindow().setAttributes(params);
//        typeWindow.showAtLocation(bt_type, Gravity.CENTER_VERTICAL, 0, 0);
        searchTypeWindow.showAsDropDown(iv_searchtype,-200,20);
    }


    /**
     * 获取所有已选择的成员名单
     * @return
     */
    private String getCheckList(){
        String resultStr;
        List<UserUpload> result = new ArrayList<UserUpload>();
        for (int i=0;i<checkedList.size();i++){
            if (checkedList.get(i).isCheck()){
                UserUpload upload = new UserUpload();
                upload.setId(checkedList.get(i).getCode());
                result.add(upload);
            }
        }
        resultStr = gson.toJson(result);
        return resultStr;
    }


    /**
     * 提交组员信息
     */
    private HttpResultCallBack cb_uploadUserList = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            // MyWebWraper.getInstance().getFileList(mContext, cb);
            String result = String.valueOf(objs[0]);
            if (result!=null&&!result.equals("")&&!result.contains("exception")) {
                Logger.json(result);
                List<User> checkList = new ArrayList<>();
                for (int i=0;i<userList.size();i++){
                    if (userList.get(i).isCheck()){
                        checkList.add(userList.get(i));
                    }
                }
                List<UserHistoryVo> historyVos = getListUser(checkList);
                try {
                    db.delete(UserHistoryVo.class, WhereBuilder.b("userId","=",userId).and("tblStudyDataId","=",resultTree.getId()));
                    for (int i=0;i<historyVos.size();i++){
                        UserHistoryVo userHistoryVo = historyVos.get(i);
                        db.save(userHistoryVo);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(12);
            }
        }
    };



    /**
     * 该文件是否分配阅读
     */
    HttpResultCallBack cb_fileIsCheck = new HttpResultCallBack() {
        @Override
        public void onResultCallBack(Object... objs) {
            String result = String.valueOf(objs[0]);
            if (!result.isEmpty()){
                if (!result.contains("exception")) {
                    Logger.json(result);
                    Type type = new TypeToken<List<CheckFileVo>>() {
                    }.getType();
                    List<CheckFileVo> list = new ArrayList<CheckFileVo>();
                    list = gson.fromJson(result, type);
                    if (list == null || list.size() <= 0) {
                        mHandler.sendEmptyMessage(9);
                    } else {
                        Message msg = new Message();
                        msg.what = 8;
                        msg.obj = list;
                        mHandler.sendMessage(msg);
                    }
                }
            }else{
                mHandler.sendEmptyMessage(9);
            }

        }
    };

    /**
     * 文件下载弹框
     */
    private void initMessageDialog(final TreeVo vo,final int position,String title){
        if (dialog == null){
            // 创建构建器
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            // 设置参数
            builder.setTitle(title);
            builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    // TODO Auto-generated method stub
                    downFile(dialogVo,dialogPosition);
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


    /**
     * 下载进度条
     */
    public void initDialog() {
        if (mDialog == null){
            mDialog = new CommonProgressDialog(getActivity());
            mDialog.setMessage("正在下载");
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setMax(100);
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.setOnOkAndCancelListener(this);
        }

    }

    @Override
    public void onCancel(View v) {
        mDialog.dismiss();
        isCancel = true;
    }


    public void setBack(){
        // clear map_tab_order、map_tab、layout_guide
        isSearched = false;

        Iterator iterator = map_tab.keySet().iterator();
        while (iterator.hasNext()) {
            String tabName_temp = iterator.next().toString();
            layout_guide.removeView(map_tab.get(tabName_temp));
            iterator.remove();
            map_tab.remove(tabName_temp);
        }
        titleVo = null;
        map_tab_order.clear();

        // hide guidebar
        hScroll_guide.setVisibility(View.GONE);
        // hide gridview_sec
        gridView_sec.setVisibility(View.GONE);
        // hide listview
        listView.setVisibility(View.GONE);
        // show gridview
        sv_index.setVisibility(View.VISIBLE);
       getBg(true);
    }

    /**
     * 是否退回到首页
     * @return
     */
    public static boolean isBack(){
        boolean flag = true;
        if (sv_index.getVisibility() == 0){
            flag = true;
        }else{
            flag = false;
        }
        return flag;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //TODO now visible to user
            initUsers();
            initUserPop();
//            userCheckAdapter = new UserCheckPopAdapter(mContext,userList);
//            list_user.setAdapter(userCheckAdapter);
        } else {
            //TODO now invisible to user
        }
    }

    /**
     * 是否是首页
     */
    private void getBg(boolean flag){
        if (flag){
            ll_main_bg.setBackground(getActivity().getResources().getDrawable(R.drawable.index_bg_new));
        }else{
            ll_main_bg.setBackgroundColor(Color.WHITE);
        }

    }



    /**
     * 判断是否之前点击过
     * @param data
     */
    private void getUserData(String data){
        if (listUsers!=null){
            listUsers.clear();
        }
        try {
            listUsers = db.selector(UserHistoryVo.class).where("userId","=",userId).and("tblStudyDataId","=",data).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listUsers!=null&&listUsers.size()>0){
            for (int i=0;i<userList.size();i++){
                User user = userList.get(i);
                user.setCheck(false);
                userList.set(i,user);
            }
            for (int i=0;i< userList.size();i++){
                UserHistoryVo historyVo = null;
                try {
                    historyVo = db.selector(UserHistoryVo.class).where("userId","=",userId).and("tblStudyDataId","=",data).and("code","=", userList.get(i).getCode()).findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (historyVo!=null){
                    User user = userList.get(i);
                    user.setCheck(historyVo.isCheck());
                    userList.set(i,user);
                }
            }
        }else{
            if (userList!=null){
                userList.clear();
            }
            try {
                userListData = db.selector(User.class).findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            userList = userListData;
        }
        List<User> checkOffUserList = new ArrayList<>();
        List<User> checkOnUserList = new ArrayList<>();
        for (int i=0;i<userList.size();i++){
            if (!userList.get(i).isCheck()){
                checkOffUserList.add(userList.get(i));
            }else{
                checkOnUserList.add(userList.get(i));
            }
        }
        userList.clear();
        userList.addAll(checkOnUserList);
        userList.addAll(checkOffUserList);
    }

    /**
     * 历史人员转化为user
     * @param list
     * @return
     */
    private List<UserHistoryVo> getListUser(List<User> list){
        List<UserHistoryVo> result = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            UserHistoryVo user = new UserHistoryVo();
//			user.setId(list.get(i).getId());
            user.setCode(list.get(i).getCode());
            user.setName(list.get(i).getName());
            user.setUserId(userId);
            user.setCheck(list.get(i).isCheck());
            user.setTblStudyDataId(String.valueOf(resultTree.getId()));
            result.add(user);
        }
        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            switch (resultCode){
                case 1:
                    UserListCheckedBean checkedBean = (UserListCheckedBean) data.getSerializableExtra("data");
                    List<User> dataList = checkedBean.getUserList();
                    for (int i=0;i<userList.size();i++){
                        User dataUser = userList.get(i);
                        for (int j=0;j<dataList.size();j++){
                            if (dataUser.getCode().equals(dataList.get(j).getCode())){
                                dataUser.setCheck(true);
                                userList.set(i,dataUser);
                                break;
                            }
                        }
                    }
                    List<User> checkOffUserList = new ArrayList<>();
                    List<User> checkOnUserList = new ArrayList<>();
                    for (int i=0;i<userList.size();i++){
                        if (!userList.get(i).isCheck()){
                            checkOffUserList.add(userList.get(i));
                        }else{
                            checkOnUserList.add(userList.get(i));
                        }
                    }
                    userList.clear();
                    userList.addAll(checkOnUserList);
                    userList.addAll(checkOffUserList);
                    userCheckAdapter.notifyDataSetChanged(userList);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
