package cn.horry.rxjavatest.api;

/**
 * Created by Administrator on 2016/3/12.
 */
public interface APIListener {
    void result(String result);
    void result(int errorNo, String result);
}
