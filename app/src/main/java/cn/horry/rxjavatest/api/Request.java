package cn.horry.rxjavatest.api;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;

import java.util.Map;
import cn.horry.rxjavatest.utils.MyHttpParams;

/**
 * Created by Administrator on 2016/3/12.
 */
public class Request {
    private KJHttp.Builder builder = new KJHttp.Builder();
    private HttpCallBack httpCallBack=new HttpCallBack() {
        @Override
        public void onPreStart() {
            super.onPreStart();
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            if(apiListener!=null)
            {
                apiListener.result(t);
            }
        }

        @Override
        public void onSuccess(Map<String, String> headers, byte[] t) {
            super.onSuccess(headers, t);
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
            if(apiListener!=null)
            {
                apiListener.result(errorNo,strMsg);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    };
    private static Request _Request;
    private APIListener apiListener;

    /**
     * 私有构造方法
     */
    private Request(){}

    /**
     * 单例模式
     * @return
     */
    public static Request getInstance(){
        if(_Request ==null)
        {
            synchronized (Request.class)
            {
                _Request = new Request();
            }
        }
        return _Request;
    }

    private void post(MyHttpParams params,String url,Boolean UserCache){
        builder.params(params).url(url).httpMethod(org.kymjs.kjframe.http.Request.HttpMethod.POST).useCache(UserCache).callback(httpCallBack).request();
    }

    private void get(MyHttpParams params,String url,Boolean UserCache){
        builder.params(params).url(url).httpMethod(org.kymjs.kjframe.http.Request.HttpMethod.GET).useCache(UserCache).callback(httpCallBack).request();
    }

    public APIListener getApiListener() {
        return apiListener;
    }

    public void setApiListener(APIListener apiListener) {
        this.apiListener = apiListener;
    }
}
