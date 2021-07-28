from random import random

import requests
import time
import random
from pynput.keyboard import Key, Controller

link = "http://YOUR.WEBSITE/file.txt"
oldValue = "temp"
newValue = "temp"
iterationsCounter = 0
keyPressed = False


def send_response():
    data = {'responseKey': random.randint(1, 10000),
            'type': '2'}
    requests.post(url="http://YOUR.WEBSITE/sendKeyStroke.php", data=data)


while True:
    siteText = requests.get(link)
    siteContent = siteText.text
    siteContentSplit = siteContent.split()
    newValue = siteContentSplit[0]

    if newValue != oldValue and oldValue != "temp":
        keyboard = Controller()
        key = getattr(Key, siteContentSplit[1])
        keyboard.press(key)
        keyboard.release(key)
        keyPressed = True
        iterationsCounter = 0
        print('Key pressed: ', str(key))
        send_response()

    oldValue = siteContentSplit[0]
    if iterationsCounter >= 30:
        keyPressed = False
        iterationsCounter = 0
    if keyPressed:
        iterationsCounter += 1
        time.sleep(0.2)
    else:
        time.sleep(1)
