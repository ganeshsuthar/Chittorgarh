package com.m.chittorgarh.Halper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FormattedTextView extends TextView {
    public FormattedTextView(Context context) {
        super(context);
        setTypeface(OpenSans.getInstance(context).getTypeFace());
    }

    public FormattedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(OpenSans.getInstance(context).getTypeFace());
    }

    public FormattedTextView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(OpenSans.getInstance(context).getTypeFace());
    }
}
