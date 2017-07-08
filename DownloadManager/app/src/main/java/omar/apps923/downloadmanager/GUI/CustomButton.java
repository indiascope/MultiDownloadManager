package omar.apps923.downloadmanager.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);

        Typeface face= Typeface.createFromAsset(context.getAssets(), "DroidSansArabic.ttf");
        this.setTypeface(face);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!this.isInEditMode()) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "DroidSansArabic.ttf");
            this.setTypeface(face);
        }
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!this.isInEditMode()) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "DroidSansArabic.ttf");
            this.setTypeface(face);
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}
