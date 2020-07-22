package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.dao.FileDao;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;

/**
 * 文章列表
 * Created by Administrator on 2017/7/13.
 */

public class ArticleAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<TreeVo> childList;
    private MyApplication application;
    private DbManager db;
    private String name;
    private String path;
    List<FileDao> fileDaoList = new ArrayList<>();
    public ArticleAdapter(Context context, List<TreeVo> childList) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.childList = childList;
        this.mInflater = LayoutInflater.from(context);
        application = new MyApplication();
        db = x.getDb(application.getDaoConfig());
        try {
            fileDaoList=db.selector(FileDao.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        path = PropertiesUtil.getPropertiesURL(context, "filePath");
    }

    public void notifyDataSetChanged(List<TreeVo> list){
        childList.clear();
        if (list!=null&&list.size()>0){
            childList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (childList.size()>0){
            return childList.size();
        }else{
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_article, null);

//			holder.iv = (ImageView) convertView
//					.findViewById(R.id.list_item_img_arr);
//			holder.tv = (TextView) convertView.findViewById(R.id.list_item_tv);
//			holder.cb = (CheckBox) convertView
//					.findViewById(R.id.list_item_checkbox);
//			holder.bar = (ProgressBar) convertView
//					.findViewById(R.id.list_item_progress);
            holder.tv_article_id = (TextView) convertView.findViewById(R.id.tv_article_id);
            holder.tv_article_docNum = (TextView) convertView.findViewById(R.id.tv_article_docNum);
            holder.tv_article_name = (TextView) convertView.findViewById(R.id.tv_article_name);
            holder.tv_article_new = (TextView) convertView.findViewById(R.id.tv_article_new);
            holder.tv_article_null = (TextView) convertView.findViewById(R.id.tv_article_null);
            holder.tv_article_update = (TextView) convertView.findViewById(R.id.tv_article_update);
            holder.iv_article = (ImageView) convertView.findViewById(R.id.iv_article);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TreeVo vo = childList.get(position);
        String name  = "";
        if (vo.getTitle()!=null&&!vo.getTitle().equals("")){
            name = vo.getTitle();
        }else if (vo.getName()!=null&&!vo.getName().equals("")){
            name = vo.getName();
        }
        holder.tv_article_name.setText(name);
        holder.tv_article_id.setText("文号："+vo.getDocNum());
        holder.tv_article_docNum.setText("企标号："+vo.getRuleNum());

        String pdfName = vo.getPdfName() == null ? "" : vo.getPdfName();
        if(!TextUtils.isEmpty(pdfName)){
            File file = new File(path+"/ftpIndexAll/" + pdfName);
            boolean flag = false;
            if (file.exists()){
                flag = true;
            }else{
                flag = false;
            }


            if (!flag) {
                holder.tv_article_null.setTextColor(mContext.getResources().getColor(
                        R.color.green));
                holder.tv_article_new.setTextColor(mContext.getResources().getColor(
                        R.color.gray));
                holder.tv_article_update.setTextColor(mContext.getResources().getColor(
                        R.color.gray));
            } else {
                //是否为最新版
                if (vo.isNew()){
                    holder.tv_article_null.setTextColor(mContext.getResources().getColor(
                            R.color.gray));
                    holder.tv_article_new.setTextColor(mContext.getResources().getColor(
                            R.color.black));
                    holder.tv_article_update.setTextColor(mContext.getResources().getColor(
                            R.color.gray));
                }else{
                    holder.tv_article_null.setTextColor(mContext.getResources().getColor(
                            R.color.gray));
                    holder.tv_article_new.setTextColor(mContext.getResources().getColor(
                            R.color.gray));
                    holder.tv_article_update.setTextColor(mContext.getResources().getColor(
                            R.color.blue));
                }
            }
        }

        return convertView;
    }

    class ViewHolder {
        //文号
        TextView tv_article_id;
        //企标号
        TextView tv_article_docNum;
        //文件名
        TextView tv_article_name;
        //已下载
        TextView tv_article_new;
        //未下载
        TextView tv_article_null;
        //待更新
        TextView tv_article_update;
        //图标
        ImageView iv_article;

    }

}
