package com.common.util;

import android.view.View;

public class ViewUtil {

    public static <T extends View> T requestView(View convertView, int id) {
        View view = null;
        if (convertView != null) {
            view = convertView.findViewById(id);
        }
        return (T) view;
    }




}
