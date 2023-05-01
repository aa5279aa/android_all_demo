package com.xt.client.activitys;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.xt.client.R;
import com.xt.client.util.ReflectUtil;


public class ThreadRefreshUIActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        AddShortCut(getApplicationContext(), ThreadRefreshUIActivity.class
                , ThreadRefreshUIActivity.class, 112, R.mipmap.ic_launcher);
    }

    public void AddShortCut(Context context, Class targetClass, Class backClass, int shortCutId, int shortCutIcon) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                Intent shortcutInfoIntent = new Intent(context, targetClass);
                shortcutInfoIntent.setAction(Intent.ACTION_VIEW);

                ShortcutInfo info = new ShortcutInfo.Builder(context, "id" + shortCutId)
                        .setIcon(Icon.createWithResource(context, shortCutIcon)).
                                setShortLabel("新的快捷").setIntent(shortcutInfoIntent).setActivity(null).build();
                try {
//                    info.setIconResourceId()
                    ReflectUtil.setPrivateField(info, "mIconResId", R.mipmap.ic_launcher);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, backClass), PendingIntent.FLAG_IMMUTABLE);
                shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());
            }
        } else {
            Toast.makeText(context, "设备不支持在桌面创建快捷图标！", Toast.LENGTH_LONG).show();
        }
    }


}
