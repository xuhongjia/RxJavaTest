package cn.horry.rxjavatest.ui;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.I_KJActivity;
import org.kymjs.kjframe.ui.KJActivityStack;
import org.kymjs.kjframe.utils.KJLoger;

import java.lang.ref.SoftReference;

public class BaseActivity extends AppCompatActivity implements I_KJActivity , View.OnClickListener{

    public static final int WHICH_MSG = 0X37210;
    public Activity aty;
    private ThreadDataCallBack callback;
    private KJActivityHandle threadHandle = new KJActivityHandle(this);
    /**
     * Activity状态
     */
    public int activityState = DESTROY;

    private static class KJActivityHandle extends Handler {
        private final SoftReference<BaseActivity> mOuterInstance;

        KJActivityHandle(BaseActivity outer) {
            mOuterInstance = new SoftReference<>(outer);
        }

        // 当线程中初始化的数据初始化完成后，调用回调方法
        @Override
        public void handleMessage(android.os.Message msg) {
            BaseActivity aty = mOuterInstance.get();
            if (msg.what == WHICH_MSG && aty != null) {
                aty.callback.onSuccess();
            }
        }
    }
    /**
     * 一个私有回调类，线程中初始化数据完成后的回调
     */
    private interface ThreadDataCallBack {
        void onSuccess();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aty = this;
        KJActivityStack.create().addActivity(this);
        KJLoger.state(this.getClass().getName(), "---------onCreat ");
        setRootView(); // 必须放在annotate之前调用
        AnnotateUtil.initBindView(this);
        initializer();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        KJLoger.state(this.getClass().getName(), "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = RESUME;
        KJLoger.state(this.getClass().getName(), "---------onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = PAUSE;
        KJLoger.state(this.getClass().getName(), "---------onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = STOP;
        KJLoger.state(this.getClass().getName(), "---------onStop ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        KJLoger.state(this.getClass().getName(), "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        activityState = DESTROY;
        KJLoger.state(this.getClass().getName(), "---------onDestroy ");
        super.onDestroy();
        KJActivityStack.create().finishActivity(this);
        callback = null;
        threadHandle = null;
        aty = null;
    }

    // 仅仅是为了代码整洁点
    private void initializer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initDataFromThread();
                threadHandle.sendEmptyMessage(WHICH_MSG);
            }
        }).start();
        initData();
        initWidget();
    }

    @Override
    public void setRootView() {

    }

    /**
     * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
     */
    protected void threadDataInited() {
    }

    /**
     * 在线程中初始化数据，注意不能在这里执行UI操作
     */
    @Override
    public void initDataFromThread() {
        callback = new ThreadDataCallBack() {
            @Override
            public void onSuccess() {
                threadDataInited();
            }
        };
    }

    @Override
    public void initData() {
    }

    @Override
    public void initWidget() {
    }

    @Override
    public void widgetClick(View v) {

    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(int id, boolean click) {
        T view = (T) findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }
    @Override
    public void onClick(View v) {

    }
}
