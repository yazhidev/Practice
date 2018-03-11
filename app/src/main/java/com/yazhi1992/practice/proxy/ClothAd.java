package com.yazhi1992.practice.proxy;

import android.util.Log;

/**
 * Created by zengyazhi on 2018/3/11.
 */

public class ClothAd implements AD {
    @Override
    public void slogan(String word) {
        Log.e("zyz", "衣服" + word);
    }

    @Override
    public int getPrice() {
        return 20;
    }
}
