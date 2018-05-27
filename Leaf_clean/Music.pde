//The Music library was found on a Processing Discussion Board, parts of the code was appropriated from a user named kfrajer
//https://forum.processing.org/two/discussion/19302/using-android-mediaplayer-to-play-sound

int fileN;
int song = 0;

void selectionUpdate(int s) {

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

void songshuffle() {
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

void songshuffle1() {
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

void songshuffle2() {
  music.stop();
  selectionUpdate(song);
  println(fileN);
  music.play();
  delay(400);
}
