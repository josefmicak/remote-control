from random import random
import requests
import time
import random
from pynput.keyboard import Key, Controller

oldValue = "temp"
newValue = "temp"
iterationsCounter = 0
keyPressed = False


def send_response():
    data = {'responseKey': random.randint(1, 10000),
            'type': '2'}
    requests.post(url="http://YOUR.WEBSITE/sendKeyStroke.php", data=data)


while True:
    response = requests.get("http://YOUR.WEBSITE/getKeyValues.php")
    newValue = response.json().get("messageKey")

    if newValue != oldValue and oldValue != "temp":
        keyboard = Controller()
        key = getattr(Key, response.json().get("keyStroke"))
        keyboard.press(key)
        keyboard.release(key)
        keyPressed = True
        iterationsCounter = 0
        print('Key pressed: ', str(key))
        send_response()

    oldValue = response.json().get("messageKey")
    if iterationsCounter >= 30:
        keyPressed = False
        iterationsCounter = 0
    if keyPressed:
        iterationsCounter += 1
        time.sleep(0.2)
    else:
        time.sleep(1)
