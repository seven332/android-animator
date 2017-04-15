/*
 * Copyright 2017 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.android.animator.demo;

/*
 * Created by Hippo on 4/15/2017.
 */

import android.animation.Animator;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Property;
import android.view.View;
import com.hippo.android.animator.Animators;
import com.hippo.android.animator.util.ArcMotion;
import com.hippo.android.animator.util.PointFProperty;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OfPointFActivity extends AppCompatActivity {

  private static Property<View, PointF> VIEW_LEFT_TOP = new PointFProperty<View>() {

    private Method SET_FRAME;

    public void setFrame(View view, int left, int top, int right, int bottom) {
      if (SET_FRAME == null) {
        try {
          SET_FRAME = View.class.getDeclaredMethod("setFrame", int.class, int.class, int.class, int.class);
          SET_FRAME.setAccessible(true);
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
      }

      try {
        SET_FRAME.invoke(view, left, top, right, bottom);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void set(View object, PointF value) {
      int x = (int) value.x;
      int y = (int) value.y;
      setFrame(object, x, y, x + object.getWidth(), y + object.getHeight());
    }

    @Override
    public PointF get(View object) {
      return null;
    }
  };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_of_point_f);

    final View view = findViewById(R.id.view);
    final View frame = findViewById(R.id.frame);
    findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Animator animator = Animators.ofPointF(
            view, VIEW_LEFT_TOP,
            0, 0, frame.getWidth() - view.getWidth(), frame.getHeight() - view.getHeight());
        animator.setDuration(1000L);
        animator.start();
      }
    });

    findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ArcMotion motion = new ArcMotion();
        motion.setMaximumAngle(90);
        motion.setMinimumHorizontalAngle(15);
        motion.setMinimumVerticalAngle(0);
        Path path = motion.getPath(0, 0, frame.getWidth() - view.getWidth(), frame.getHeight() - view.getHeight());
        Animator animator = Animators.ofPointF(view, VIEW_LEFT_TOP, path);
        animator.setDuration(1000L);
        animator.start();
      }
    });
  }
}
