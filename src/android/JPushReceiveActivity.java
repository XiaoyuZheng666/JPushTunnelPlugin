package com.tunnel.plugin;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.zhaoyin.hjx.MainActivity;
import com.zhaoyin.hjx.R;

import com.zhaoyin.testhjx.MainActivity;
import com.zhaoyin.testhjx.R;

import com.zhaoyin.devhjx.MainActivity;
import com.zhaoyin.devhjx.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiveActivity extends Activity {


    /**消息Id**/
    public static final String KEY_MSGID = "msg_id";

    /**该通知的下发通道**/
    private static final String KEY_WHICH_PUSH_SDK = "rom_type";

    /**通知标题**/
    public static final String KEY_TITLE = "n_title";

    /**通知内容**/
    public static final String KEY_CONTENT = "n_content";

    /**通知附加字段**/
    public static final String KEY_EXTRAS = "n_extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_receive);

        handleOpenClick();

    }

    private void handleOpenClick() {

        if (getIntent().getData() == null) return;



        String data = getIntent().getData().toString();
        if (TextUtils.isEmpty(data)) return;
        try {
            JSONObject jsonObject = new JSONObject(data);

            String msgId = jsonObject.optString(KEY_MSGID);
            byte whichPushSDK = (byte) jsonObject.optInt(KEY_WHICH_PUSH_SDK);

            String title = jsonObject.optString(KEY_TITLE);
            String content = jsonObject.optString(KEY_CONTENT);
            String extras = jsonObject.optString(KEY_EXTRAS);

            //上报点击事件
            JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);

            if (isExsitMianActivity(this,MainActivity.class)){
                //如果MainActivity存在的话直接发个通知就好了
                Intent intent  =new Intent("android.intent.action.noticeData");
                intent.putExtra(KEY_MSGID,msgId);
                intent.putExtra(KEY_TITLE,title);
                intent.putExtra(KEY_CONTENT,content);
                intent.putExtra(KEY_EXTRAS,extras);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                finish();

            }else {
                Intent noticeIntent = new Intent(this, MainActivity.class);
                noticeIntent.putExtra(KEY_MSGID,msgId);
                noticeIntent.putExtra(KEY_TITLE,title);
                noticeIntent.putExtra(KEY_CONTENT,content);
                noticeIntent.putExtra(KEY_EXTRAS,extras);
                startActivity(noticeIntent);

                finish();
            }
        } catch (JSONException e) {
        }
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    public static boolean isExsitMianActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context
                .getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break; // 跳出循环，优化效率
                }
            }
        }
        return flag;
    }



}
