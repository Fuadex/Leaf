

void someoneissitting() {


  chByte = Serial.read();
  //    Serial.println(chByte);
  //  if (chByte == 255){
  //  static byte chByte = 120;
  //  }
  if (chByte != 255) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(chByte, chByte, chByte);
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
    if (force > 260) {
      someoneissitting();
    }
    else if (rangeInCentimeters > 20 && rangeInCentimeters < 240) {
      glow3();
    }
    else if (pir) {
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

  //  int breakstatus = someonedetected();
  //  if (breakstatus == 1) {
  //    return;
  //  }

  for (int x = 90; x >= 5; x--) {
    someonedetected();
    if (force > 260) {
      someoneissitting();
    }
    else if (rangeInCentimeters > 20 && rangeInCentimeters < 240) {
      glow3();
    }
    else if (pir) {
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

void glow2() {
  for (int x = 15; x <= 50; x++) {
    for (int i = 0; i < 60; i++) {
      leds[i] = CRGB(x, x, x);
    }
    //                                    if (force > 100 || (rangeInCentimeters > 20 && rangeInCentimeters < 240)){
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
    //                          if (force > 100 || (rangeInCentimeters > 20 && rangeInCentimeters < 240)){
    //        break;
    //      }
    delay(5);
    // Show the leds
    FastLED.show();
  }
}



//Based on the feedback, we introduced light blink after half an hour has passed
