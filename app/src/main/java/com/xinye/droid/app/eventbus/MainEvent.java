package com.xinye.droid.app.eventbus;

import java.io.Serializable;

/**
 * MainActivity事件
 *
 * @author wangheng
 */

public class MainEvent implements Serializable {
    private static final long serialVersionUID = -2141535771374039206L;

    public static final int EVENT_TOKEN_TIME_OUT = 1;

    private int event;

    public static MainEvent create(int event){
        MainEvent me = new MainEvent();
        me.event = event;
        return me;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
