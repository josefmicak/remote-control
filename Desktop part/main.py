import requests
import time
from pynput.keyboard import Key, Controller

link = "http://YOUR.WEBSITE/file.txt"
oldValue = "temp"
newValue = "temp"
iterationsCounter = 0
keyPressed = False

while True:
    siteContent = requests.get(link)
    newValue = siteContent.text

    if newValue != oldValue and oldValue != "temp":
        siteContentSplit = newValue.split()
        keyboard = Controller()
        key = getattr(Key, siteContentSplit[1])
        keyboard.press(key)
        keyboard.release(key)
        keyPressed = True
        iterationsCounter = 0
        print('Key pressed: ', str(key))

    oldValue = siteContent.text
    if iterationsCounter >= 30:
        keyPressed = False
        iterationsCounter = 0
    if keyPressed:
        iterationsCounter += 1
        time.sleep(0.2)
    else:
        time.sleep(1)