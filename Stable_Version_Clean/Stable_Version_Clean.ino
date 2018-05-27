//The Project featured here is based on codes that were developed throughout the MIDEA9101 & MIDEA9102 courses
//Some code was recycled from the first assignment for MIDEA9101, primarily the LED Strip and bluetooth logic
//Force sensor logic was appropriated from Sparkfun https://learn.sparkfun.com/tutorials/force-sensitive-resistor-hookup-guide
//Bluetooth logic was appropriated from Prof. Takaya's blog, although no code really prevailed: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
//Some fixes within the code logic and sorting, along with many suggestions that were provided and tested, were all delivered by the teaching staff of MIDEA9101 and MIDEA9102

#include <NewPing.h> //That's the ultrasonic sensor
#include "FastLED.h" //That's the LED Strip

#define PIRPIN 7 //Motion Sensor
#define DATA_PIN 8 //LED Strip PIN
#define TRIGGER_PIN  12 //Ultrasonic transmitting
#define ECHO_PIN  13 //Ultrasonic receiving 

#define NUM_LEDS 60 //Number of LED Lights
#define MAX_DISTANCE 250 //It was 300 but the most it picked up was 255 (tested)

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); //Ultrasonic logic

// LED Strip logic
CRGB leds[NUM_LEDS];
CRGBPalette16 currentPalette;

//  Variables
byte chByte;  // incoming serial byte
const int FSR_PIN = A0; // Pin connected to FSR/resistor divider
const int FSR_PIN2 = A1; // Pin connected to FSR/resistor divider
const float VCC = 4.98; // Measured voltage of Ardunio 5V line
const float R_DIV = 330.0; // Measured resistance of 3.3k resistor
unsigned long force; //Instead of float, unsigned long
unsigned int rangeInCentimeters;
bool pirsensor;

void setup()
{
  FastLED.addLeds<WS2811, DATA_PIN, RGB>(leds, NUM_LEDS);
  Serial.begin(115200); //The 115200 is a must for mBot's bluetooth
  pinMode(FSR_PIN, INPUT);
  pinMode(FSR_PIN2, INPUT);
  pinMode(PIRPIN, INPUT);
}

void loop()
{
  pir();
  forcesensor();
  ultrasonic();

  //    Begins the cascade: If someone is sitting (and weighs more than 260grams) will trigger a code for constant light
  //    If someone is picked up by the ultrasonic sensor, a hasty and aggressive glow will turn on
  //    If someone is picked up by the pir sensor, the glow will be a bit hasty, but rather enticing
  //    If nobody is around, the glow will be quite prolonged and normal

  
  if (force > 260) {  // if someone is sitting
    someoneissitting();
  } else if (rangeInCentimeters > 20 && rangeInCentimeters < 250) { // anyone within 2m,
    glow3();
  }  else if (pirsensor == true) { // anyone withing 3 to 7m
    glow2();
  } else {
    glow1();
  }

}

