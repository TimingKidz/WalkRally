/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.walkrally;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import helpers.SnackbarHelper;

/**
 * This application demonstrates using augmented images to place anchor nodes. app to include image
 * tracking functionality.
 *
 * <p>In this example, we assume all images are static or moving slowly with a large occupation of
 * the screen. If the target is actively moving, we recommend to check
 * ArAugmentedImage_getTrackingMethod() and render only when the tracking method equals to
 * AR_AUGMENTED_IMAGE_TRACKING_METHOD_FULL_TRACKING. See details in <a
 * href="https://developers.google.com/ar/develop/c/augmented-images/">Recognize and Augment
 * Images</a>.
 */
public class AugmentedImageActivity extends AppCompatActivity {

  private ArFragment arFragment;
  private ImageView fitToScanView;
  private  AugmentedImageNode node = null;
  // Augmented image and its associated center pose anchor, keyed by the augmented image in
  // the database.
  private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    node = null;

    setContentView(R.layout.activity_ar);
      Button sendButton = (Button) findViewById(R.id.senbut);
      EditText inputText = findViewById(R.id.text0);
      sendButton.setVisibility(View.INVISIBLE);
      inputText.setVisibility(View.INVISIBLE);
      new Team().readData(new Team.MyCallback() {
          @Override
          public void onCallback(Team value) {
              if(!value.isFin){
                  new User().readTeamcp(new User.MyCallbackk() {
                      @Override
                      public void onCallbackk(Clues value) {
                          Button sendButton = (Button) findViewById(R.id.senbut);
                          EditText inputText = findViewById(R.id.text0);
                          sendButton.setVisibility(View.VISIBLE);
                          inputText.setVisibility(View.VISIBLE);

                          sendButton.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  EditText inputText = findViewById(R.id.text0);
                                  String text = inputText.getText().toString();
                                  if( text.equals(value.ans)){
                                      Toast.makeText(getApplicationContext(),
                                              "Correct.", Toast.LENGTH_SHORT).show();
                                      if(value.isEnd){
                                          FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                          User value = dataSnapshot.getValue(User.class);
                                                          FirebaseDatabase.getInstance().getReference("Team").child(value.team)
                                                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                      @Override
                                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                                          Team value = dataSnapshot.getValue(Team.class);
                                                                          FirebaseDatabase.getInstance().getReference("Team").child(value.id).child("isFin").setValue(true);
                                                                      }
                                                                      @Override
                                                                      public void onCancelled(DatabaseError databaseError) {}
                                                                  });
                                                      }
                                                      @Override
                                                      public void onCancelled(DatabaseError databaseError) {}
                                                  });
                                          Toast.makeText(getApplicationContext(),
                                                  "Finished this Activity", Toast.LENGTH_SHORT).show();
                                      }else {
                                          FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                          User value = dataSnapshot.getValue(User.class);
                                                          FirebaseDatabase.getInstance().getReference("Team").child(value.team)
                                                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                      @Override
                                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                                          Team value = dataSnapshot.getValue(Team.class);
                                                                          int temp = Integer.parseInt(value.checkp) + 1;
                                                                          FirebaseDatabase.getInstance().getReference("Team").child(value.id).child("checkp").setValue(Integer.toString(temp));
                                                                      }
                                                                      @Override
                                                                      public void onCancelled(DatabaseError databaseError) {}
                                                                  });
                                                      }
                                                      @Override
                                                      public void onCancelled(DatabaseError databaseError) {}
                                                  });
                                      }
                                      startActivity(new Intent(AugmentedImageActivity.this, MainActivity.class));
                                  }else {

                                      Toast.makeText(getApplicationContext(),
                                              "Try Again.", Toast.LENGTH_SHORT).show();

                                  }

                              }
                          });

                      }
                  });

              }else {
                  Toast.makeText(getApplicationContext(),
                          "Finished this Activity", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(AugmentedImageActivity.this, MainActivity.class));
              }

          }
      });







      arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
      fitToScanView = findViewById(R.id.image_view_fit_to_scan);
    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (augmentedImageMap.isEmpty()) {
      fitToScanView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Registered with the Sceneform Scene object, this method is called at the start of each frame.
   *
   * @param frameTime - time since last frame.
   */

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void onUpdateFrame(FrameTime frameTime) {

    Frame frame = arFragment.getArSceneView().getArFrame();

    // If there is no frame, just return.
    if (frame == null) {
      return;
    }

    Collection<AugmentedImage> updatedAugmentedImages =
        frame.getUpdatedTrackables(AugmentedImage.class);
    for (AugmentedImage augmentedImage : updatedAugmentedImages) {
      switch (augmentedImage.getTrackingState()) {
        case PAUSED:
          // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
          // but not yet tracked.
          String text = "Detected Image " + augmentedImage.getName();
          SnackbarHelper.getInstance().showMessage(this, text);
          break;

        case TRACKING:
          // Have to switch to UI Thread to update View.
            SnackbarHelper.getInstance().hide(this);
          fitToScanView.setVisibility(View.GONE);
          if(node == null){
              if (!augmentedImageMap.containsKey(augmentedImage)) {

                  if (augmentedImage.getName().equals("up.jpeg")) {
                      node = new AugmentedImageNode(this, R.raw.q1fixsize2);
                      node.setImage(augmentedImage);
                      arFragment.getArSceneView().getScene().addChild(node);
                  }else if (augmentedImage.getName().equals("down.jpeg")) {
                      node = new AugmentedImageNode(this, R.raw.mac);
                      node.setImage(augmentedImage);
                      arFragment.getArSceneView().getScene().addChild(node);
                  }
              }
          }
          break;

          // Create a new anchor for newly found images.




        case STOPPED:
          augmentedImageMap.remove(augmentedImage);
          break;
      }
    }

  }
}
