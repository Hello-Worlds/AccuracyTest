package cn.lenovo.accuracytest.entity;

/**
 * 按键进度测试GridView 单个Item信息实体类
 * Created by chao on 2017/6/7.
 */
public class AccuracyGridItemInfo {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AccuracyGridItemInfo{" + "count=" + count + '}';
    }
}
