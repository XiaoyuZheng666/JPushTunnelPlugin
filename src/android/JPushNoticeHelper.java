package com.tunnel.plugin;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

public class JPushNoticeHelper {

    public static Intent getPackedNoticeIntent(Intent intent){

        String msgId= intent.getStringExtra(JPushReceiveActivity.KEY_MSGID);
        String title = intent.getStringExtra(JPushReceiveActivity.KEY_TITLE);
        String content = intent.getStringExtra(JPushReceiveActivity.KEY_CONTENT);
        String extras = intent.getStringExtra(JPushReceiveActivity.KEY_EXTRAS);

        Intent openIntent  =new Intent(JPushInterface.ACTION_NOTIFICATION_OPENED);
        openIntent.putExtra(JPushInterface.EXTRA_TITLE,title);
        openIntent.putExtra(JPushInterface.EXTRA_MESSAGE,content);
        openIntent.putExtra(JPushInterface.EXTRA_ALERT,"ding");
        openIntent.putExtra(JPushInterface.EXTRA_MSG_ID,msgId);

        Bundle bundle = new Bundle();
        try {
            JSONObject jsonObject = new JSONObject(extras);

            Iterator<String> it = jsonObject.keys();
            while(it.hasNext()){
                // 获得key
                String key = it.next();
                String value = jsonObject.getString(key);

                openIntent.putExtra(key,value);
                bundle.putString(key,value);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        openIntent.putExtras(bundle);

        return  openIntent;

    }

}
