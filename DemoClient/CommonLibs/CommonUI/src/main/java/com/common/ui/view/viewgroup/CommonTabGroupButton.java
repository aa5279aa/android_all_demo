package com.common.ui.view.viewgroup;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.common.ui.R;
import com.common.util.DeviceUtil;

import java.util.List;


/**
 * 带有2个或3个的Tab Group Buttton ，切换时带有动画效果
 *
 * @author lxl
 */
public class CommonTabGroupButton extends LinearLayout {

    protected RadioGroup mRadioGroup;
    protected RadioButton mRadioButton0;
    protected RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    protected View mAnimView0;
    protected View mAnimView1;
    private View mAnimView2;
    protected int mWidth;
    private boolean mIsFillScreen = false;
    private int mTabSize;
    protected Animation mAnimation;
    protected int mIndex = 0;
    protected OnTabItemSelectedListener mOnTabItemSelectedListener;
    private boolean isMatchFont = false;//tab是否匹配字体宽度
    private boolean isSelectBold = false;//是否加粗
    private boolean isInit = true;
    private ColorStateList csl;

    private View mBottomLine;

    /**
     * Tab Group 回调接口
     */
    public interface OnTabItemSelectedListener {

        /**
         * 当点击button时的外部事件处理
         *
         * @param whichButton 对应点击的那个Item button
         */
        void onTabItemClicked(int whichButton);
    }

    /**
     * 设置 TabGroup对外回调接口，供外部回调使用
     *
     * @param onTabItemSelectedListener TabGroup 对外回调接口
     */
    public void setOnTabItemSelectedListener(OnTabItemSelectedListener onTabItemSelectedListener) {
        mOnTabItemSelectedListener = onTabItemSelectedListener;
    }

    /**
     * 设置每个Item的Text Value
     *
     * @param itemArray tab item array
     */
    public void setTabItemArrayText(List<String> itemArray) {
        if (itemArray.size() > 0 && itemArray.size() <= 3) {
            mRadioButton0.setText(itemArray.get(0));
            mRadioButton1.setText(itemArray.get(1));
        }
        if (itemArray.size() == 3) {
            mRadioButton2.setText(itemArray.get(2));
        }

    }

    /**
     * 设置RadioGroup的Background
     *
     * @param res
     */
    public void setBackgroundWithTabGroup(int res) {
        mRadioGroup.setBackgroundResource(res);
    }

    /**
     * 设置RadioGroup的padding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPaddingWithTabGroup(int left, int top, int right, int bottom) {
        mRadioGroup.setPadding(left, top, right, bottom);
    }

    /**
     * 设置view的宽度, 用于位移
     *
     * @param width
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * 设置默认选项卡
     *
     * @param position 0：第一项 1：第二项 2：第三项
     */
    public void setDefaultTab(int position) {
        isInit = true;
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.radioButton0);
                break;
            case 1:
                mRadioGroup.check(R.id.radioButton1);
                break;
            case 2:
                mRadioGroup.check(R.id.radioButton2);
                break;
            default:
                break;

        }

    }

    /**
     * 功能描述:隐藏底部线条
     * <pre>
     *     youj:   2013-12-30      新建
     * </pre>
     */
    public void hineBottomLine() {
        if (null != mBottomLine)
            mBottomLine.setVisibility(View.GONE);
    }

    /**
     * 功能描述: 隐藏底部动画
     * <pre>
     *     zhuc:   2015-10-27      新建
     * </pre>
     */
    public void hideAniwView() {
        if (null != mAnimView0)
            mAnimView0.setVisibility(View.INVISIBLE);
    }

    /**
     * 功能描述: 展示底部动画
     * <pre>
     *     zhuc:   2015-10-27      新建
     * </pre>
     */
    public void showAniwView() {
        if (null != mAnimView0)
            mAnimView0.setVisibility(View.VISIBLE);
    }

    public CommonTabGroupButton(Context context) {
        this(context, null);
    }

    public CommonTabGroupButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpViews(context, attrs);
    }

    protected void setUpViews(Context context, AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.common_tab_group_buttton, null);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_switch);
        mRadioButton0 = (RadioButton) view.findViewById(R.id.radioButton0);
        mRadioButton1 = (RadioButton) view.findViewById(R.id.radioButton1);
        mRadioButton2 = (RadioButton) view.findViewById(R.id.radioButton2);
        mAnimView0 = view.findViewById(R.id.anim_view0);
        mAnimView1 = view.findViewById(R.id.anim_view1);
        mAnimView2 = view.findViewById(R.id.anim_view2);
        addView(view);
        initAttribute(context, attrs);
        init();
    }

    public void setTextColor(ColorStateList csl) {
        this.csl = csl;
        mAnimView0.setBackgroundDrawable(new ColorDrawable(csl.getColorForState(View.SELECTED_STATE_SET, 0)));
    }

    public void setTabSize(int size) {
        if (size == mTabSize && isMatchFont) {
            return;
        }
        mTabSize = size;
        if (mTabSize == 3) {
            mAnimView2.setVisibility(View.INVISIBLE);
        } else {
            mAnimView2.setVisibility(View.GONE);
        }
        init();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTabGroupButton);
        mTabSize = typedArray.getInt(R.styleable.CommonTabGroupButton_common_tabbutton_is_match_font, 2);
        isMatchFont = typedArray.getBoolean(R.styleable.CommonTabGroupButton_common_tabbutton_is_match_font, false);
        if (mTabSize == 3) {
            mAnimView2.setVisibility(View.INVISIBLE);
        } else {
            mAnimView2.setVisibility(View.GONE);
        }
        typedArray.recycle();
    }

    private void init() {
        int[] screenInfo = DeviceUtil.getScreenSize(getContext());
        mWidth = screenInfo[0] - getPaddingLeft() - getPaddingRight();
        if (isMatchFont) {
            mRadioGroup.setPadding(0, mRadioGroup.getPaddingTop(), 0, mRadioGroup.getPaddingBottom());
        }
        if (mTabSize == 2 && isMatchFont) {
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int transimitionWidth;
                    if (checkedId == R.id.radioButton0) {
                        refreshSelectState(0);
                        if (mIndex == 0 && !isInit) {
                            return;
                        }
                        isInit = false;
                        if (mIndex == 1) {
                            transimitionWidth = mWidth / 2;
                        } else {
                            transimitionWidth = 0;
                        }
                        AnimatorSet animationSet = new AnimatorSet();
                        ObjectAnimator moveIn = ObjectAnimator.ofFloat(mAnimView0, "translationX", transimitionWidth, 0f);
                        AnimatorSet.Builder play = animationSet.play(moveIn);
                        if (isMatchFont) {
                            play.with(getScaleAnimation(group, mIndex, 0, 2));
                        }
                        startAnimation(animationSet);
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(0);
                        }
                        mIndex = 0;
                    } else if (checkedId == R.id.radioButton1) {
                        refreshSelectState(1);
                        if (mIndex == 1) {
                            return;
                        }
                        AnimatorSet animationSet = new AnimatorSet();
                        ObjectAnimator moveIn;
                        transimitionWidth = 0;
                        moveIn = ObjectAnimator.ofFloat(mAnimView0, "translationX", transimitionWidth, mWidth / 2);
                        AnimatorSet.Builder play = animationSet.play(moveIn);
                        if (isMatchFont) {
                            play.with(getScaleAnimation(group, mIndex, 1, 2));
                        }
                        startAnimation(animationSet);
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(1);
                        }
                        mIndex = 1;
                    } else {
                    }

                }
            });
        } else if (mTabSize == 3 && isMatchFont) {
            mRadioButton2.setVisibility(View.VISIBLE);
            AnimatorSet animationSet = new AnimatorSet();
            ObjectAnimator scaleAnimation = getScaleAnimation(mRadioGroup, mIndex, 0, 3);
            animationSet.play(scaleAnimation);
            startAnimation(animationSet);
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int transimitionWidth;
                    if (checkedId == R.id.radioButton0) {
                        refreshSelectState(0);
                        if (mIndex == 0 && !isInit) {
                            return;
                        }
                        isInit = false;
                        if (mIndex == 2) {
                            transimitionWidth = mWidth / 3 * 2;
                        } else if (mIndex == 1) {
                            transimitionWidth = mWidth / 3;
                        } else {
                            transimitionWidth = 0;
                        }
                        AnimatorSet animationSet = new AnimatorSet();
                        ObjectAnimator moveIn = ObjectAnimator.ofFloat(mAnimView0, "translationX", transimitionWidth, 0f);
                        AnimatorSet.Builder play = animationSet.play(moveIn);
                        if (isMatchFont) {
                            play.with(getScaleAnimation(group, mIndex, 0, 3));
                        }
                        startAnimation(animationSet);
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(0);
                        }
                        mIndex = 0;
                    } else if (checkedId == R.id.radioButton1) {
                        refreshSelectState(1);
                        if (mIndex == 1) {
                            return;
                        }
                        AnimatorSet animationSet = new AnimatorSet();
                        ObjectAnimator moveIn;
                        if (mIndex == 0) {
                            transimitionWidth = 0;
                        } else {
                            transimitionWidth = mWidth / 3 * 2;
                        }
                        moveIn = ObjectAnimator.ofFloat(mAnimView0, "translationX", transimitionWidth, mWidth / 3);
                        AnimatorSet.Builder play = animationSet.play(moveIn);
                        if (isMatchFont) {
                            play.with(getScaleAnimation(group, mIndex, 1, 3));
                        }
                        startAnimation(animationSet);
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(1);
                        }
                        mIndex = 1;
                    } else if (checkedId == R.id.radioButton2) {
                        refreshSelectState(2);
                        if (mIndex == 2) {
                            return;
                        }
                        AnimatorSet animationSet = new AnimatorSet();
                        ObjectAnimator moveIn;
                        if (mIndex == 0) {
                            transimitionWidth = 0;
                        } else {
                            transimitionWidth = mWidth / 3;
                        }
                        moveIn = ObjectAnimator.ofFloat(mAnimView0, "translationX", transimitionWidth, mWidth / 3 * 2);
                        AnimatorSet.Builder play = animationSet.play(moveIn);
                        if (isMatchFont) {
                            play.with(getScaleAnimation(group, mIndex, 2, 3));
                        }
                        startAnimation(animationSet);
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(2);
                        }
                        mIndex = 2;
                    }
                }
            });
        } else if (mTabSize == 3) {
            mRadioButton2.setVisibility(View.VISIBLE);
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int transimitionWidth = mWidth / 3;
                    if (checkedId == R.id.radioButton0) {
                        refreshSelectState(0);
                        if (mIndex == 2) {
                            transimitionWidth = mWidth / 3 * 2;
                        }
                        mAnimation = new TranslateAnimation(transimitionWidth, 0, 0, 0);
                        startAnimation();
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(0);
                        }
                        mIndex = 0;
                    } else if (checkedId == R.id.radioButton1) {
                        refreshSelectState(1);
                        if (mIndex == 0) {
                            mAnimation = new TranslateAnimation(0, mWidth / 3, 0, 0);
                        } else if (mIndex == 2) {
                            mAnimation = new TranslateAnimation(mWidth / 3 * 2, mWidth / 3, 0, 0);
                        }

                        startAnimation();
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(1);
                        }
                        mIndex = 1;
                    } else if (checkedId == R.id.radioButton2) {
                        refreshSelectState(2);
                        if (mIndex == 0) {
                            mAnimation = new TranslateAnimation(0, mWidth / 3 * 2, 0, 0);
                        } else if (mIndex == 1) {
                            mAnimation = new TranslateAnimation(mWidth / 3, mWidth / 3 * 2, 0, 0);
                        }

                        startAnimation();
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(2);
                        }
                        mIndex = 2;
                    }
                }
            });
        }
    }

    public ObjectAnimator getScaleAnimation(RadioGroup group, int fromIndex, int toIndex, int count) {
        int itemWidth = mWidth / count;
        View fromView = group.getChildAt(fromIndex);
        View toView = group.getChildAt(toIndex);
        if (fromView instanceof RadioButton && toView instanceof RadioButton) {
            RadioButton fromButton = (RadioButton) fromView;
            RadioButton toButton = (RadioButton) toView;
            float fromWidth = fromButton.getPaint().measureText(fromButton.getText().toString());
            float toWidth = toButton.getPaint().measureText(toButton.getText().toString());
            float fromRate = fromWidth / (float) itemWidth;
            float toRate = toWidth / (float) itemWidth;
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mAnimView0, "scaleX", fromRate, toRate);
            return scaleX;
        }
        return ObjectAnimator.ofFloat(mAnimView0, "scaleX", 1, 1);
    }

    private void refreshSelectState(int position) {
        if (!isSelectBold || csl == null) {
            return;
        }
        mRadioButton0.getPaint().setFakeBoldText(false);
        mRadioButton1.getPaint().setFakeBoldText(false);
        mRadioButton2.getPaint().setFakeBoldText(false);
        int defaultColor = getResources().getColor(R.color.color_tab_item_normal);
        mRadioButton0.setTextColor(defaultColor);
        mRadioButton1.setTextColor(defaultColor);
        mRadioButton2.setTextColor(defaultColor);
        RadioButton select = null;
        if (position == 0) {
            select = mRadioButton0;
        }
        if (position == 1) {
            select = mRadioButton1;
        }
        if (position == 2) {
            select = mRadioButton2;
        }
        if (select != null) {
            select.getPaint().setFakeBoldText(true);
            select.setTextColor(getResources().getColor(R.color.color_blue));
        }
    }


    protected void startAnimation() {
        // True:图片停在动画结束位置
        if (mAnimation != null) {
            mAnimation.setFillAfter(true);
            mAnimation.setDuration(300);
            mAnimView0.startAnimation(mAnimation);
        }
    }

    protected void startAnimation(AnimatorSet animatorSet) {
        // True:图片停在动画结束位置
        animatorSet.setDuration(400);
        animatorSet.start();
    }


    public int getIndex() {
        return mIndex;
    }

    public void setIsMatchFont(boolean matchFont) {
        isMatchFont = matchFont;
    }

    public void setIsSelectBold(boolean selectBold) {
        isSelectBold = selectBold;
    }


}
