package com.common.ui.demoshow.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.ui.dialog.CoverDialog;
import com.common.util.UIShowUtil;

/**
 * Created by lxl on 2017/5/25.
 */
public class CommonUIDialogFragment extends CommonUIBaseFragment implements View.OnClickListener {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCommonDialog();
    }


    @Override
    public String getPageTitle() {
        return "UI浮层弹框样式库";
    }

    public void addCommonDialog() {
        TextView text = new TextView(getContext());
        text.setText("CommonCustomDialog");
        text.setTag("CommonCustomDialog");
        text.setOnClickListener(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        mContainer.addView(text, lp);
        addLine();
    }

    private void showCommonDialog() {
        CoverDialog dialog = new CoverDialog(getContext());
        CoverDialog.DialogModel dialogModel = new CoverDialog.DialogModel();
        dialogModel.addButton("确定", "取消");
        dialogModel.addButton("立即更新");
//            dialogModel.setTitle("title");
        dialogModel.setDesc("描述描述描述描述描述描述");
//            dialogModel.setIconResourceId(R.drawable.tool_icon);
        dialog.setButtonClickBack((position, view) -> {
            UIShowUtil.showToast(getContext(), "position:" + position);
        });

        dialog.bindData(dialogModel);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if ("CommonCustomDialog".equals(v.getTag())) {
            showCommonDialog();
        }
    }
}
