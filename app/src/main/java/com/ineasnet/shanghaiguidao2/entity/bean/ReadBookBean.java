package com.ineasnet.shanghaiguidao2.entity.bean;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class ReadBookBean implements Serializable{

    /**
     * page : 1
     * pageSize : 10
     * pages : 1
     * rows : [{"docName":"93B01775820646CBA51A70B571E7D4CC.pdf","id":2906,"pdfName":"cd3cb767-fb6b-45bf-8f8f-e28b5fbda239.pdf","title":"附件1_沪地铁运〔2017〕212号附件"},{"docName":"F67F582FA2EC4586A739B31CB07DCA0A.pdf","id":2908,"pdfName":"F67F582FA2EC4586A739B31CB07DCA0A.pdf","title":"31上海轨道交通3号线列车驾驶模式变更作业指导书"},{"docName":"93B01775820646CBA51A70B571E7D4CC.pdf","id":2905,"pdfName":"93B01775820646CBA51A70B571E7D4CC.pdf","title":"上海申通地铁集团有限公司反恐防控应急处置专项预案"},{"docName":"FF08583CE9C34401AD10654E36144A04.pdf","id":2909,"pdfName":"FF08583CE9C34401AD10654E36144A04.pdf","title":"36上海轨道交通3号线列车人工限制向前运行作业指导书"},{"docName":"600D8459191D40358CABABE7B9F786C5.pdf","id":2904,"pdfName":"600D8459191D40358CABABE7B9F786C5.pdf","title":"19上海轨道交通1号线跨车场及正线轨行区施工登记、注销作业指导书"}]
     * total : 5
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
