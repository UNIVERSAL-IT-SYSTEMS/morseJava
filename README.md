# morseJava
Morse coder / decoder with audio visualisation
This project was originally written for the establishment of radio communication between two laptops.
Program is using TarsosDSP framework for audio detection and visualisation.

Features:
- morse coder (outputs are audio & text)
- morse decoder
- save logs
- audio visualisation

Link to TarsosDSP:
https://github.com/JorenSix/TarsosDSP

How to use decoder:
After you pressed Decoder button, a new window will popup. Inside that window you need to select the audio input and change value of slider value. Value of slider should be slightly below the curve in order to properly decode the signal (signal bellow line is ignored). Decoder will than be activated. 
