package processing.test.question_;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.content.Intent; 
import android.os.Bundle; 
import ketai.net.bluetooth.*; 
import ketai.ui.*; 
import ketai.net.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Question_ extends PApplet {

// The bluetooth module functioning logic is created based on Prof. Takaya's blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
// However, I changed the logic radically, so that it worked beyond booleans and so that I could send any information between Processing and Arduino
// Despite having referenced Prof. Takaya's blog on multiple occassions, it must be stated at this point, that the Prof. has used Ketai library
// Therefore all the logic is based on the library and therefore I would treat his code as a reference code for running Bluetooth
// The reference code does nothing beyond launching Arduino's LED and printing information, as it whether it is on or off
// I studied, appropriated and extended the script so that it fully functions with Strings
// The following code can be modified to feature a fully-fledged questionnaire gathering application, which could be used in relation to the Studio project
// It is also possible to save the data natively and update the values at launch, one method I know is via saving .csv values, although many methods exist
// Potentially, the code could also work in java, although it seems that Ketai library is used rather for Processing applications build for Android
// Thank you for reviewing and reading this code. It took ages to get it 'till this point (the infamous bluetooth!)






// import processing.serial.*;

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
PFont fontMy;
boolean bReleased = true; //no permament sending when finger is tap
KetaiBluetooth bt;
boolean isConfiguring = true;
String info = "";
KetaiList klist;
ArrayList devicesDiscovered = new ArrayList();


//The following code is mine, stored values that change depending on what the user does

int wave[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1}; //This array enables a stroke glowing animation
int i = 0; //Relates to the animations and wave array
int z = 0; //state change!
float x = 0; //state change Awesome!
float v = 0; //state change Nope.
int time = 500; //simple timer
int participants = 0; //number of participants collected here


//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
public void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

public void setup() {
  frameRate(60);
  // Images must be in the "data" directory to load correctly
  
  strokeWeight(4);
  orientation(LANDSCAPE);

  //start listening for BT connections
  bt.start();
  //at app start select device\u2026
  isConfiguring = true;
  //font size
  fontMy = createFont("SansSerif", 40);
  textFont(fontMy);
}

public void draw() {

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

    //If Arduino sends a value of 0, then do nothing. Purely for debugging reasons.

    if (info.contains("0")) {
    } else {

      //The following code draws most of the page with the main question and runs the animation

      if (z == 0) {
        time = 500;
        background(59, 118, 178);
        stroke(50, 50, 50);
        strokeWeight(wave[i]);
        fill(136, 195, 255);
        rect(width/2-width/4, height/2-height/4, width/2, height/6-width/50, 50);
        fill(50, 50, 50);
        textSize(width/40);
        text("What do you think of this area?", width/3-30, height/3);
        if (i==16) {
          i=0;
        } else {
          i++;
        }

        //The following code draws the buttons

        strokeWeight(3);
        fill(126, 195, 84);
        ellipse(width/6, height/1.3f, width/8, width/8);
        fill(50, 50, 50);
        textSize(width/50);
        text("Awesome!", width/6-90, height/1.3f+15);

        fill(232, 83, 81);
        ellipse(width*5/6, height/1.3f, width/8, width/8);
        fill(50, 50, 50);
        textSize(width/50);
        text("Nope.", width*5/6-50, height/1.3f+15);

        fill(255, 189, 83);
        ellipse(width/2, height/1.3f, width/8, width/8);
        fill(50, 50, 50);
        textSize(width/50);
        text("50/50", width/2-50, height/1.3f+15);

        //The Awesome! Button action

        if (z==0 && mousePressed && (mouseX > width/6-width/8 && mouseX < width/6+width/8) && (mouseY > height/1.3f-width/8 && mouseY < height/1.3f+width/8)) {
          z++;
          x++;
          participants++;
          delay(250);
        }

        //The 50/50 button action

        if (z==0 && mousePressed && (mouseX > width/2-width/8 && mouseX < width/2+width/8) && (mouseY > height/1.3f-width/8 && mouseY < height/1.3f+width/8)) {
          z++;
          x++;
          v++;
          participants++;
          delay(250);
        }

        //The Nepe. Button action

        if (z==0 && mousePressed && (mouseX > width*5/6-width/8 && mouseX < width*5/6+width/8) && (mouseY > height/1.3f-width/8 && mouseY < height/1.3f+width/8)) {
          z++;
          v++;
          participants++;
          delay(250);
        }

        //The following code will showcase information as to how many participants took part in the survey and what's the result
        //The i value corresponds with the animation, that is looped with the use of a different method. Once it reaches the maximum value in an array, it resets
      } else if (z == 1) {
        background(59, 118, 178);
        stroke(50, 50, 50);
        strokeWeight(wave[i]);
        fill(136, 195, 255);
        rect(width/2-width/4, height/2-height/4, width/2, height/6-width/50, 50);
        fill(50, 50, 50);
        textSize(width/40);
        text("Participants: "+participants, width/3, height/3-30);
        text("Satisfaction: "+x/(x+v)*100+" %", width/3, height/3+30);
        if (i==16) {
          i=0;
        } else {
          i++;
        }

        //The following code will render appropriate visualizations of the data gathered in total

        rect(width/8, height/2, width*6/8, height/8);
        if (x/(x+v) >= 0.66f) {
          fill(0, pow(255, (x/(x+v))*1.2f), 0);
        } else if (x/(x+v) <= 0.33f) {
          fill(255-(pow(255, x/(x+v)))*10, 0, 0);
        } else if (x/(x+v) >= 0.33f && x/(x+v) < 0.66f) {
          fill(255, 255, 0);
        }
        rect(width/8+10, height/2+10, width*6/8*(x/(x+v)), height/8);
        //fill(220, 200, 200);
        //text("Satisfaction: "+x/(x+v)*100+" %", width/2-250, height/2+100);

        //The following code will send according bytes, which will trigger an RGB LED Stripe response based on mathematical equations and the remaining time left

        if (time>499) {
          if (x/(x+v) == 1.0f) {
            bReleased = false;
            byte[] data = {'Y'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.90f && x/(x+v) < 1.00f) {
            bReleased = false;
            byte[] data = {'9'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.80f && x/(x+v) < 0.90f) {
            bReleased = false;
            byte[] data = {'8'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.70f && x/(x+v) < 0.80f) {
            bReleased = false;
            byte[] data = {'7'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.60f && x/(x+v) < 0.70f) {
            bReleased = false;
            byte[] data = {'6'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.50f && x/(x+v) < 0.60f) {
            bReleased = false;
            byte[] data = {'5'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.40f && x/(x+v) < 0.50f) {
            bReleased = false;
            byte[] data = {'4'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.30f && x/(x+v) < 0.40f) {
            bReleased = false;
            byte[] data = {'3'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.20f && x/(x+v) < 0.30f) {
            bReleased = false;
            byte[] data = {'2'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.10f && x/(x+v) < 0.20f) {
            bReleased = false;
            byte[] data = {'1'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          } else if (x/(x+v) >= 0.0f && x/(x+v) < 0.10f) {
            bReleased = false;
            byte[] data = {'N'};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = true;
          }
        }
        time--; //Reduces the time with each code run

        if (time == 0) { //Once the time reaches zero, the main page will be shown
          z--;
        }
      }
      //println(z, x, v, time, x/(x+v)); //Checks the values as to whether they are being manipulated properly
    }
  }
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
  public void settings() {  fullScreen(P2D); }
}
