// The bluetooth module functioning logic is created based on Prof. Takaya's blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
// However, the logic was changed radically, so that it worked beyond booleans and so that I could send any information between Processing and Arduino
// Despite having referenced Prof. Takaya's blog on multiple occassions, it must be stated at this point, that the Prof. has used Ketai library
// Therefore all the logic is based on the library and therefore we treated his code as a reference code for running Bluetooth
// The reference code does nothing beyond launching Arduino's LED and printing information, as it whether it is on or off
// We managed to send integers back and forth via a selection of variable transformations

//The StopWatchTimer is a class built on java and created by a user name
//https://forum.processing.org/one/topic/timer-in-processing#25080000001238364.html

//The Music library was found on a Processing Discussion Board, parts of the code was appropriated from a user named kfrajer
//https://forum.processing.org/two/discussion/19302/using-android-mediaplayer-to-play-sound

import android.content.Intent;
import android.os.Bundle;
import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;
import android.os.Environment;
import cassette.audiofiles.SoundFile; 
import android.media.MediaPlayer; 

SoundFile music;
import java.util.*;

//int wave[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1}; //This array enables a stroke glowing animation
//int i = 0; //Relates to the animations and wave array
//int time = 150; //simple timer, 5 seconds

void setup() {
  font = new PFont[]{
    createFont("Avenir.ttc", 32), 
    createFont("Avenir-Next.ttc", 32), 
    createFont("Avenir-Next-Condensed.ttc", 32)};
  background(0);
  frameRate(30);
  // Images must be in the "data" directory to load correctly
  fullScreen(P2D);
  strokeWeight(4);
  orientation(PORTRAIT);


  savedTime = millis();
  sw = new StopWatchTimer();
  sw.start();
  for (int i=0; i< img.length; i++) {
    String imgs = img[i];
    images[i] = loadImage(imgs);
  }

  //start listening for BT connections
  bt.start();
  //at app start select device…
  isConfiguring = true;
  //font size
  fontMy = createFont("SansSerif", 40);
  textFont(fontMy);
  selectionUpdate(1);
}

void draw() {

  background(50);

  //Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
  //at app start select device
  if (isConfiguring)
  {
    ArrayList names;
    background(78, 93, 75);
    klist = new KetaiList(this, bt.getPairedDeviceNames());
    isConfiguring = false;
  } else
  {
    textFont(font[2]);
    top();
    bottom();
    texttop();
    part1();
    part2();
  }
}
