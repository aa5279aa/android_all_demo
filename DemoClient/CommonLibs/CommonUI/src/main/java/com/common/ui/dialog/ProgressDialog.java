package com.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.ui.R;
import com.common.ui.view.widget.CommonRoundProgressBar;

/**
 * 带进度的dilaog
 */
public class ProgressDialog extends Dialog {

    private RelativeLayout mRoot;
    private CommonRoundProgressBar progressBar;

    public TextView contentTv;
    private TextView cancelBt;
    private TextView confirmBt;
    private OnFinishListener mOnFinishListener;
    private DialogClick mDialogClick;


    public ProgressDialog(Context context) {
        super(context, R.style.NativeInsertDialog);
        initView();
    }


    private void initView() {
        mRoot = (RelativeLayout) View.inflate(getContext(), R.layout.common_dialog_process_layout, null);
        progressBar = mRoot.findViewById(R.id.process_bar);
        contentTv = mRoot.findViewById(R.id.d_content_tv);
        cancelBt = mRoot.findViewById(R.id.d_cancel_bt);
        confirmBt = mRoot.findViewById(R.id.d_confirm_bt);

        getWindow().setGravity(Gravity.CENTER);
        setContentView(mRoot, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogClick!= null){
                    mDialogClick.onLeftClick();
                }
            }
        });
        confirmBt.setVisibility(View.GONE);
    }


    public void setProgress(int progress) {
        setProgress(progress,true);
    }

    public void setProgress(int progress,boolean isAutoDismiss) {
        if (isAutoDismiss && progress >= 100) {
            if (mOnFinishListener != null)
                mOnFinishListener.onFinish();
            dismiss();
            return;
        }
        if (progressBar != null){
            progressBar.setProgress(progress);
        }
    }

    public int getProgress() {
        if (progressBar == null) {
            return 0;
        }
        return progressBar.getProgress();
    }
    public void setDialogClick (DialogClick dialogClick){
        this.mDialogClick = dialogClick;
    }

    public void setHideLeftButton(){
        cancelBt.setVisibility(View.GONE);
    }
    public void setHideRightButton(){
        confirmBt.setVisibility(View.GONE);
    }
    public void setContent(String content){
        if (content != null){
            contentTv.setText(content);
        }
    }
    /**
     * 当进度跑到100的时候调用
     */
    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.mOnFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
        void onFinish();
    }
    public interface DialogClick {
        void onLeftClick();
    }
}
