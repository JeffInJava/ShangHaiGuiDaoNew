package com.ineasnet.shanghaiguidao2.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 文件信息
 * Created by Administrator on 2017/5/19.
 */
@Table(name = "file")
public class FileDao {
    @Column(name = "id", isId = true , autoGen = true)
    private int id;
    @Column(name = "name")
    private String pdfName;
    @Column(name = "url")
    private String pdfNameUrl;
    @Column(name = "version")
    private int version;
    @Column(name = "pagecount")
    private int pageCount;
    @Column(name = "page")
    private int page;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getPdfNameUrl() {
        return pdfNameUrl;
    }

    public void setPdfNameUrl(String pdfNameUrl) {
        this.pdfNameUrl = pdfNameUrl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    @Override
    public String toString() {
        return "FileDao{" +
                "id=" + id +
                ", pdfName='" + pdfName + '\'' +
                ", pdfNameUrl='" + pdfNameUrl + '\'' +
                ", version=" + version +
                ", pageCount=" + pageCount +
                ", page=" + page +
                '}';
    }
}
