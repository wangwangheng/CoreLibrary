package com.droid.library.thirdplatform.gson;

import com.droid.library.thirdplatform.IThirdPlatformManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * GSON配置
 *
 * @author wangheng
 */
public class GsonManager implements IThirdPlatformManager {

    private Gson mGson;

    private GsonManager() {
        mGson = new GsonBuilder().create();
    }

    public static GsonManager getInstance() {
        return GsonManagerCreator.INSTANCE;
    }

    @Override
    public void init() {
    }

    public Gson getGsonDefault() {
        return mGson;
    }

    public Gson createGson() {
        return new GsonBuilder().create();
    }

    private static class GsonManagerCreator {
        private static final GsonManager INSTANCE = new GsonManager();
    }

}
