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
 * Created by Hippo on 4/16/2017.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import com.hippo.android.animator.Animators;

public class CrossFadeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cross_fade);

    final View view1 = findViewById(R.id.view1);
    final View view2 = findViewById(R.id.view2);
    final ViewGroup frame = (ViewGroup) findViewById(R.id.frame);

    view2.setVisibility(View.INVISIBLE);

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Animator animator = Animators.crossFade(view1, view2, frame, true);
        animator.setDuration(1000L);
        animator.addListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationStart(Animator animation) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            view1.setVisibility(View.INVISIBLE);
          }
        });
        animator.start();
      }
    });
  }
}
