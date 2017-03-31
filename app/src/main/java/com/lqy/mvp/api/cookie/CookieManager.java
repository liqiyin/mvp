package com.lqy.mvp.api.cookie;

import com.lqy.mvp.MyApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * manager cookie automatically
 */

public class CookieManager implements CookieJar {
    private PersistentCookieStore cookieStore;

    public CookieManager() {
        cookieStore = new PersistentCookieStore(MyApplication.getInstance().getApplicationContext());
    }


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }
}
