package com.xinye.droid.app.bean;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * 返回指定类型的列表
 *
 * @author wangheng
 */
public class ResponseList<T extends Serializable> extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -1864903652474192155L;
    private ArrayList<T> list;

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}
