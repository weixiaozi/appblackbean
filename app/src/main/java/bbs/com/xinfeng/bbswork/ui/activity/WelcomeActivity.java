package bbs.com.xinfeng.bbswork.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.domin.LoginBus;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static bbs.com.xinfeng.bbswork.base.Constant.isLogin;

public class WelcomeActivity extends Activity {

    private Disposable subscribe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_welcome);
        EventBus.getDefault().register(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        ScreenUtils.hideUI(this);
        subscribe = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Intent intent;
                if (SharedPrefUtil.getBoolean(isLogin, false)) {
                    if (SharedPrefUtil.getBoolean(Constant.profile_key, true)) {
                        intent = new Intent(WelcomeActivity.this, ModifyActivity.class);
                        intent.putExtra("from", "welcome");
                    } else {
                        intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        if (getIntent().getExtras() != null)
                            intent.putExtras(getIntent().getExtras());
                    }
                } else {
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    intent.putExtra("from", "welcome");
                }
                startActivity(intent);
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginBus bus) {
        if (!bus.isLoginSuccess) {
            if (subscribe != null)
                subscribe.dispose();
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            intent.putExtra("from", "welcome");
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
