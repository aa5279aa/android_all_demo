package com.xt.client.activitys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class WCDBActivity extends BaseActivity implements View.OnClickListener {

    SQLiteDatabase db;
    String tableName = "table_data1";
    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Decode/db/decoder.db";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteOpenHelper helper = new SQLiteOpenHelper(this, filePath, null, 80) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        db = helper.getWritableDatabase();

        viewHolder.getLine2().setVisibility(View.VISIBLE);

        viewHolder.getButton1().setText("建表");
        viewHolder.getButton2().setText("查询");
        viewHolder.getButton3().setText("插入1W条数据");
        viewHolder.getButton4().setText("清空所有数据");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == viewHolder.getButton1()) {
            db.execSQL("create table " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, path varchar,name varchar, content varchar)");
            show("建表成功:" + tableName);
            return;
        }
        if (v == viewHolder.getButton2()) {
            show("start");
            long startTime = System.currentTimeMillis();
            List<String> list = new ArrayList<>();
            Cursor cursor = db
                    .rawQuery(
                            "select * from " + tableName, new String[]{});// 根据编号查找，并存储到Cursor类中
            while (cursor.moveToNext()) {
                int type = cursor.getType(0);
                if (type == 0) {
                    list.add("");
                } else if (type == 1) {
                    int positin = cursor.getInt(0);
                    list.add(String.valueOf(positin));
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append("spendTime:" + (System.currentTimeMillis() - startTime));
            builder.append(",list.size:" + list.size());
            builder.append(",DB大小：" + new File(filePath).length() / 1024 / 1024 + "M");
            show(builder.toString());
            return;
        }
        if (v == viewHolder.getButton3()) {
            insertOneRecord(10000);
            return;
        }
        if (v == viewHolder.getButton4()) {
            db.execSQL("drop table " + tableName);
            return;
        }
    }


    private void insertOneRecord(final int time) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long startTime = System.currentTimeMillis();
                for (int i = 0; i < time; i++) {
                    String[] strings = new String[3];
                    strings[0] = "/sdcard/Decode/sql/0vrTjh+9mV3nx5sKdgRQhw==/IzLHQ0816R0=/93MUXHSW2uk=";
                    strings[0] = "wegwegwegweg";
                    strings[0] = "CRSF\u0000\u0000\u0002\u0000�\u0000\u0000\u0000,\u0004\u0000\u0000��\u0018��\u007F�#b5\u001C��\u0013pD*�Lw U,E�#�p0\u0003�\u0007��/\u0002����\u0001�ea!�E��u���*�D0\u007F�\bX�Է\u000F\u0013���\u000E\u001F\u0011�\\�K�\n" +
                            "���������\f`婨\u0000G\u007FHy���rG�ظgX��}=�*r\uEE03�\u001E\u0010�;#���p�)^�~�\b�+�ԏ+��rb���b�28v��\u0019��۴K|H���h�\u0015��-C��&����\t\u001B�\u0003��@\u0003��\u00006��8`�p\u0015����{e���e�\u0002���\u0015��ˀɱ�!����m��VB\n" +
                            "7E�A�y����$Y��*p��P���4#c�\u007Ft\t����R{���0�j�7�\u0018,��\u000E�V#��\u001AE�S�|\u001Bt��y\f\u0000��R`�<#7����\u000FK�2��#۵1�\u007F�\f\\�z���\u000F�\u0005�͗�4U)f//;�J$ V!O����ă�g\n" +
                            "��\u0014T\u0012�&麊\u0013�\u0004,\u001E�@\u0005\n" +
                            "��\u0010�oa\n" +
                            "\u000Fk\u001B\u0007s��U\u001FF�G�\u0014�e8�\u0007�\"�\u0011�U9\u052D��MG͊BB@��\u00124��,�(7튈,�.p�j�M\u000Fd\u0019�\u001C�P7/.=�d�ũt�\n" +
                            "&,x�8���'����\u0013d>\u001DJ<�.�\u0017\b����ƭ�ٰqD蠥�H+!*8�D#���c���R6Ķ�Ա\u0000Q*|�\u001E���\u001B�Am�/j-ۗ�η�w�ch��U'X�\u007F�\u001D\u000E\u0006�\u00061�\u001D�Nf\u0004LEL�x��V�)��k�\u001C���\u0012�b\u0019��\"c�G�պ9\u0015��xb�\u000F���{H��|~���\u0013*\u0005d\u0001\u0011��$٬\u0004�&�o=\u0005%4AJ�\u0005j�\u001C��x\u0018\u0014�P?T��ܚ��Pb���U������ʗ_^�B�/A����҃�3�\u0006�\u000B$̈\u0001\"#[��~��&Z����Uzsh����tZ�4j\n" +
                            "�gl��O��*@?�\u0000����'\u001A�AΑ�p�\n" +
                            "���\u001A5�\u0019TO�j\u0016\u0006�Қ:s�7��$&Wc�\u0016'ذ\u000F\t�^v�1T�D\b�a]y\u0014���o!�]��sq(L�}P7P�M�̞��U\u000B?\"gI�<^�_�k�g��?�\u0002U2L\uE127�7*�����7�zy\u001C\u0016�]\u0017��@z�$ls2(�9��6r�X�\u0015}\u0018f?\f�+���\u001F����\u0004Z��0\u0000�D�8L���ui��Ѳ�~�z52�0=��9�\u008D���&s]e�NI���>/��++�l\u0007�\u0012��z�-z��8�)\u007F�r�=;�>>e���\u0003�\u001B[�ȕ�P�&��p��nh�!y,�. ��\n" +
                            "������\u001B\u0012�^� W��ۿp�CH+'���\"�1؎�ޅ�\u001E���Ж�薅\u0004�\t\u0016�2A�V����N�4>��ww�|�.f�\u001F�Q�e��y\u001DdN�K::�\u0003�9&��Y3��`��Aq�s�\u001AO�1F5\u0017���-�M���\u0012���|~U��\u000E�\u001Cb\u0018\u0005\u0007�Hf���\u0010;u��";
                    db.execSQL("insert into " + tableName + "  (path,name,content) values (?,?,?)", strings);
                }
                show(tableName + " 插入成功,spendTime:" + (System.currentTimeMillis() - startTime));
            }
        }).start();
    }

    private void show(String content) {
        getHanlder().post(() -> {
            viewHolder.getResultText().setText(content);
        });
        Log.i("lxltest", content);
    }

}
