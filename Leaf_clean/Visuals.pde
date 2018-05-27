PFont[] font;

String[] img = {"back.png", "leaf.png", "lightoff.png", "lighton.png", "next.png", "nosound.png", "yessound.png", "Lamp 2.png", "Lamp 3.png", "Lamp 4.png", "Lamp 5.png", "Lamp 6.png", "Lamp 7.png", "Lamp 8.png"};
PImage[] images = new PImage[img.length];

String[] names = {"Light Gentle Rain", "Forest", "Gentle Creek"};
byte checkmate;

boolean used = false;
boolean on = true;
int lastX = mouseX;
int lastY = mouseY;

void top() {
  fill(#262626);
  rect(0, height*9/10, width, height);
}

void texttop() {
  fill(#7ED321);
  textAlign(CENTER);
  textSize(width/20);
  text("Leaf", width/2, height/16);
}

void part1() {
  fill(30);
  rect(0, height/10, width, height*4/10);
  stroke(#262626);
  strokeWeight(width/100);
  line(0, height/2, width, height/2);
  imageMode(CORNERS);
  image(images[0], width*2/12, height*8.6/38);
  image(images[4], width*9.5/12, height*8.6/38);
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
    rect(width*2/12, height*4.075/11, width*8/12, height*0.8/11, width); //button sound
    fill(255);
    text("SOUND ON", width/2, height*5/12);
  } else if (on==true) {
    image(images[6], width/2, height*2/12); //sound off icon
    fill(#7ED321);
    rect(width*2/12, height*4.075/11, width*8/12, height*0.8/11, width); //button sound
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

void part2() {
  fill(30);
  rect(0, height*5/10, width, height*4/10);
  fill(#7ED321);
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

void bottom() {
  textAlign(CENTER);
  fill(#7ED321);
  textSize(width/30);
  text("Sitting here for", width/2, height*27.9/30);
  fill(#5A5A5A);
  textSize(width/15);
  text(nf(sw.hour(), 2)+":"+nf(sw.minute(), 2)+":"+nf(sw.second(), 2), width/2, height*29.2/30);
  fill(#262626);
  rect(0, 0, width, height/10);
}

void kolor() {
  if (on==false) {
    fill(#5A5A5A);
  } else if (on == true) {
    fill(#7ED321);
  }
}
