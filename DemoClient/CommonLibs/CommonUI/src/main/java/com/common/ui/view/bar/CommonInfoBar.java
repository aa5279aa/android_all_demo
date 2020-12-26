package com.common.ui.view.bar;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.ui.R;
import com.common.util.DeviceUtil;
import com.common.util.StringUtil;
import com.common.util.ToastUtil;

import static com.common.ui.view.bar.CommonInfoBar.CommonInfoBarStyleControl.TITLE_GRAVITY_CENTER;


/**
 * <p>封装InfoBar功能，用于显示信息Logo + 标题 + 内容 + Arrow的式样，具体显示效果如下：</p>
 * <p>----------------------------------------</p>
 * <p> Logo 	*Label 			Value 	 Arrow </p>
 * <p>----------------------------------------</p>
 * <p>Logo - 需要显示的logo图片</p>
 * <p>Label - 标签文字，可设置必选项标识 "*"</p>
 * <p>Value - 当前设置/回显的内容，可设置特定的格式以支持多行显示</p>
 * <p>Arrow - 为箭头图片，默认向右</p>
 * <p>Value内容部分自适应显示宽度，剩余空间由Label标题部分自适应占满。</p>
 * <p>
 * <p>
 * <b>XML attributes</b>
 * <p>
 *
 * @author lxl
 * 再添加一种hint
 */
public class CommonInfoBar extends LinearLayout {

    protected CommonInfoBarStyleControl mStyleControl = new CommonInfoBarStyleControl();
    protected TextView mTitleText;
    protected LinearLayout mInfoContainer;
    protected TextView mValueText;
    protected TextView mHintText;
    protected ImageView mIconView;

    public CommonInfoBar(Context context) {
        this(context, null);
    }

    public CommonInfoBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonInfoBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null);
    }

    public CommonInfoBar(Context context, AttributeSet attrs, int defStyle, CommonInfoBarStyleControl control) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.common_info_bar, this);
        if (control != null) {
            mStyleControl = control;
        } else {
            initFromAttributes(context, attrs);
        }
        initView();
        setViewAttr();
    }

    private void setViewAttr() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        mTitleText.setGravity(Gravity.CENTER);
        mTitleText.setText(mStyleControl.mTitleText);//为空也应占据宽度
        mTitleText.setTextAppearance(getContext(), mStyleControl.mTitleStyle);
        if (mStyleControl.mTitleWidth > 0) {
            mTitleText.setWidth(mStyleControl.mTitleWidth);
        }
        ((LayoutParams) mValueText.getLayoutParams()).gravity = mStyleControl.mValueGravity == TITLE_GRAVITY_CENTER ? Gravity.CENTER : Gravity.RIGHT;
        mValueText.setText(mStyleControl.mValueText);
        mValueText.setTextAppearance(getContext(), mStyleControl.mValueStyle);

        mHintText.setVisibility(StringUtil.emptyOrNull(mStyleControl.mHintText) ? View.GONE : View.VISIBLE);
        mHintText.setText(mStyleControl.mHintText);
        mHintText.setTextAppearance(getContext(), mStyleControl.mHintStyle);

    }

    public void initFromAttributes(Context context, AttributeSet attrs) {
        // 通过xml style设置默认参数
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonInfoBar);
        if (attrs == null || a == null) {
            return;
        }
        //title
        mStyleControl.mTitleGravity = a.getInt(R.styleable.CommonInfoBar_common_infobar_title_gravity, TITLE_GRAVITY_CENTER);
        mStyleControl.mTitleText = a.getString(R.styleable.CommonInfoBar_common_infobar_title_text);
        mStyleControl.mTitleStyle = a.getResourceId(R.styleable.CommonInfoBar_common_infobar_title_appearance, mStyleControl.mTitleStyle);
        mStyleControl.mTitleWidth = a.getDimensionPixelSize(R.styleable.CommonInfoBar_common_infobar_title_label_width, 0);

        //value
        mStyleControl.mValueGravity = a.getInt(R.styleable.CommonInfoBar_common_infobar_value_gravity, TITLE_GRAVITY_CENTER);
        mStyleControl.mValueText = a.getString(R.styleable.CommonInfoBar_common_infobar_value_text);
        mStyleControl.mValueStyle = a.getResourceId(R.styleable.CommonInfoBar_common_infobar_value_appearance, mStyleControl.mValueStyle);

        mStyleControl.mHintText = a.getString(R.styleable.CommonInfoBar_common_infobar_hint_text);
        mStyleControl.mHintStyle = a.getResourceId(R.styleable.CommonInfoBar_common_infobar_hint_appearance, mStyleControl.mValueStyle);

        //arrow
        mStyleControl.mHasArrow = a.getBoolean(R.styleable.CommonInfoBar_common_infobar_hasArrow, false);
        mStyleControl.mArrowDrawable = a.getDrawable(R.styleable.CommonInfoBar_common_infobar_arrow_drawable);
        mStyleControl.mArrowIconWidth = a.getDimensionPixelSize(R.styleable.CommonInfoBar_common_infobar_arrow_width, DeviceUtil.getPixelFromDip(context, 12));
        mStyleControl.mArrowIconHeight = a.getDimensionPixelSize(R.styleable.CommonInfoBar_common_infobar_arrow_height, DeviceUtil.getPixelFromDip(context, 12));
        //icon
        mStyleControl.mIconWidth = a.getDimensionPixelSize(R.styleable.CommonInfoBar_common_infobar_icon_width, 0);
        mStyleControl.mIconHeight = a.getDimensionPixelSize(R.styleable.CommonInfoBar_common_infobar_icon_height, 0);
        mStyleControl.mIconDrawable = a.getDrawable(R.styleable.CommonInfoBar_common_infobar_icon_drawable);
        a.recycle();
    }

    /**
     * 创建child view对象，并添加到父容器中
     * <p>Value内容部分自适应显示宽度，剩余空间由Label标题部分自适应占满。</p>
     */
    protected void initView() {
        mTitleText = (TextView) findViewById(R.id.common_info_bar_title);
        mInfoContainer = (LinearLayout) findViewById(R.id.common_info_container);
        mValueText = (TextView) findViewById(R.id.common_info_bar_value);
        mHintText = (TextView) findViewById(R.id.common_info_bar_hint);
        mIconView = (ImageView) findViewById(R.id.common_info_bar_arrow);

        if (mStyleControl.mHasArrow) {
            if (mStyleControl.mArrowDrawable != null) {
                mIconView.getLayoutParams().width = mStyleControl.mArrowIconWidth;
                mIconView.getLayoutParams().height = mStyleControl.mArrowIconHeight;
                mIconView.setBackgroundDrawable(mStyleControl.mArrowDrawable);
            } else {
                mIconView.setImageResource(R.drawable.common_arrow_right);
            }
            mIconView.setVisibility(View.VISIBLE);
        }
        if (StringUtil.emptyOrNull(mStyleControl.mValueText)) {
//            mInfoContainer.setVisibility(View.GONE);
        }
//        setPadding(DeviceUtil.getPixelFromDip(getContext(), 10));
    }

    /**
     * 设置title文字
     *
     * @param text 需要显示的Value文本
     */
    public void setTitleText(CharSequence text) {
        if (mTitleText != null) {
            mTitleText.setText(text);
        }
    }


    public void setValueCopy() {
        mValueText.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mValueText.getText().toString());
                ToastUtil.showToast(getContext(), "复制成功");
                return false;

            }

        });

    }

    /**
     * 设置Value文字
     *
     * @param text 需要显示的Value文本
     */
    public void setValueText(CharSequence text) {
        if (mValueText != null) {
            mValueText.setText(text);
        }
    }

    public void setPadding(int padding) {
        super.setPadding(padding, padding, padding, padding);
    }


    public static class CommonInfoBarStyleControl {
        public static final int DEFAULT_WIDTH = 400;
        //
        public static final int TITLE_GRAVITY_CENTER = 0;
        public static final int TITLE_GRAVITY_RIGHT = 3;


        //title
        public int mTitleWidth = DEFAULT_WIDTH;
        public int mTitleGravity = TITLE_GRAVITY_CENTER;
        public String mTitleText;
        public int mTitleStyle = R.style.text_16_999999;

        //value
        public int mValueGravity = TITLE_GRAVITY_CENTER;
        public String mValueText;
        public int mValueStyle = R.style.text_18_333333;

        //hint
//        public int mHintGravity = TITLE_GRAVITY_CENTER;
        public String mHintText;
        public int mHintStyle = R.style.text_14_333333;

        //arrow
        public boolean mHasArrow = false;
        public Drawable mArrowDrawable;
        public int mArrowIconHeight;
        public int mArrowIconWidth;

        //icon
        public Drawable mIconDrawable;
        public int mIconHeight;
        public int mIconWidth;
    }
}