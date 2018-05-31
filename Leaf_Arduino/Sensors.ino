//The code for the force sensor was appropriated and slightly modified from
//https://learn.sparkfun.com/tutorials/force-sensitive-resistor-hookup-guide

void forcesensor() {

  // updates force
  //It seems that long numerals were not long enough, so we used double and it seems to be doing a proper job

  double fsrADC = analogRead(FSR_PIN2) / 2 + analogRead(FSR_PIN) * 4;
  float fsrV = fsrADC * VCC / 1023.0;
  // calculate FSR resistance:
  float fsrR = R_DIV * (VCC / fsrV - 1.0);
  float fsrG = 1.0 / fsrR; // Calculate conductance
  force = (fsrG - 0.00075) / 0.00000032639;
  force =  fsrG / 0.000000642857;

}

//The ultrasonic sensor was the most difficult one to reign over
//We had to filter out the values so that we received precise amounts
//Otherwise we imposed a limitation to provide a reading equal of 0

void ultrasonic() {

  //Here in order to optimize the code, we made an array of values
  //And looped through them appropriately delay by 1ms and stored safely

int d[9];
int uS[9];
      
    for (int i=0; i<9; i++){
      uS[i] = sonar.ping();
      d[i] = (uS[i] / US_ROUNDTRIP_CM);
      delay(2);
    }

  //Here we subtracted the values. If 8 different readings had similar values, then a random reading was included in rangeInCentimeters
  //If the values left the safe zone of around 10, then the reading was preset to 0, hence treated as anomalies
  //This way we were able to minimize the amount of aberrations picked up by our ultrasonic sensor
  
  if (((d[8] - d[7]) - (d[6] - d[5])) - ((d[4] - d[3]) - (d[2] - d[1])) > -5 && ((d[8] - d[7]) - (d[6] - d[5])) - ((d[4] - d[3]) - (d[2] - d[1])) < 5) {

    rangeInCentimeters = d[random(1,8)];
  }
  else if (((d[8] - d[7]) - (d[6] - d[5])) - ((d[4] - d[3]) - (d[2] - d[1])) < -5 || ((d[8] - d[7]) - (d[6] - d[5])) - ((d[4] - d[3]) - (d[2] - d[1])) > 5) {
    rangeInCentimeters = 0;
  }

Serial.println(rangeInCentimeters);
  
}

//This is the motion sensor
//So straightforward, that it just returns a boolean
//Quite nice, huh?

void pir() {
  pirsensor = digitalRead(PIRPIN);
  Serial.println(pirsensor);
}
