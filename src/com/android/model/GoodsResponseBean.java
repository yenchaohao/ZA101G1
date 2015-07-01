package com.android.model;

import com.goods.model.*;
import java.util.List;

public class GoodsResponseBean {

    List<GoodsVO> goods;
    int allItemSize;

    public GoodsResponseBean(List<GoodsVO>goods, int allItemSize) {
        this.goods = goods;
        this.allItemSize = allItemSize;
    }

    public List<GoodsVO> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsVO> goods) {
        this.goods = goods;
    }

    public int getAllItemSize() {
        return allItemSize;
    }

    public void setAllItemSize(int allItemSize) {
        this.allItemSize = allItemSize;
    }
}

