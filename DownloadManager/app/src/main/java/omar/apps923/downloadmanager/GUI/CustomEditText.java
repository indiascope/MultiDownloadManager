package omar.apps923.downloadmanager.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
 
public class CustomEditText extends AppCompatEditText
        {
    public CustomEditText(Context context) {
        super(context);
        if(!this.isInEditMode()){
            Typeface face= Typeface.createFromAsset(context.getAssets(), "DroidSansArabic.ttf");
            this.setTypeface(face);
        }

    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!this.isInEditMode()) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "DroidSansArabic.ttf");
            this.setTypeface(face);
        }
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
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
