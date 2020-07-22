package com.ineasnet.shanghaiguidao2.entity.bean;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import java.io.Serializable;
import java.util.List;

/**版本号变动list
 * Created by Administrator on 2017/6/29.
 */

public class ListVoBean implements Serializable{
    private List<TreeVo> list;

    public List<TreeVo> getList() {
        return list;
    }

    public void setList(List<TreeVo> list) {
        this.list = list;
    }
}
