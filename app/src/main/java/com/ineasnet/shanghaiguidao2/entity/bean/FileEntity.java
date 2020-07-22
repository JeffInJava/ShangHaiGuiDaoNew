package com.ineasnet.shanghaiguidao2.entity.bean;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import java.io.Serializable;
import java.util.List;

/**
 * 文件列表
 * Created by Administrator on 2017/5/19.
 */

public class FileEntity implements Serializable{


    /**
     * page : 1
     * pageSize : 10
     * pages : 20
     * rows : [{"createTime":1495077371000,"createTimeShow":"2017-05-18 11:16:11","docID":"20170518111606_d2666f29-e2b2-4894-b1f5-42311f7c8b8f","docName":"20170518111606_d2666f29-e2b2-4894-b1f5-42311f7c8b8f.docx","folderName":"test01","hisStatus":1,"id":201,"pageCount":2,"pdfName":"20170518111606_d2666f29-e2b2-4894-b1f5-42311f7c8b8f.pdf","sendRange":"运营业务部","sendRangeCode":"08","title":"20161226会议记录"},{"createTime":1494826942000,"createTimeShow":"2017-05-15 13:42:22","docID":"20170515134216_7b090ef4-d661-401e-be49-f8144473ea57","docName":"20170515134216_7b090ef4-d661-401e-be49-f8144473ea57.docx","folderName":"test02","hisStatus":1,"id":200,"pageCount":1,"pdfName":"20170515134216_7b090ef4-d661-401e-be49-f8144473ea57.pdf","sendRange":"运营业务部","sendRangeCode":"08","title":"2017-04-01服务区维护记录"},{"createTime":1494479468000,"createTimeShow":"2017-05-11 13:11:08","docID":"20170511131035_ce74ba92-c8e5-4ab4-847f-2cffa72dfae6","docName":"20170511131035_ce74ba92-c8e5-4ab4-847f-2cffa72dfae6.pdf","folderName":"test01","hisStatus":1,"id":199,"pageCount":120,"pdfName":"20170511131035_ce74ba92-c8e5-4ab4-847f-2cffa72dfae6.pdf","sendRange":"运营业务部","sendRangeCode":"08","title":"2016年公路路况分析报告"},{"createTime":1494479254000,"createTimeShow":"2017-05-11 13:07:34","docID":"20170511130733_0835db73-6908-461c-9936-be121a5f95d0","docName":"20170511130733_0835db73-6908-461c-9936-be121a5f95d0.pdf","folderName":"test","hisStatus":1,"id":198,"pageCount":120,"pdfName":"20170511130733_0835db73-6908-461c-9936-be121a5f95d0.pdf","sendRange":"运营业务部","sendRangeCode":"08","title":"2016年公路路况分析报告"},{"createTime":1494479068000,"createTimeShow":"2017-05-11 13:04:28","docID":"20170511130423_14e63839-fb36-4899-bde2-34adf43ac1c2","docName":"20170511130423_14e63839-fb36-4899-bde2-34adf43ac1c2.docx","folderName":"test01","hisStatus":1,"id":197,"pageCount":2,"pdfName":"20170511130423_14e63839-fb36-4899-bde2-34adf43ac1c2.pdf","sendRange":"运营业务部","sendRangeCode":"08","title":"20161226会议记录"},{"createTime":1469436650000,"createTimeShow":"2016-07-25 16:50:50","docID":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268","docName":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268.pdf","folderName":"test03-1","hisStatus":1,"id":193,"pageCount":5,"pdfName":"6f2413b1-00a2-4b36-acf0-f36bd5bd0011.pdf","title":"附件8_null"},{"createTime":1469436650000,"createTimeShow":"2016-07-25 16:50:50","docID":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268","docName":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268.pdf","folderName":"test03-1","hisStatus":1,"id":194,"pageCount":5,"pdfName":"96f4277b-a232-40d9-8d0a-acbbfbc52dff.pdf","title":"附件9_null"},{"createTime":1469436650000,"createTimeShow":"2016-07-25 16:50:50","docID":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268","docName":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268.pdf","folderName":"test03-1","hisStatus":1,"id":195,"pageCount":5,"pdfName":"3a360518-cf39-45f5-906c-949e72bbe5b8.pdf","title":"附件10_null"},{"createTime":1469436650000,"createTimeShow":"2016-07-25 16:50:50","docID":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268","docName":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268.pdf","folderName":"test03-1","hisStatus":1,"id":196,"pageCount":5,"pdfName":"f81ea73c-9672-4410-a706-c85843e2812f.pdf","title":"附件11_null"},{"createTime":1469436649000,"createTimeShow":"2016-07-25 16:50:49","docID":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268","docName":"20160725165044_7f28537a-d82b-444c-94e1-7da07c3a1268.pdf","folderName":"test03-1","hisStatus":1,"id":190,"pageCount":5,"pdfName":"a36ff5b6-3248-484d-b0f6-64885bb60d68.pdf","title":"附件5_null"}]
     * total : 194
     */

    private int page;
    private int pageSize;
    private int pages;
    private int total;
    private List<TreeVo> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TreeVo> getRows() {
        return rows;
    }

    public void setRows(List<TreeVo> rows) {
        this.rows = rows;
    }
}
