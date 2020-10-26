package com.app.erladmin.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erladmin.ERLApp;
import com.app.erladmin.R;
import com.app.erladmin.databinding.ActivitySplashBinding;
import com.app.erladmin.model.entity.info.FcmData;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.utilities.utils.StringHelper;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private int SPLASH_TIME_OUT = 2500;
    private Context mContext;
    private FcmData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;


//        Log.e("test","TimeZone1:"+ TimeZone.getDefault().getID());
//
//        Calendar cal = Calendar.getInstance();
//        TimeZone tz = cal.getTimeZone();
//        Log.e("test","TimeZone2"+tz.getID());
//        Log.e("test","TimeZone2"+tz.getDisplayName(true, TimeZone.SHORT));

        onCreateData();
    }

    public void callTimerCount() {
        new Handler().postDelayed(() -> {
            String userInfo = ERLApp.preferenceGetString(AppConstant.SharedPrefKey.USER_INFO, "");
            if (StringHelper.isEmpty(userInfo) || userInfo.equalsIgnoreCase("null")) {
                moveActivity(mContext, LoginActivity.class, true, true, null);
            } else {
                moveActivity(mContext, DashBoardActivity.class, true, true, null);
            }
        }, SPLASH_TIME_OUT);
    }

    public void onCreateData() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("type")) {
            String userInfo = ERLApp.preferenceGetString(AppConstant.SharedPrefKey.USER_INFO, "");
            if (!StringHelper.isEmpty(userInfo)) {
                moveToNotificationIntent();
            }
        } else {
            callTimerCount();
        }
    }

    public void moveToNotificationIntent() {
        try {
            Bundle bundle = getIntent().getExtras();
            data = new FcmData();
            data.setNotification_type(bundle.getString("notification_type"));
            data.setUser_id(bundle.getString("user_id"));
            data.setBody(bundle.getString("body"));
            data.setIcon(bundle.getString("icon"));
            data.setType(bundle.getString("type"));
            data.setTimestamp(bundle.getString("timestamp"));
            data.setSound(bundle.getString("sound"));
            data.setTitle(bundle.getString("title"));
        } catch (Exception e) {
        }

        Intent intent = null;
        if (AppUtils.getUserPrefrence(SplashActivity.this) == null) {
            intent = new Intent("com.app.erladmin.view.activity.LoginActivity");
        } else {
            intent = AppUtils.getFcmIntent(data);
            intent.putExtra(AppConstant.IntentKey.IS_FROM_NOTIFICATION, true);
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
