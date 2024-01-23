package com.myhss.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.uk.myhss.R;

import java.util.Objects;

public class CustomProgressBar extends Dialog {

    public Context c;

    public CustomProgressBar(Context a) {
        super(a);
        this.c = a;
    }

    public CustomProgressBar(Context a, int myTheme) {
        super(a, myTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(R.color.transparent_Color);
        setContentView(R.layout.view_custom_progress_bar);
        setCancelable(false);
    }
}
