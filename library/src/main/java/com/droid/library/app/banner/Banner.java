package com.droid.library.app.banner;

import java.io.Serializable;

/**
 * Banner的数据结构.
 *
 * @author wangheng
 */
public class Banner implements Serializable {

    private static final long serialVersionUID = 4981938402094129713L;

    public static final int TYPE_FILTER_THEME = 1;
    public static final int TYPE_STICKER_THEME = 2;

    private String id;
    // banner的图片url
    private String src;
    private Serializable data;
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
