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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * Created by Hippo on 4/13/2017.
 */

public class MainActivity extends AppCompatActivity {

  private static final String[] TITLES = {
      "Play Together",
      "Play Sequentially",
      "ofPointF",
      "Circular Reveal",
      "Cross Fade",
      "Recolor Background",
  };

  private static final Class<?>[] CLASSES = {
      PlayTogetherActivity.class,
      PlaySequentiallyActivity.class,
      OfPointFActivity.class,
      CircularRevealActivity.class,
      CrossFadeActivity.class,
      RecolorBackgroundActivity.class,
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ListView listView = (ListView) findViewById(R.id.list);
    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TITLES));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(MainActivity.this, CLASSES[position]));
      }
    });
  }
}
