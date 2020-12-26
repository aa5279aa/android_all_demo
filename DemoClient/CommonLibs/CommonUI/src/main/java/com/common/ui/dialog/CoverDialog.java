package com.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
public class CoverDialog extends Dialog implements View.OnClickListener {

    private LinearLayout mLayout;
    private RelativeLayout mRoot;
    private LinearLayout mButtonContainer;

    private ClickCallBack mCallBack;


    public CoverDialog(Context context) {
        super(context, R.style.NativeInsertDialog);
        initView();
    }


    private void initView() {
        mRoot = (RelativeLayout) View.inflate(getContext(), R.layout.common_dialog_cover_layout, null);
        mButtonContainer = mRoot.findViewById(R.id.button_container);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(mRoot, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setButtonClickBack(ClickCallBack callBack) {
        mCallBack = callBack;
    }


    public void setContent(String desc, String button) {
        DialogModel model = new DialogModel();
        model.desc = desc;
        if (!StringUtil.emptyOrNull(button)) {
            model.addButton(button);
        }
        bindData(model);
    }

    public void bindData(DialogModel dialogModel) {
        mButtonContainer.removeAllViews();

        TextView title = ViewUtil.requestView(mRoot, R.id.common_dialog_title);
        TextView desc = ViewUtil.requestView(mRoot, R.id.common_dialog_content);
        UIShowUtil.showText(title, dialogModel.title);
        UIShowUtil.showText(desc, dialogModel.desc);
        ImageView view = ViewUtil.requestView(mRoot, R.id.common_dialog_icon);
        UIShowUtil.showImageView(view, dialogModel.iconResourceId);
        if (dialogModel.buttonList.size() == 0) {
            mButtonContainer.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < dialogModel.buttonList.size(); i++) {
                TextView button = createButton(dialogModel.buttonList.get(i), i);
                mButtonContainer.addView(button);
            }
            mButtonContainer.setVisibility(View.VISIBLE);
        }
        if (dialogModel.isLoading) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                    R.anim.anim_loading);
            view.startAnimation(animation);
        }
    }

    private TextView createButton(String text, int position) {
        TextView button = new TextView(getContext());
        button.setTextAppearance(getContext(), R.style.text_17_017afe);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1f;
        button.setLayoutParams(lp);
        button.setTag(position);
        button.setOnClickListener(this);
        return button;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (mCallBack == null || !(tag instanceof Integer)) {
            return;
        }
        int position = (int) tag;
        mCallBack.clickButton(position, v);
    }

    public interface ClickCallBack {
        void clickButton(int position, View view);
    }

    @Override
    public void show() {
        super.show();
    }

    static public class DialogModel {
        List<String> buttonList = new ArrayList<>();
        CharSequence title;
        CharSequence desc;
        int iconResourceId;
        boolean isLoading;

        public void addButton(String... str) {
            buttonList.addAll(Arrays.asList(str));
        }


        public void setDesc(CharSequence desc1) {
            desc = desc1;
        }

        public void setTitle(CharSequence title1) {
            title = title1;
        }

        public void setIconResourceId(int iconResourceId1) {
            iconResourceId = iconResourceId1;
        }

        public void setIsLoading(boolean isLoading1) {
            isLoading = isLoading1;
        }
    }
}
