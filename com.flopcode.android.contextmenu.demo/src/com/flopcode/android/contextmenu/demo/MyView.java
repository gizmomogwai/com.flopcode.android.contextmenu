package com.flopcode.android.contextmenu.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelXorXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

  private Paint fPaint;
  private RectF fRect;


  public MyView(Context context) {
    super(context);
  }
  
  public MyView(Context context, AttributeSet as) {
    super(context, as);
  }

  public MyView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  {
    fPaint = new Paint();
    fPaint.setXfermode(new PixelXorXfermode(0xffffffff));
    fPaint.setARGB(0x00, 0x00, 0x00, 0x00);
    fRect = new RectF();
  }
  
  public void setPos(float x, float y) {
    fRect.left = x-5;
    fRect.right = x+5;
    fRect.top = y-5;
    fRect.bottom = y+5;
    invalidate();
  }
  
  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawOval(fRect, fPaint);
  }
  
}
