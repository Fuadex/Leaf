// The bluetooth module functioning logic is created based on Prof. Takaya's blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
// However, the logic was changed radically, so that it worked beyond booleans and so that I could send any information between Processing and Arduino
// Despite having referenced Prof. Takaya's blog on multiple occassions, it must be stated at this point, that the Prof. has used Ketai library
// Therefore all the logic is based on the library and therefore we treated his code as a reference code for running Bluetooth
// The reference code does nothing beyond launching Arduino's LED and printing information, as it whether it is on or off

import android.content.Intent;
import android.os.Bundle;
import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;
import cassette.audiofiles.SoundFile;
SoundFile music;

PFont fontMy;
boolean bReleased = true; //no permament sending when finger is tapped
KetaiBluetooth bt;
boolean isConfiguring = true;
String info = "";
KetaiList klist;
ArrayList devicesDiscovered = new ArrayList();

int wave[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1}; //This array enables a stroke glowing animation
int i = 0; //Relates to the animations and wave array
int time = 150; //simple timer, 5 seconds

int fileN;

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

void setup() {
  background(0);
  frameRate(30);
  // Images must be in the "data" directory to load correctly
  fullScreen(P2D);
  strokeWeight(4);
  orientation(PORTRAIT);

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
       

      }
      
      fill(255);
      rect(width/3,width/4,width/3,width/8);
      textSize(width/20);
      fill(0);
      text("Next Sound", width/2.7, width/3);
      
            fill(255);
      rect(width/3,width/9,width/3,width/8);
      textSize(width/20);
      fill(0);
      text("Music Stop", width/2.7, width/5);
      
                if (mousePressed && (mouseX > width/3 && mouseX < width*2/3) && (mouseY > width/9 && mouseY < width*3/8)){
        music.stop();
      delay(400);
    }
      
          if (mousePressed && (mouseX > width/3 && mouseX < width*2/3) && (mouseY > width/4 && mouseY < width*3/8)){
      music.stop();
      selectionUpdate(int(random(1,2)));
      println(fileN);
        music.play();
      delay(400);
    }
      
    
        if (mousePressed && (mouseX > width/4 && mouseX < width*3/4) && (mouseY > height*3/8 && mouseY < height*5/8)){
float na = map(mouseX, width/4, width*3/4, 0, 127);
byte naa = (byte)na;
            bReleased = true;
            byte[] data = {naa};
            bt.broadcast(data);
            //first tap off to send next message
            bReleased = false;
            println(naa);
            delay(50);
}
fill(map(mouseX, width/4, width*3/4, 0, 255),map(mouseX, width/4, width*3/4, 0, 255),map(mouseX, width/4, width*3/4, 0, 255));
rect(width/4,height*3/8,width*1/2,height*1/8);
    }


//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
void onKetaiListSelection(KetaiList klist) {
  String selection = klist.getSelection();
  bt.connectToDeviceByName(selection);
  //dispose of list for now
  klist = null;
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
//Call back method to manage data received
void onBluetoothDataEvent(String who, byte[] data) {
  if (isConfiguring)
    return;
  //received
  info += new String(data);
  //clean if string to long
  if (info.length() > 16)
    info = "";
    
}

void selectionUpdate(int s) {
 
  fileN=s;
 
  switch(s) {
 
  case 1:
    music = new SoundFile(this, "Sound 1. Forest.wav");
    music.loop();
    break;
 
  case 2:
    music = new SoundFile(this, "Sound 4. Light Gentle Rain.mp3"); 
    music.loop();
    break;
  }

}  
