# Remote control
Allows the user to use the computer's keyboard from other devices. It's mainly meant to be used for watching media, with 3 keys being able to be pressed this way so far:
- Spacebar - pause/play
- Left Arrow key - rewind
- Right Arrow key - forward

There are also 2 indicator lights that determine whether the key press was successfully sent and delivered or not
- First light (the left one) - determines whether the device successfully sent the key press
- Second light (the right one) - determines whether the desktop app acknowledged the key press

<p align="center"> 
<img src="Images/Website.png">
</p>

## Desktop part

*Languages used: Python*

Detects remote key presses and immediately performs them. For now there's no GUI, the key presses are printed to console.


## Website part

*Languages used: HTML, CSS, JavaScript, PHP*

Provides a way to perform remote key presses using a website. The .php files are required for the other parts of this project to work, the other files are optional.


## Watch part

*Languages used: HTML, CSS, JavaScript*

Provides a way to perform remote key presses using a smart watch. The application has been successfully tested on a real device (Samsung Galaxy Watch Active2) and it can probably only be used on Tizen OS-based watches. Includes a widget.
