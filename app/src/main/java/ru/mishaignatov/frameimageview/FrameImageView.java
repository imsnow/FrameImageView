package ru.mishaignatov.frameimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Ignatov on 29.10.2015.
 */
public class FrameImageView extends View {

    private Drawable drawable;
    private int wight;
    private int height;

    private Paint paint;

    private float radius;
    private float offset;
    private float stroke;

    private int color;


    public FrameImageView(Context context) {
        super(context);
        init();
    }

    public FrameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FrameImageView);

        radius = arr.getDimensionPixelSize(R.styleable.FrameImageView_radius, 0);
        offset = arr.getDimensionPixelSize(R.styleable.FrameImageView_offset, 0);
        stroke = arr.getDimensionPixelSize(R.styleable.FrameImageView_border_stroke, 3);
        color = arr.getColor(R.styleable.FrameImageView_border_color, Color.WHITE);
        drawable = arr.getDrawable(R.styleable.FrameImageView_src);

        wight = drawable.getMinimumWidth();
        height = drawable.getMinimumHeight();

        init();
    }

    private void init(){

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);

        paint.setStrokeWidth(stroke);
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        int h = (int)(height*(float)w/wight);


        setMeasuredDimension(w, h);
    }

    private int w, h;
    RectF rect = new RectF();
    Path path = new Path();

    @Override
    public void draw(Canvas canvas) {

        w = canvas.getWidth();
        h = canvas.getHeight();

        if(radius == 0)
            radius = (w*1.0f)/15;
        if(offset == 0)
            offset = (w*1.0f)/30;

        // It's border
        path.moveTo(offset, offset + radius);
        path.lineTo(offset, h - offset - radius);

        rect.set(offset - radius, h - offset - radius, offset + radius, h - offset + radius);
        path.arcTo(rect, 270, 90);
        //path.addArc(rect, 0, -90);
        //path.moveTo(offset + radius, h - offset);
        path.lineTo(w - offset - radius, h - offset);

        rect.set(w - offset - radius, h - offset - radius, w - offset + radius, h - offset + radius);
        path.arcTo(rect, 180, 90);
        //path.addArc(rect, 180, 90);
        //path.moveTo(w - offset, h - offset - radius);
        path.lineTo(w - offset, offset + radius);

        rect.set(w - offset - radius, offset - radius, w - offset + radius, offset + radius);
        path.arcTo(rect, 90, 90);
        //path.addArc(rect, 90, 90);
        //path.moveTo(w - offset - radius, offset);
        path.lineTo(offset + radius, offset);

        rect.set(offset - radius, offset - radius, offset + radius, offset + radius);
        path.arcTo(rect, 0, 90);
        //path.addArc(rect, 0, 90);

        path.close();

        canvas.clipPath(path);

        if(drawable != null) {
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
        }

        canvas.drawPath(path, paint);

        super.draw(canvas);
    }
}
