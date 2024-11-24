package com.example.camlingo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    private TextView outlineTextView = null;
    private float mStroke;
    private int mStrokeColor;

    public CustomTextView(Context context, float stroke) {
        super(context);
        outlineTextView = new TextView(context);
        this.mStroke=stroke;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        outlineTextView = new TextView(context, attrs);
        TypedArray attr = context.obtainStyledAttributes(attrs,R.styleable.CustomTextView);
        this.mStroke = attr.getFloat(R.styleable.CustomTextView_stroke,6);
        this.mStrokeColor =attr.getColor(R.styleable.CustomTextView_strokeColor,Color.parseColor("#000000"));
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle, float stroke) {
        super(context, attrs, defStyle);
        outlineTextView = new TextView(context, attrs, defStyle);
        this.mStroke=stroke;
        init();
    }

    public void init() {
        TextPaint paint = outlineTextView.getPaint();
        paint.setStrokeWidth(mStroke);
        paint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(mStrokeColor);
        outlineTextView.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText())) {
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }

}
