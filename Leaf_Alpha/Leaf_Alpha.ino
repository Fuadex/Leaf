// The code featured here will react correspondingly with an RGB LED Strip, Ultrasonic sensor and a RGB LED
// Some code was appropriated from Prof. Takaya's Blog found here: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
// Although the code is rather subtle and only includes some bluetooth logic, that is still relatively limited, as any print actually goes through the bluetooth module anyway
// A bit more code was adopted from Day 5 Studio Ultrasonic tutorial for IDEA9102
// In this case I used the detection logic, although that's as far as I went
// Some code might overlap with IDEA9102 code that was used for RGB LED Strip
// However, I have introduced the logic and appropriated the code entirely by myself and actually tried relating more to the official FastLED library overview featured on GitHub
// I have also used oversampling for the ultrasonic sensor by the use of Average.h library, in order to make the values more stable
// I used to use mBot's libraries to get the bluetooth module up and running, but surprisingly, to work it doesn't need it anymore ._.

// I'd like to think that this project could be useful for the Studio project, to collect data and provide a base for our project altogether, if we decided to use a bluetooth module
// It is effective and stable and works reasonably well with the environment. Although, I would use a more effective area scanning sensor, such as infrared and perhaps a secure bluetooth module
// Otherwise, I'd consider the setup robust enough to be able to take it further, if neccessary!

#include <Average.h>
#include <SoftwareSerial.h>
#include <NewPing.h>
#include "FastLED.h"

#define NUM_LEDS 60
#define DATA_PIN 8

#define TRIGGER_PIN  12
#define ECHO_PIN  13
#define LED_PIN 9 //Blue LED
#define LED_PIN2 10 //Green LED
#define LED_PIN3 11 //Blue LED
#define MAX_DISTANCE 240 //It was 300 but the most it picked up was 255, but to make it more nicely dividable, I made it max 240
#define PIRPIN 7

Average <int>sonic(20); //That makes an aggregate based on 20 sensor readings
//SoftwareSerial mySerial(0, 1); // RX, TX
NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

// Define the array of leds
CRGB leds[NUM_LEDS];
CRGBPalette16 currentPalette;

//Some obsolete code that I tested in relation to Average library
//int hue = 0;
//int over[4];

//  Variables
const int led = 13;   //  The on-board Arduion LED
byte chByte;  // incoming serial byte
String strInput = "";    // buffer for incoming packet
String strCompare = "switch";
byte brightness;
//long rangeInCentimeters; //Code appropriated from Day 5 IDEA9102 Ultrasonics
const int SENSOR_PIN = 0;      // Analog input pin

const int FSR_PIN = A0; // Pin connected to FSR/resistor divider
const int FSR_PIN2 = A1; // Pin connected to FSR/resistor divider

// Measure the voltage at 5V and resistance of your 3.3k resistor, and enter
// their value's below:
const float VCC = 4.98; // Measured voltage of Ardunio 5V line
const float R_DIV = 330.0; // Measured resistance of 3.3k resistor


float force;
unsigned int rangeInCentimeters;
bool pir;

void setup()
{
  FastLED.addLeds<WS2811, DATA_PIN, RGB>(leds, NUM_LEDS);
  Serial.begin(115200); //The 115200 is a must for mBot's bluetooth
  Serial.println("Blueeeetoooooth!"); //Just checkin' if the serial.print is alright!
  pinMode(LED_PIN, OUTPUT);
  pinMode(LED_PIN2, OUTPUT);
  pinMode(LED_PIN3, OUTPUT);
  pinMode(FSR_PIN, INPUT);
  pinMode(FSR_PIN2, INPUT);
  pinMode(PIRPIN, INPUT);
}

void loop()
{

  //Infrared code is technically here

  
  pir = digitalRead(PIRPIN);



  forcesensor();
  ultrasonic();


  //The following conditionals will check for incoming bytes and respond to poll results for a specific amount of time
  //Due to me not being able to send and receive integers, I had to issue manual checks. Very cumbersome, but neccessary.

  //    delay(20);                      // Wait 50ms between pings (about 20 pings/sec). 29ms should be the shortest delay between pings.


  // These are here so that Arduino can actually compile the next code. rgb corresponds with red, green, blue values

  unsigned int r = 0;
  unsigned int g = 0;
  unsigned int b = 0;

  //  Serial.println(sonic.mode());

  // Check if something in range, glow if there is

  int sensorValue;
  unsigned int bupa;
  // Read the voltage from the softpot (0-1023)
  sensorValue = analogRead(SENSOR_PIN);
  bupa = map(sensorValue, 0, 1023, 0, 120);
  //    Serial.println(bupa);

  //    Begin the cascade


  if (force > 500) {  // if someone is sitting
    someoneissitting();
  } else if (sonic.mode() > 1 && sonic.mode() < 200) { // anyone within 2m,
    glow3();
  }  else if (pir) { // anyone withing 3 to 7m
    glow2();
  } else {
    glow1();
  }

}



void ultrasonic() {

  unsigned int uS = sonar.ping(); // Send ping, get ping time in microseconds. - Code from IDEA9102 Studio (Happyface Arduino)

  unsigned int distance = 0;
  rangeInCentimeters = (uS / US_ROUNDTRIP_CM);

  for (int i = 0; i < 20; i++) {
    unsigned int cm = sonar.ping_cm();
    sonic.push(constrain(cm, 1, 240)); //I had to constrain this value, as at times it jumped to exorbitant values for no reason (and obliterated my for loops further on)
  }
}



int someonedetected() {

  int break_status;
  forcesensor();
  ultrasonic();
  pir;
  if (force > 500 ) {

    break_status = 1;

  }  else if (pir) {
    break_status = 1;
  }
  else if (sonic.mode() > 1 || sonic.mode() < 200) {
    break_status = 1;
  }
  else {
    break_status = 0;
  }

  return break_status;

}



void forcesensor() {

  // updates force

 // I looked for variable types that can store the largest numbers, as my pressure sensors produced some immence types
  //It seems that long numerals were not long enough, so I chucked in double and it seems to be doing a proper job

  double fsrADC = analogRead(FSR_PIN2) + analogRead(FSR_PIN);
  // If the FSR has no pressure, the resistance will be
  // near infinite. So the voltage should be near 0.
  //  if (fsrADC != 0) // If the analog reading is non-zero
  //  {
  // Use ADC reading to calculate voltage:
  float fsrV = fsrADC * VCC / 1023.0;
  // Use voltage and static resistor value to
  // calculate FSR resistance:
  float fsrR = R_DIV * (VCC / fsrV - 1.0);
  //    Serial.println("Resistance: " + String(fsrR) + " ohms");
  // Guesstimate force based on slopes in figure 3 of
  // FSR datasheet;
  float fsrG = 1.0 / fsrR; // Calculate conductance
  //    // Break parabolic curve down into two linear slopes:
  //    if (fsrR <= 600)
  force = (fsrG - 0.00075) / 0.00000032639;
  //    else
  force =  fsrG / 0.000000642857;
  //    Serial.println("Force: " + String(force) + " g");
  //    Serial.println();
  //  }
  
  //Serial.println(force);
  // Some of the following bluetooth code was appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
  // However, the logic is quite limited and I made alterations so that it worked. In fact, most of the code seems gone, as it's rather limited to reading and printing values
  // get incoming byte:
  //mySerial.println(mySerial.read().toInt());

}


void someoneissitting() {


  chByte = Serial.read();
  Serial.println(chByte);
  //  if (chByte == 255){
  //  static byte chByte = 120;
  //  }
  if (chByte != 255){
  for (int i = 0; i < 60; i++) {
    leds[i] = CRGB(chByte / 2, chByte / 2, chByte / 2);
  }
  // Show the leds
  FastLED.show();
}
}


void glow3() {



  for (int x = 15; x <= 90; x++) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    delay(2);
    // Show the leds
    FastLED.show();
  }
  for (int x = 90; x >= 15; x--) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    delay(2);
    // Show the leds
    FastLED.show();
  }

}

void glow1 () {


  //gloweffect



  for (int x = 5; x <= 90; x++) {
  someonedetected();
  if (force > 500){
    someoneissitting();
  }
  else if (sonic.mode() > 1 && sonic.mode() < 200){
    glow3();
  }
  else if (pir){
    glow2();
  }
  else{
    
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    delay(30);
    // Show the leds
    FastLED.show();

  }
  }  

//  int breakstatus = someonedetected();
//  if (breakstatus == 1) {
//    return;
//  }

  for (int x = 90; x >= 5; x--) {
        someonedetected();
  if (force > 500){
    someoneissitting();
  }
  else if (sonic.mode() > 1 && sonic.mode() < 200){
    glow3();
  }
  else if (pir){
    glow2();
  }
  else{
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    delay(45);
    // Show the leds
    FastLED.show();

  }
  }
}

void glow2() {
  for (int x = 15; x <= 50; x++) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    //                                    if (force > 100 || (sonic.mode() > 1 && sonic.mode() < 200)){
    //        break;
    //      }
    delay(5);
    // Show the leds
    FastLED.show();
  }
  for (int x = 50; x >= 15; x--) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    //                          if (force > 100 || (sonic.mode() > 1 && sonic.mode() < 200)){
    //        break;
    //      }
    delay(5);
    // Show the leds
    FastLED.show();
  }
}



//Based on the feedback, we introduced light blink after half an hour has passed
