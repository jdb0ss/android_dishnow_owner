package com.picke.dishnow_owner;

import android.app.AppComponentFactory;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

public class DNedittext extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener{
    private Drawable cleardrawable;
    boolean flag = false;
    private OnFocusChangeListener onFocusChangeListener;

    public DNedittext(final Context context) {
        super(context);
    }
    public DNedittext(final Context context, final Attributes attributes){
        super(context, (AttributeSet) attributes);
    }
    public DNedittext(final Context context, final Attributes attributes,final int style){
        super(context, (AttributeSet) attributes,style);
        init();
    }

    public void init(){
        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_iconmonstr_arrow_25);
        cleardrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(cleardrawable,getHintTextColors());
        cleardrawable.setBounds(0,0,cleardrawable.getIntrinsicHeight(),cleardrawable.getIntrinsicWidth());

        setCleariconVisible(false);
        super.setOnFocusChangeListener((OnFocusChangeListener) this);
        addTextChangedListener((TextWatcher) this);

        }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
         public final void onTextChanged(final CharSequence s,final int start, final int before, final int count){
                if(isFocused()){
            setCleariconVisible(s.length()>0);
            }
         }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
         public void onFocusChange(final View view,final boolean hasFocus){
            if(hasFocus){
                setCleariconVisible(getText().length()>0);
            }else{
                setCleariconVisible(false);
            }

            if(onFocusChangeListener != null){
                onFocusChangeListener.onFocusChange(view, hasFocus);
            }
         }

    public void setCleariconVisible(boolean visible){
        cleardrawable.setVisible(visible,false);
        setCompoundDrawables(null,null,visible?cleardrawable :null,null);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangedListener){
        this.onFocusChangeListener = onFocusChangedListener;
    }
}
