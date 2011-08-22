package com.flopcode.android.contextmenu;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;

public class ContextDialog extends Dialog {

  public enum TickDirection {
    TOP, LEFT, BOTTOM, RIGHT, NO
  };

  private BackgroundDrawable fBackground;

  public ContextDialog(Context c) {
    super(c);
    fBackground = new BackgroundDrawable(c);
    getWindow().setBackgroundDrawable(fBackground);
  }

  public BackgroundDrawable getBackground() {
    return fBackground;
  }

  public ContextDialog(Context c, int style) {
    super(c, style);
    fBackground = new BackgroundDrawable(c);
  }

  public void show(View content, int x, int y, TickDirection dir) {
    show(this, content, x, y, dir, fBackground);
  }

  public static void show(Dialog dialog, View content, int x, int y, TickDirection dir, BackgroundDrawable background) {
    if (background == null) {
      background = new BackgroundDrawable(dialog.getContext());
    }
    content.setMinimumWidth(background.getMinimumWidth());
    content.setMinimumHeight(background.getMinimumHeight());
    dialog.setContentView(content);
    dialog.getWindow().setBackgroundDrawable(background);
    background.setDirection(dir);
    dialog.show();

    View v = dialog.getWindow().getDecorView();
    v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    int h = v.getMeasuredHeight();
    int w = v.getMeasuredWidth();
    LayoutParams lp = dialog.getWindow().getAttributes();
    background.setValuesForDirection(lp, x, y, w, h);
    lp.gravity = Gravity.TOP | Gravity.LEFT;
    dialog.getWindow().setAttributes(lp);
  }
}
