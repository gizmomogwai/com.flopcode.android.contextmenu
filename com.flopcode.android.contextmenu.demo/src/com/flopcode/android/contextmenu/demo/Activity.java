package com.flopcode.android.contextmenu.demo;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TextView;

import com.flopcode.android.contextmenu.ContextDialog;
import com.flopcode.android.contextmenu.ContextDialog.TickDirection;

public class Activity extends android.app.Activity {
  private static class IdAndDirection {
    public int fId;
    public TickDirection fDir;

    public IdAndDirection(int id, TickDirection dir) {
      fId = id;
      fDir = dir;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    wireViews(new IdAndDirection(R.id.top, TickDirection.TOP), new IdAndDirection(R.id.left, TickDirection.LEFT), new IdAndDirection(R.id.bottom, TickDirection.BOTTOM), new IdAndDirection(R.id.right, TickDirection.LEFT), new IdAndDirection(R.id.center, TickDirection.NO));
  }

  private void wireViews(IdAndDirection... ids) {
    for (final IdAndDirection i : ids) {
      View v = findViewById(i.fId);
      v.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          if (event.getAction() == MotionEvent.ACTION_DOWN) {
            showContextMenu(v, event, i.fDir);
          }
          return true;
        }
      });
    }
  }

  private void showContextMenu(View v, MotionEvent event, TickDirection dir) {
    MyView mv = (MyView)v;
    PointerCoords c = new PointerCoords();
    event.getPointerCoords(0, c);
    mv.setPos(c.x, c.y);
    
    int[] xy = new int[2];
    v.getLocationOnScreen(xy);
  /* 
    AlertDialog.Builder b = new AlertDialog.Builder(new ContextThemeWrapper(Activity.this, 0));
    Button button= new Button(Activity.this);
    button.setText("hello world");
    b.setCustomTitle(button);
    TextView tv = new TextView(Activity.this);
    tv.setPadding(0, 0, 0, 0);
    tv.setText("1 - abc\n2 - def\n3 - ghi\n4 - jkl");
    b.setView(tv);
    AlertDialog d = b.create();
    d.show();
//    ContextDialog.show(b.create(), (int) c.x + xy[0], (int) c.y + xy[1], dir, null);
*/
   
    ContextDialog dialog = new ContextDialog(Activity.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    TextView tv = new TextView(Activity.this);
    tv.setText("1 - abc\n2 - def");
    dialog.show(tv, (int) c.x + xy[0], (int) c.y + xy[1], dir);
  }
  /*
   * private ColorFilter getScaleColorFilter(float i) { ColorMatrixColorFilter
   * cm = new ColorMatrixColorFilter(new float[]{i, 0, 0, 0, 0, 0, 1, 0, 0, 0,
   * 0, 0, 1, 0, 0, 0, 0, 0, 1, 0}); return cm; }
   */
}
