package com.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.ui.R;
import com.common.util.StringUtil;
import com.common.util.UIShowUtil;
import com.common.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用dialog
 */
public class CommonLoadingDialog extends Dialog {
    private RelativeLayout mRoot;
    private View.OnClickListener mCallBack;

    public CommonLoadingDialog(Context context) {
        super(context, R.style.NativeInsertDialog);
        initView();
    }


    private void initView() {
        mRoot = (RelativeLayout) View.inflate(getContext(), R.layout.common_dialog_loading_layout, null);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(mRoot, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setButtonClickBack(View.OnClickListener callBack) {
        mCallBack = callBack;
    }


    public void setContent(String button) {
        DialogModel model = new DialogModel();
        if (!StringUtil.emptyOrNull(button)) {
            model.addButton(button);
        }
        bindData(model);
    }

    public void bindData(DialogModel dialogModel) {
        TextView title = ViewUtil.requestView(mRoot, R.id.common_dialog_title);
        UIShowUtil.showText(title, dialogModel.title);
        ImageView view = ViewUtil.requestView(mRoot, R.id.common_dialog_icon);
        Button button = ViewUtil.requestView(mRoot, R.id.comfirm_button);

        UIShowUtil.showImageView(view, dialogModel.iconResourceId);
        Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.anim_loading);
        view.startAnimation(animation);
        if (mCallBack != null) {
            button.setOnClickListener(mCallBack);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    static public class DialogModel {
        List<String> buttonList = new ArrayList<>();
        CharSequence title;
        boolean isShowAnim;
        int iconResourceId;

        public void addButton(String... str) {
            buttonList.addAll(Arrays.asList(str));
        }


        public void setDesc(CharSequence title1) {
            title = title1;
        }

        public void setIconResourceId(int iconResourceId1) {
            iconResourceId = iconResourceId1;
        }

        public void setShowAnim(boolean showAnim) {
            isShowAnim = showAnim;
        }
    }
}
