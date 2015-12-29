#include <Adafruit_NeoPixel.h>
#include <MoodLight.h>

#define PIXEL_PIN 10 // pin IO used for pilot the Neopixel led
#define PIXEL_COUNT 20 // number Neopixel led present on strip
int valueRead = 0; // variable used for read the serial data
byte data_in[4] {0,0,0,0}; // variable used for write data read of the serial 
int saturation = 255;  // use value between 0 - 255
int brightness = 255;  // use value between 0 - 255
int hue = 0; // use value between 0 - 359
 
// create the object strip of type Adafruit_NeoPixel
// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(PIXEL_COUNT, PIXEL_PIN, NEO_GRB + NEO_KHZ400);

// create MoodLight object
MoodLight ml = MoodLight();

void setup() {
  Serial.begin(9600);
  // inizialize the strip LED
  strip.begin();
  strip.show();
}

void loop() { 
  // save data in data_in
  if (Serial.available() > 4){
      for (int i = 0; i<4; i++) {
      valueRead = Serial.read();
      data_in[i] = valueRead;
      }
    }
  // choose the animation
  if (data_in[0] == 'A')  
      single_color();
  else if (data_in[0] == 'B') {
      hue = (data_in[2]*256+data_in[1]);
      hue = map(hue,0,1024,0,359);
      rainbow_light(hue);
  }
}

// animation signle color
void single_color(){
  for (int i = 0; i < 20; i++) 
    strip.setPixelColor(i,(int)data_in[1],(int)data_in[2],(int)data_in[3]);
  strip.show();
}

// animation rainbow_light
void rainbow_light(int hue){
  // set the Moodlight values 
  ml.setHSB(hue, saturation , brightness);
  for (int i = 0; i < 20; i++) 
    strip.setPixelColor(i,ml.getRed(),ml.getGreen(),ml.getBlue());
  strip.show();
}


