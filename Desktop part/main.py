import requests
import time
from pynput.keyboard import Key, Controller

link = "http://YOUR.WEBSITE/file.txt"
oldValue = "temp"
newValue = "temp"

while True:
    siteContent = requests.get(link)
    newValue = siteContent.text

    if newValue != oldValue and oldValue != "temp":
        siteContentSplit = newValue.split()
        keyboard = Controller()
        key = getattr(Key, siteContentSplit[1])
        keyboard.press(key)
        keyboard.release(key)
        print('Key pressed: ', str(key))

    oldValue = siteContent.text
    time.sleep(1)

