package processing.test.leaf_clean;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.content.Intent; 
import android.os.Bundle; 
import ketai.net.bluetooth.*; 
import ketai.ui.*; 
import ketai.net.*; 
import android.os.Environment; 
import cassette.audiofiles.SoundFile; 
import android.media.MediaPlayer; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Leaf_clean extends PApplet {

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







 
 

SoundFile music;


//int wave[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1}; //This array enables a stroke glowing animation
//int i = 0; //Relates to the animations and wave array
//int time = 150; //simple timer, 5 seconds

public void setup() {
  font = new PFont[]{
    createFont("Avenir.ttc", 32), 
    createFont("Avenir-Next.ttc", 32), 
    createFont("Avenir-Next-Condensed.ttc", 32)};
  background(0);
  frameRate(30);
  // Images must be in the "data" directory to load correctly
  
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

public void draw() {

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
PFont fontMy;

boolean bReleased = true; //no permament sending when finger is tapped
KetaiBluetooth bt;
boolean isConfiguring = true;
String info = "";
KetaiList klist;
ArrayList devicesDiscovered = new ArrayList();

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
public void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html

public void onKetaiListSelection(KetaiList klist) {
  String selection = klist.getSelection();
  bt.connectToDeviceByName(selection);
  //dispose of list for now
  klist = null;
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
//Call back method to manage data received
public void onBluetoothDataEvent(String who, byte[] data) {
  if (isConfiguring)
    return;
  //received
  info += new String(data);
  //clean if string to long
  if (info.length() > 16)
    info = "";
}
//The Music library was found on a Processing Discussion Board, parts of the code was appropriated from a user named kfrajer
//https://forum.processing.org/two/discussion/19302/using-android-mediaplayer-to-play-sound

int fileN;
int song = 0;

public void selectionUpdate(int s) {

  fileN=s;

  switch(s) {

  case 0:
    music = new SoundFile(this, "Forest.mp3");
    music.loop();
    break;

  case 1:
    music = new SoundFile(this, "Light Gentle Rain.mp3"); 
    music.loop();
    break;

  case 2:
    music = new SoundFile(this, "Gentle Creek with Cicadas.mp3");
    music.loop();
    break;
  }
}

public void songshuffle() {
  song++;
  if (song >2) {
    song = 0;
  }
  music.stop();
  selectionUpdate(song);
  println(fileN);
  music.play();
  delay(400);
}

public void songshuffle1() {
  song--;

  if (song <0 ) {
    song = 2;
  }
  music.stop();
  selectionUpdate(song);
  println(fileN);
  music.play();
  delay(400);
}

public void songshuffle2() {
  music.stop();
  selectionUpdate(song);
  println(fileN);
  music.play();
  delay(400);
}
//The StopWatchTimer is a class built on java and created by a user name
//https://forum.processing.org/one/topic/timer-in-processing#25080000001238364.html

StopWatchTimer sw;
int savedTime;
int totalTime = 5000;
static long CurrentTime = System.currentTimeMillis();

class StopWatchTimer {
  int startTime = 0, stopTime = 0;
  boolean running = false; 
  public void start() {
    startTime = millis();
    running = true;
  }
  public void stop() {
    stopTime = millis();
    running = false;
  }
  public int getElapsedTime() {
    int elapsed;
    if (running) {
      elapsed = (millis() - startTime);
    } else {
      elapsed = (stopTime - startTime);
    }
    return elapsed;
  }
  public int second() {
    return (getElapsedTime() / 1000) % 60;
  }
  public int minute() {
    return (getElapsedTime() / (1000*60)) % 60;
  }
  public int hour() {
    return (getElapsedTime() / (1000*60*60)) % 24;
  }
}
PFont[] font;

String[] img = {"back.png", "leaf.png", "lightoff.png", "lighton.png", "next.png", "nosound.png", "yessound.png", "Lamp 2.png", "Lamp 3.png", "Lamp 4.png", "Lamp 5.png", "Lamp 6.png", "Lamp 7.png", "Lamp 8.png"};
PImage[] images = new PImage[img.length];

String[] names = {"Light Gentle Rain", "Forest", "Gentle Creek"};
byte checkmate;

boolean used = false;
boolean on = true;
int lastX = mouseX;
int lastY = mouseY;

public void top() {
  fill(0xff262626);
  rect(0, height*9/10, width, height);
}

public void texttop() {
  fill(0xff7ED321);
  textAlign(CENTER);
  textSize(width/20);
  text("Leaf", width/2, height/16);
}

public void part1() {
  fill(30);
  rect(0, height/10, width, height*4/10);
  stroke(0xff262626);
  strokeWeight(width/100);
  line(0, height/2, width, height/2);
  imageMode(CORNERS);
  image(images[0], width*2/12, height*8.6f/38);
  image(images[4], width*9.5f/12, height*8.6f/38);
  //println(song);
  imageMode(CENTER);
  if (mousePressed && mouseX > width*2/12 && mouseX < width*10/12 && mouseY > height*3/11 && mouseY < height*5/11) {
    on = !on;

    if (on==false) {
      music.stop();
    } else if (on==true) {
      //music.stop();
      //selectionUpdate(song);
      //music.play();
      songshuffle2();
    }
    delay(400);
  }

  if (on==false) {
    image(images[5], width/2, height*2/12); //sound off icon
    //fill(#262626);
    rect(width*2/12, height*4.075f/11, width*8/12, height*0.8f/11, width); //button sound
    fill(255);
    text("SOUND ON", width/2, height*5/12);
  } else if (on==true) {
    image(images[6], width/2, height*2/12); //sound off icon
    fill(0xff7ED321);
    rect(width*2/12, height*4.075f/11, width*8/12, height*0.8f/11, width); //button sound
    fill(255);
    text("SOUND OFF", width/2, height*5/12);
  }

  strokeWeight(0);
  //
  if (mousePressed && mouseX>width*9/12 && mouseX<width*11/12 && mouseY>height*8/38 && mouseY<height*15/38) {
    //   fill(#5A5A5A);
    //textAlign(CENTER);
    //textSize(width/20);
    //text("WTF", width/2, height*3/12);
    if (on==true) {
      songshuffle();
    }
  } else if (mousePressed && mouseX>width*1/12 && mouseX<width*3/12 && mouseY>height*8/38 && mouseY<height*15/38) {
    //   fill(#5A5A5A);
    //textAlign(CENTER);
    //textSize(width/20);
    //text("WTF", width/2, height*3/12);
    if (on==true) {
      songshuffle1();
    }
  }
  kolor();
  textAlign(CENTER);
  textSize(width/20);
  text(names[song], width/2, height*3/12);
}

public void part2() {
  fill(30);
  rect(0, height*5/10, width, height*4/10);
  fill(0xff7ED321);
  int slider = parseInt(constrain(map(lastX, width/12, width*11/12, 0, 100), 0, 100)); //Based on last position of recorded mouse, the value changes
  text(slider+"%", width/2, height*8/10);

  stroke(150);
  strokeWeight(width/70);
  line(width*1/10, height*7/10, width*9/10, height*7/10);
  strokeWeight(0);

  if (mousePressed && mouseX > width/20 && mouseX < width*19/20 && mouseY > height*6/10 && mouseY < height*8/10) {
    //image(images[1], pmouseX, height*7/10); //leaf
    used = true;
    lastX=pmouseX;
    lastY=pmouseY;
  }
  if (used == false) {
    image(images[1], width/12, height*7/10); //leaf
  } else if (used == true) {
    image(images[1], constrain(lastX, width/12, width*11/12), height*7/10); //leaf

    float na = map(constrain(map(lastX, width/12, width*11/12, 0, 100), 0, 100), 0, 100, 0, 127);
    byte naa = (byte)na;
    if (checkmate != naa) {
      checkmate=naa;
      bReleased = true;
      byte[] data = {naa};
      bt.broadcast(data);
      //first tap off to send next message
      bReleased = false;
      println(naa);
      delay(50);
    } else if (checkmate == naa) {
    }
  }

  if (slider==0) {
    image(images[2], width/2, height*6/10); //light bulb
  } else if (slider!=0) {
    float mappedslider = map(slider, 0, 100, 100, 255); //mapping slider to match rgb values
    //println(mappedslider);
    //tint(100, 255, mappedslider);
    //images[3].resize(parseInt((mappedslider-4)/2),parseInt(mappedslider/2));
    //noSmooth();
    tint(mappedslider, mappedslider, mappedslider); //resetting tint
    if (slider>0 && slider <100/7) {
      image(images[7], width/2, height*6/10, mappedslider/2, mappedslider/2); //light bulb
    } else if (slider>=100/7 && slider<100/7*2) {
      image(images[8], width/2, height*6/10, (mappedslider-width/60)/2, mappedslider/2);
    } else if (slider>=100/7*2 && slider<100/7*3) {
      image(images[9], width/2, height*6/10, (mappedslider-width/60)/2, mappedslider/2);
    } else if (slider>=100/7*3 && slider<100/7*4) {
      image(images[10], width/2, height*6/10, (mappedslider-width/60)/2, mappedslider/2);
    } else if (slider>=100/7*4 && slider<100/7*5) {
      image(images[11], width/2, height*6/10, (mappedslider-width/60)/2, mappedslider/2);
    } else if (slider>=100/7*5 && slider<100/7*6) {
      image(images[12], width/2, height*6/10, (mappedslider-width/60)/2, mappedslider/2);
    } else if (slider>=100/7*6) {
      image(images[13], width/2, height*6/10, mappedslider/2, mappedslider/2);
    }
    tint(255); //resetting tint
  }
}

public void bottom() {
  textAlign(CENTER);
  fill(0xff7ED321);
  textSize(width/30);
  text("Sitting here for", width/2, height*27.9f/30);
  fill(0xff5A5A5A);
  textSize(width/15);
  text(nf(sw.hour(), 2)+":"+nf(sw.minute(), 2)+":"+nf(sw.second(), 2), width/2, height*29.2f/30);
  fill(0xff262626);
  rect(0, 0, width, height/10);
}

public void kolor() {
  if (on==false) {
    fill(0xff5A5A5A);
  } else if (on == true) {
    fill(0xff7ED321);
  }
}
  public void settings() {  fullScreen(P2D); }
}
