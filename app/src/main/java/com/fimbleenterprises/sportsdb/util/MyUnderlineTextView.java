package com.fimbleenterprises.sportsdb.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyUnderlineTextView extends androidx.appcompat.widget.AppCompatTextView implements View.OnClickListener {

    public boolean hasBeenClicked = false;

    public MyUnderlineTextView(Context context) {
        super(context);
        this.setPaintFlags(this.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.setTextColor(Color.BLUE);
    }

    @Override
    public void onClick(View view) {
        hasBeenClicked = true;
        this.setTextColor(Color.DKGRAY);
    }
}
