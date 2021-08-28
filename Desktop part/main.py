import requests
import time
import random
import json
from pynput.keyboard import Key, Controller

oldValue = "temp"
newValue = "temp"
iterationsCounter = 0
keyPressed = False


def send_response():
    data = {'responseKey': random.randint(1, 10000),
            'type': '2'}
    requests.post(url="http://testt.8u.cz/sendKeyStroke.php", data=data)


while True:
    response = requests.get("http://testt.8u.cz/getKeyValues.php")
    result = response.text
    result = json.loads(result)
    print(result)
    newValue = result[0].get("messageKey")

    if newValue != oldValue and oldValue != "temp":
        keyboard = Controller()
        key = result[0].get("keyStroke")
        if len(key) > 1:
            key = getattr(Key, result[0].get("keyStroke"))
        keyboard.press(key)
        keyboard.release(key)
        keyPressed = True
        iterationsCounter = 0
        print('Key pressed: ', str(key))
        send_response()

    oldValue = result[0].get("messageKey")
    if iterationsCounter >= 30:
        keyPressed = False
        iterationsCounter = 0
    if keyPressed:
        iterationsCounter += 1
        time.sleep(0.2)
    else:
        time.sleep(1)
