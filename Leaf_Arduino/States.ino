//In order to find out whether anyone is detected, we have to check all of the sensors
//If someone sits, then it gets triggered
//If the ultrasonic sensor picks up on something, it gets triggered
//If the pir sensor picks up someone, it gets triggered
//If not, continue with the regular normal glow

int someonedetected() {

  int break_status;
  forcesensor();
  ultrasonic();
  pir();

  if (force > 260 ) {

    break_status = 1;

  }  else if (pirsensor) {
    break_status = 1;
  }
  else if (rangeInCentimeters > 20 || rangeInCentimeters < 250) {
    break_status = 1;
  }
  else {
    break_status = 0;
  }

  return break_status;

}

//Regular, normal glow, Idle state, nobody picked up
//The function uses a nested for loop to increase progressively the brightness (one loop) of all the LEDs in the Strip (nested) and then back down
//However, considering the function takes a long time, we had to make checks as to whether anything happened in the middle of it
//Therefore the glow will break once someone is detected

void glow1 () {

  for (int x = 5; x <= 90; x++) {
    someonedetected();
    if (force > 260) {
      someoneissitting();
    }
    else if (rangeInCentimeters > 20 && rangeInCentimeters < 250) {
      glow3();
    }
    else if (pirsensor) {
      glow2();
    }
    else {

      for (int i = 0; i < 60; i++) {
        leds[i] = CRGB(x, x, x);
      }
      delay(30);
      // Show the leds
      FastLED.show();

    }
  }

  for (int x = 90; x >= 5; x--) {
    someonedetected();
    if (force > 260) {
      someoneissitting();
    }
    else if (rangeInCentimeters > 20 && rangeInCentimeters < 250) {
      glow3();
    }
    else if (pirsensor) {
      glow2();
    }
    else {
      for (int i = 0; i < 60; i++) {
        leds[i] = CRGB(x, x, x);
      }
      delay(45);
      // Show the leds
      FastLED.show();

    }
  }
}

//Motion Sensor detects someone in range
//Introduce a hastened glow

void glow2() {
  for (int x = 15; x <= 50; x++) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    delay(5);
    // Show the leds
    FastLED.show();
  }
  for (int x = 50; x >= 15; x--) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    delay(5);
    // Show the leds
    FastLED.show();
  }
}

//Ultrasonic Sensor detects someone in range
//Introduce an aggressive glow

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

//Introduce a static lightning mode
//Listen for incoming bytes and introduce appropriate changes

void someoneissitting() {

  chByte = Serial.read();
  //    Serial.println(chByte);

  if (chByte != 255) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(chByte, chByte, chByte);
    }
    // Show the leds
    FastLED.show();
  }
}
