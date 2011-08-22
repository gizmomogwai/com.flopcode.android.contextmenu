package com.flopcode.android.contextmenu;

import com.flopcode.android.contextmenu.ContextDialog.TickDirection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager.LayoutParams;

public class BackgroundDrawable extends Drawable {
  private TickDirection fDirection;

  private Drawable fLeft;
  private Drawable fRight;
  private NinePatchDrawable fTop;
  private Drawable fBottom;

  private Drawable fContent;

  private Drawable fLeftBottom;
  private Drawable fRightBottom;
  private Drawable fTopLeft;
  private Drawable fTopRight;

  private Drawable fLeftTick;
  private Drawable fRightTick;
  private Drawable fTopTick;
  private Drawable fBottomTick;

  private int fY;
  private int fX;

  private int fMaxY;
  private int fMaxX;

  private Rect fInnerRect;
  private Rect fRect;

  private Rect fPadding;

  private Drawable[] fDrawables;

  public BackgroundDrawable(Context context) {
    final Resources r = context.getResources();
    final DisplayMetrics dm = r.getDisplayMetrics();

    fMaxY = dm.heightPixels;
    fMaxX = dm.widthPixels;
    fLeftTick = r.getDrawable(R.drawable.menu_context_bg_tip_left);
    fLeft = r.getDrawable(R.drawable.menu_context_bg_edge_left);
    fRightTick = r.getDrawable(R.drawable.menu_context_bg_tip_right);
    fRight = r.getDrawable(R.drawable.menu_context_bg_edge_right);
    fTopTick = r.getDrawable(R.drawable.menu_context_bg_tip_top);
    fTop = (NinePatchDrawable) r.getDrawable(R.drawable.menu_context_bg_edge_top);
    fBottomTick = r.getDrawable(R.drawable.menu_context_bg_tip_bottom);
    fBottom = r.getDrawable(R.drawable.menu_context_bg_edge_bottom);
    fContent = r.getDrawable(R.drawable.menu_context_bg_interior);
    fLeftBottom = r.getDrawable(R.drawable.menu_context_bg_corner_bottom_left);
    fRightBottom = r.getDrawable(R.drawable.menu_context_bg_corner_bottom_right);
    fTopLeft = r.getDrawable(R.drawable.menu_context_bg_corner_top_left);
    fTopRight = r.getDrawable(R.drawable.menu_context_bg_corner_top_right);

    fDrawables = new Drawable[] { fLeft, fLeftTick, fLeftBottom, fBottom, fBottomTick, fRightBottom, fRight, fRightTick, fTopRight, fTop, fTopTick, fTopLeft, fContent };

    fPadding = new Rect();
    fInnerRect = new Rect();
    
    Rect help = new Rect();
    fTop.getPadding(help);
    fPadding.top = help.top;

    fLeft.getPadding(help);
    fPadding.left = help.left;

    fRight.getPadding(help);
    fPadding.right = help.right;
    
    fBottom.getPadding(help);
    fPadding.bottom = help.bottom;
  }

  @Override
  public void draw(Canvas canvas) {
    setupCoordinates();

    drawTop(canvas);
    drawLeft(canvas);
    drawBottom(canvas);
    drawRight(canvas);
    drawCorners(canvas);
    drawContent(canvas);
  }

  private void setupCoordinates() {
    fRect = getBounds();
    fInnerRect.set(fRect.left + fLeft.getIntrinsicWidth(), fRect.top + fTop.getIntrinsicHeight(), fRect.right - fRight.getIntrinsicWidth(), fRect.bottom - fBottom.getIntrinsicHeight());
  }

  private void drawContent(Canvas canvas) {
    fContent.setBounds(fInnerRect.left, fInnerRect.top, fInnerRect.right, fInnerRect.bottom);
    fContent.draw(canvas);
  }

  private void drawLeft(Canvas canvas) {
    if (fDirection != TickDirection.LEFT || toNearAtTopOrBottom(canvas)) {
      fLeft.setBounds(fRect.left, fInnerRect.top, fInnerRect.left, fInnerRect.bottom);
      fLeft.draw(canvas);
    } else {
      final int leftTickTop = fRect.top + (fRect.bottom - fRect.top) / 2 - fLeftTick.getIntrinsicHeight() / 2;
      final int leftTickBottom = leftTickTop + fLeftTick.getIntrinsicHeight();
      drawVertical(canvas, fLeft, fLeftTick, fRect.left, fInnerRect.left, fInnerRect.top, leftTickTop, leftTickBottom, fInnerRect.bottom);
    }
  }

  private void drawVertical(Canvas canvas, Drawable d, Drawable t, int left, int right, int y1, int y2, int y3, int y4) {
    if (y2 > y1) {
      d.setBounds(left, y1, right, y2);
      d.draw(canvas);
    }

    t.setBounds(left, y2, right, y3);
    t.draw(canvas);

    if (y4 > y3) {
      d.setBounds(left, y3, right, y4);
      d.draw(canvas);
    }
  }

  private void drawRight(Canvas canvas) {
    if (fDirection != TickDirection.RIGHT || toNearAtTopOrBottom(canvas)) {
      fRight.setBounds(fInnerRect.right, fInnerRect.top, fRect.right, fInnerRect.bottom);
      fRight.draw(canvas);
    } else {
      final int rightTickTop = fRect.top + (fRect.bottom - fRect.top) / 2 - fLeftTick.getIntrinsicHeight() / 2;
      final int rightTickBottom = rightTickTop + fRightTick.getIntrinsicHeight();
      drawVertical(canvas, fRight, fRightTick, fInnerRect.right, fRect.right, fInnerRect.top, rightTickTop, rightTickBottom, fInnerRect.bottom);
    }
  }

  private boolean toNearAtTopOrBottom(Canvas canvas) {
    return (fY < 0) || (fY + canvas.getHeight() > fMaxY);
  }

  private boolean toNearAtLeftOrRight(Canvas canvas) {
    return (fX < 0) || (fX + canvas.getWidth() > fMaxX);
  }

  private void drawBottom(Canvas canvas) {
    if (fDirection != TickDirection.BOTTOM || toNearAtLeftOrRight(canvas)) {
      fBottom.setBounds(fInnerRect.left, fInnerRect.bottom, fInnerRect.right, fRect.bottom);
      fBottom.draw(canvas);
    } else {
      final int bottomTickLeft = fRect.left + (fRect.right - fRect.left) / 2 - fBottomTick.getIntrinsicWidth() / 2;
      final int bottomTickRight = bottomTickLeft + fBottomTick.getIntrinsicWidth();
      drawHorizontal(canvas, fBottom, fBottomTick, fInnerRect.bottom, fRect.bottom, fInnerRect.left, bottomTickLeft, bottomTickRight, fInnerRect.right);
    }
  }

  private void drawHorizontal(Canvas canvas, Drawable d, Drawable t, int top, int bottom, int x1, int x2, int x3, int x4) {
    if (x2 > x1) {
      d.setBounds(x1, top, x2, bottom);
      d.draw(canvas);
    }
    t.setBounds(x2, top, x3, bottom);
    t.draw(canvas);
    if (x4 > x3) {
      d.setBounds(x3, top, x4, bottom);
      d.draw(canvas);
    }
  }

  private void drawTop(Canvas canvas) {
    if (fDirection != TickDirection.TOP || toNearAtTopOrBottom(canvas)) {
      fTop.setBounds(fInnerRect.left, fRect.top, fInnerRect.right, fInnerRect.top);
      fTop.draw(canvas);
    } else {
      final int topTickLeft = fRect.left + (fRect.right - fRect.left) / 2 - fTopTick.getIntrinsicWidth() / 2;
      final int topTickRight = topTickLeft + fTopTick.getIntrinsicWidth();
      drawHorizontal(canvas, fTop, fTopTick, fRect.top, fInnerRect.top, fInnerRect.left, topTickLeft, topTickRight, fInnerRect.right);
    }
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSPARENT;
  }

  @Override
  public void setAlpha(int alpha) {
    for (Drawable d : fDrawables) {
      d.setAlpha(alpha);
    }
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    for (Drawable d : fDrawables) {
      d.setColorFilter(cf);
    }
  }

  @Override
  public boolean getPadding(Rect padding) {
    padding.set(fPadding);
    return true;
  }

  public int getTopOffset() {
    return fTop.getIntrinsicHeight();
  }

  public void setYPos(int y) {
    fY = y;
  }

  public void setDirection(TickDirection dir) {
    fDirection = dir;
  }

  public void setValuesForDirection(LayoutParams lp, int x, int y, int w, int h) {
    switch (fDirection) {
    case LEFT:
      lp.y = y - h / 2;
      lp.x = x;
      break;
    case RIGHT:
      lp.y = y - h / 2;
      lp.x = x - w;
      break;
    case TOP:
      lp.x = x - w / 2;
      lp.y = y;
      break;
    case BOTTOM:
      lp.x = x - w / 2;
      lp.y = y - h;
      break;
    case NO:
      lp.x = x - w / 2;
      lp.y = y - h / 2;
      break;
    default:
      throw new RuntimeException("nyi for " + fDirection.toString());
    }

    if ((lp.x + w > fMaxX) || (lp.y + h > fMaxY)) {
      fDirection = TickDirection.NO;
      lp.x = x - w / 2;
      lp.y = y - w / 2;
    }
    setXPos(lp.x);
    setYPos(lp.y);
  }

  private void setXPos(int x) {
    fX = x;
  }

  private void drawCorners(Canvas canvas) {
    drawTopLeft(canvas);
    drawLeftBottom(canvas);
    drawBottomRight(canvas);
    drawRightTop(canvas);
  }

  private void drawRightTop(Canvas canvas) {
    fTopRight.setBounds(fInnerRect.right, fRect.top, fRect.right, fInnerRect.top);
    fTopRight.draw(canvas);
  }

  private void drawBottomRight(Canvas canvas) {
    fRightBottom.setBounds(fInnerRect.right, fInnerRect.bottom, fRect.right, fRect.bottom);
    fRightBottom.draw(canvas);
  }

  private void drawLeftBottom(Canvas canvas) {
    fLeftBottom.setBounds(fRect.left, fInnerRect.bottom, fInnerRect.left, fRect.bottom);
    fLeftBottom.draw(canvas);
  }

  private void drawTopLeft(Canvas canvas) {
    fTopLeft.setBounds(fRect.left, fRect.top, fInnerRect.left, fInnerRect.top);
    fTopLeft.draw(canvas);
  }

  @Override
  public int getMinimumWidth() {
    return fTopLeft.getIntrinsicWidth() + fTopTick.getIntrinsicWidth() + fTopRight.getIntrinsicWidth();
  }

  @Override
  public int getMinimumHeight() {
    return fTopLeft.getIntrinsicHeight() + fLeftTick.getIntrinsicHeight() + fLeftBottom.getIntrinsicHeight();
  }
}
