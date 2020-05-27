package com.ouday.lib.conversational;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
public class TwoButtonsView extends FrameLayout implements View.OnClickListener {

    private TextView tvOption1;
    private TextView tvOption2;
    private LinearLayout llOption1;
    private LinearLayout llOption2;
    private ImageView ivOption1;
    private ImageView ivOption2;
    private Option selectedOption = Option.OPTION_1;

    private OnOptionSelectedListener onOptionSelectedListener;

    @ColorInt
    private int textColorSelected;
    @ColorInt
    private int textColorNotSelected;
    @DrawableRes
    private int backgroundColorSelected;
    @DrawableRes
    private int backgroundColorNotSelected;
    private Drawable option1Drawable;
    private Drawable option2Drawable;

    public TwoButtonsView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public TwoButtonsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TwoButtonsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public TwoButtonsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }

    public TwoButtonsView setIvOption1(@DrawableRes int ivOption1) {
        this.option1Drawable = getContext().getDrawable(ivOption1);
        layoutWithDrawable();
        return this;
    }

    public TwoButtonsView setIvOption2(@DrawableRes int ivOption2) {
        this.option2Drawable = getContext().getDrawable(ivOption2);
        layoutWithDrawable();
        return this;
    }

    public TwoButtonsView setSelectedOption(Option option){
        this.selectedOption = option;
        updateUI();
        return this;
    }

    public TwoButtonsView setTextOption1(String textOption1){
        tvOption1.setText(String.format("%s", textOption1));
        return this;
    }

    public TwoButtonsView setTextOption2(String textOption2){
        tvOption2.setText(String.format("%s", textOption2));
        return this;
    }

    public TwoButtonsView setSpanTextOption1(CharSequence spanTextOption1){
        tvOption1.setText(spanTextOption1);
        return this;
    }

    public TwoButtonsView setSpanTextOption2(CharSequence spanTextOption2){
        tvOption2.setText(spanTextOption2);
        return this;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View rootView = inflate(getContext(), R.layout.two_buttons_view, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TwoButtonsView, defStyleAttr, 0);
        initViews(rootView);
        initListeners();
        initAttrs(context, a);
        updateUI();
    }

    private void initAttrs(Context context, TypedArray a) {
        textColorSelected = a.getColor(R.styleable.TwoButtonsView_textColorSelected, ContextCompat.getColor(context, R.color.white));
        selectedOption = getSelectedOptionById(a.getInt(R.styleable.TwoButtonsView_selectedOption, 0));
        textColorNotSelected = a.getColor(R.styleable.TwoButtonsView_textColorNotSelected, ContextCompat.getColor(context, R.color.colorAccent));
        backgroundColorSelected = a.getColor(R.styleable.TwoButtonsView_backgroundColorSelected, R.drawable.shape_rectangle_full_blue);
        backgroundColorNotSelected = a.getColor(R.styleable.TwoButtonsView_backgroundColorNotSelected, R.drawable.shape_rectangle_blue_bordered_2);
        option1Drawable = a.getDrawable(R.styleable.TwoButtonsView_textOption1Drawable);
        option2Drawable = a.getDrawable(R.styleable.TwoButtonsView_textOption2Drawable);
        setTextOption1(a.getString(R.styleable.TwoButtonsView_textOption1) + "");
        setTextOption2(a.getString(R.styleable.TwoButtonsView_textOption2) + "");

        if(option1Drawable!=null || option2Drawable!=null)
            layoutWithDrawable();
        else
            layoutWithoutDrawable();
    }


    private void layoutWithDrawable(){
        ivOption1.setVisibility(VISIBLE);
        ivOption2.setVisibility(VISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,(int) convertDpToPixel(8),0,(int) convertDpToPixel(16));
        if(option1Drawable!=null){
            tvOption1.invalidate();
            ivOption1.setImageDrawable(option1Drawable);
            tvOption1.setLayoutParams(params);
        }
        if(option2Drawable!=null){
            tvOption2.invalidate();
            ivOption2.setImageDrawable(option2Drawable);
            tvOption2.setLayoutParams(params);
        }
    }

    private void layoutWithoutDrawable(){
        ivOption1.setVisibility(GONE);
        ivOption2.setVisibility(GONE);
        tvOption1.setPadding(0, (int) convertDpToPixel(8),0, 0);
        tvOption2.setPadding(0, (int) convertDpToPixel(8),0, 0);
    }

    private Option getSelectedOptionById(int selectedOptionID) {
        if (selectedOptionID == 1) return Option.OPTION_1;
        else if (selectedOptionID == 2) return Option.OPTION_2;
        return null;
    }

    public String getSelectedOption(){
        if(selectedOption.equals(Option.OPTION_1))
            return tvOption1.getText().toString();
        else
            return tvOption2.getText().toString();
    }

    public Option getSelection(){
        return selectedOption;
    }

    private void initListeners() {
        llOption1.setOnClickListener(this);
        llOption2.setOnClickListener(this);
    }

    private void initViews(View rootView) {
        tvOption1 = rootView.findViewById(R.id.tvOption1);
        tvOption2 = rootView.findViewById(R.id.tvOption2);
        llOption1 = rootView.findViewById(R.id.llOption1);
        llOption2 = rootView.findViewById(R.id.llOption2);
        ivOption1 = rootView.findViewById(R.id.ivOption1);
        ivOption2 = rootView.findViewById(R.id.ivOption2);
    }

    @Override
    public void onClick(View v) {
        selectOption( v.getId() == llOption1.getId() ? Option.OPTION_1 : Option.OPTION_2);
    }

    public void selectOption(Option option) {
        selectedOption = option;
        updateUI();
        if (onOptionSelectedListener != null) onOptionSelectedListener.onOptionSelected(this, option);
    }

    private void updateUI(){
        if (selectedOption == null){
            updateThemeNotSelected(tvOption1, llOption1, ivOption1);
            updateThemeNotSelected(tvOption2, llOption2, ivOption2);
        }
        else{
            if(selectedOption==Option.OPTION_1){
                updateThemeSelected(tvOption1, llOption1, ivOption1);
                updateThemeNotSelected(tvOption2, llOption2, ivOption2);
            }
            else{
                updateThemeNotSelected(tvOption1, llOption1, ivOption1);
                updateThemeSelected(tvOption2, llOption2, ivOption2);
            }
        }
    }

    private void updateThemeSelected(TextView textView, LinearLayout layout, ImageView ivDrawable){
        textView.setTextColor(textColorSelected);
        layout.setBackgroundResource(backgroundColorSelected);
        if(ivDrawable!=null)
            ivDrawable.setColorFilter(textColorSelected);
    }

    private void updateThemeNotSelected(TextView textView, LinearLayout layout, ImageView ivDrawable){
        textView.setTextColor(textColorNotSelected);
        layout.setBackgroundResource(backgroundColorNotSelected);
        if(ivDrawable!=null)
            ivDrawable.setColorFilter(textColorNotSelected);
    }

    private float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public enum Option { OPTION_1, OPTION_2 }

    public interface OnOptionSelectedListener{
        void onOptionSelected(TwoButtonsView parent, Option option);
    }

}
