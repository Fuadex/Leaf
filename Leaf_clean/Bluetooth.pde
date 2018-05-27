PFont fontMy;

boolean bReleased = true; //no permament sending when finger is tapped
KetaiBluetooth bt;
boolean isConfiguring = true;
String info = "";
KetaiList klist;
ArrayList devicesDiscovered = new ArrayList();

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
}

//Following void appropriated from Prof. Takaya's Blog: http://prof-takaya.blogspot.com.au/2013/02/connecting-android-and-arduino-by.html
void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
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
