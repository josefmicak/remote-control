import tkinter as tk
import webbrowser
from tkinter import ttk
import tkinter.font as font
import tkinter.messagebox as messagebox
import requests
import random
import json
from datetime import datetime
from pynput.keyboard import Key, Controller

window = tk.Tk()
menubar = tk.Menu(window)
window.title("RemoteControlDesktop")
window.resizable(False, False)

scanBoolean = False
fastScan = tk.IntVar()
oldValue = "temp"
newValue = "temp"
iterationsCounter = 0
fastScanAmount = 0
fastScanFrequency = 0
scanFrequency = 0
keyPressed = False
username = "temp"


def default_values():
    global fastScanAmount, fastScanFrequency, scanFrequency, username
    fastScanAmount = 10
    fastScanFrequency = 200
    scanFrequency = 1000
    username = "Select username"


def switch_scan():
    global scanBoolean, scanFrequency, username
    if usernameEntry.get() == "Select username" or len(usernameEntry.get()) == 0:
        tk.messagebox.showerror("Error", "No username selected.")
    elif len(usernameEntry.get()) > 15:
        tk.messagebox.showerror("Error", "Maximum username length is 15 characters.")
    else:
        if scanBoolean:
            scanLabel.config(
                text="Not scanning for keystrokes",
                fg="red")
            scanButton.config(
                text="Start scanning",
                fg="green"
            )
            scanFrequencyEntry.config(
                state="normal"
            )
            scanFrequencySaveButton.config(
                state="normal"
            )
            usernameEntry.config(
                state="normal"
            )
            usernameSaveButton.config(
                state="normal"
            )
        else:
            window.after(scanFrequency, my_mainloop)
            scanLabel.config(
                text="Scanning for keystrokes",
                fg="green")
            scanButton.config(
                text="Stop scanning",
                fg="red"
            )
            scanFrequencyEntry.config(
                state="disabled"
            )
            scanFrequencySaveButton.config(
                state="disabled"
            )
            usernameEntry.config(
                state="disabled"
            )
            usernameSaveButton.config(
                state="disabled"
            )
        scanBoolean = not scanBoolean


def my_mainloop():
    global oldValue, newValue, iterationsCounter, keyPressed, fastScan, scanFrequency, username
    callDelay = scanFrequency

    if scanBoolean:
        response = requests.get("http://testt.8u.cz/getKeyValues.php?username=" + username)
        result = response.text
        result = json.loads(result)
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
            now = datetime.now()
            dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
            pressedKeysTV.insert(parent="", index=0, text="", values=(str(dt_string), str(key)))
            send_response()

        oldValue = result[0].get("messageKey")
        if iterationsCounter >= fastScanAmount:
            keyPressed = False
            iterationsCounter = 0
            fastScanLabel.config(
                fg="black",
                text="Fast scan offline"
            )
            fastScanCB.config(
                state="normal"
            )
            fastScanAmountEntry.config(
                state="normal"
            )
            fastScanFrequencyEntry.config(
                state="normal"
            )
            fastScanSaveButton.config(
                state="normal"
            )
        if keyPressed and fastScan.get() == True:
            iterationsCounter += 1
            callDelay = fastScanFrequency

            fastScanLabel.config(
                fg="green",
                text=("Fast scan online (" + str(iterationsCounter) + " / " + str(fastScanAmount)) + ")"
            )
            if iterationsCounter == 1:
                fastScanCB.config(
                    state="disabled"
                )
                fastScanAmountEntry.config(
                    state="disabled"
                )
                fastScanFrequencyEntry.config(
                    state="disabled"
                )
                fastScanSaveButton.config(
                    state="disabled"
                )
        window.after(callDelay, my_mainloop)


def send_response():
    global username
    data = {'type': '2',
            'username': username,
            'responseKey': random.randint(1, 10000)
            }
    requests.post(url="http://testt.8u.cz/sendKeyStroke.php", data=data)


def fast_scan_switch():
    if not fastScan.get():
        fastScanLabel.config(
            fg="red",
            text="Fast scan disabled"
        )
        fastScanAmountEntry.config(
            state="disabled"
        )
        fastScanFrequencyEntry.config(
            state="disabled"
        )
        fastScanSaveButton.config(
            state="disabled"
        )
    else:
        fastScanLabel.config(
            fg="black",
            text="Fast scan offline"
        )
        fastScanAmountEntry.config(
            state="normal"
        )
        fastScanFrequencyEntry.config(
            state="normal"
        )
        fastScanSaveButton.config(
            state="normal"
        )


def fast_scan_save():
    global fastScanAmount, fastScanFrequency, scanFrequency
    if int(fastScanFrequencyEntry.get()) > scanFrequency:
        tk.messagebox.showerror("Error", ("Error: The fast scan frequency (" + (fastScanFrequencyEntry.get()) +
                                          ") is higher than the scan frequency (" + str(scanFrequency) + ").\n" +
                                          "Please input a lower value."))
    else:
        fastScanAmount = int(fastScanAmountEntry.get())
        fastScanFrequency = int(fastScanFrequencyEntry.get())
        tk.messagebox.showinfo("Success", "Fast scan values updated successfully")


def fast_scan_about():
    fastScanWindow = tk.Toplevel(window)
    fastScanWindow.title("Fast scan")
    tk.Label(fastScanWindow,
             text="Immediately after a key press is detected, the frequency of scans can be temporarily increased.\n"
                  "This is done because it's likely that the user will press multiple keys in a short time.\n"
                  "The fast scan frequency as well as the amount of performed scans can be changed by the user.\n"
                  "Default values: Amount 30, Frequency 200 ms",
             justify=tk.LEFT).grid()


def scan_frequency_save():
    global scanFrequency, fastScanFrequency, fastScan
    if int(scanFrequencyEntry.get()) < fastScanFrequency and (fastScan.get() == True):
        tk.messagebox.showerror("Error", "Error: The scan frequency (" + scanFrequencyEntry.get() +
                                ") is lower than the fast scan frequency (" + str(fastScanFrequency) + ").\n" +
                                "Please either input a higher value or disable fast scan.")
    else:
        scanFrequency = int(scanFrequencyEntry.get())
        tk.messagebox.showinfo("Success", "Scan frequency updated successfully")


def show_help(event=None):
    helpWindow = tk.Toplevel(window)
    helpWindow.title("About")
    tk.Label(helpWindow,
             text="This program is part of a project called Remote Control.\n"
                  "Its purpose is to make it possible to control your computer from remote devices.\n"
                  "Further explanation, as well as the source code can be obtained at \n",
             justify=tk.LEFT).grid()
    linkLabel = tk.Label(helpWindow,
                         text="https://github.com/josefmicak/remote-control",
                         fg="blue",
                         cursor="hand2",
                         justify=tk.LEFT)
    linkLabel.grid()
    linkLabel.bind("<Button-1>", lambda e: callback("https://github.com/josefmicak/remote-control"))


def callback(url):
    webbrowser.open_new(url)


def clear_pressed_keys():
    pressedKeysTV.delete(*pressedKeysTV.get_children())


def save_username():
    global username
    if usernameEntry.get() == "Select username" or len(usernameEntry.get()) == 0:
        tk.messagebox.showerror("Error", "No username selected.")
    elif len(usernameEntry.get()) > 15:
        tk.messagebox.showerror("Error", "Maximum username length is 15 characters.")
    else:
        username = usernameEntry.get()
        tk.messagebox.showinfo("Success", "Username saved successfully.")


default_values()

filemenu = tk.Menu(menubar, tearoff=0)
filemenu.add_command(label="Exit", command=window.destroy, accelerator="Alt+F4")
menubar.add_cascade(label="File", menu=filemenu)

helpmenu = tk.Menu(menubar, tearoff=0)
helpmenu.add_command(label="About", command=show_help, accelerator="F1")
menubar.add_cascade(label="Help", menu=helpmenu)

window.bind("<F1>", show_help)
window.bind("<Alt-F4>", show_help)

scanLF = tk.LabelFrame(text="Scan")
scanLF.grid(row=0)

scanLabel = tk.Label(
    scanLF,
    text="Not scanning for keystrokes",
    fg="red",
    font=("Courier", 30))
scanLabel.grid(row=0, columnspan=10)

scanButton = tk.Button(
    scanLF,
    text="Start scanning",
    fg="green",
    command=switch_scan
)
scanButton['font'] = font.Font(size=20)
scanButton.grid(row=1, column=1, columnspan=3, sticky=tk.E)

scanFrequencyLabel = tk.Label(
    scanLF,
    text="Scan frequency (in ms):")
scanFrequencyLabel.grid(row=2, column=0, sticky=tk.W)

scanFrequencyEntry = tk.Entry(
    scanLF)
scanFrequencyEntry.insert(tk.END, scanFrequency)
scanFrequencyEntry.grid(row=2, column=1, sticky=tk.W)

scanFrequencySaveButton = tk.Button(
    scanLF,
    text="Save",
    command=scan_frequency_save
)
scanFrequencySaveButton.grid(row=2, column=1, columnspan=5)

fastScanLF = tk.LabelFrame(text="Fast scan")
fastScanLF.grid(row=1, column=0, sticky=tk.W)

fastScanAboutButton = tk.Button(
    fastScanLF,
    text="What is fast scan?",
    relief=tk.FLAT,
    fg="blue",
    cursor="hand2",
    command=fast_scan_about
)
fastScanAboutButton.grid(row=0, column=0, sticky=tk.W)

fastScanLabel = tk.Label(
    fastScanLF,
    text="Fast scan offline",
    font=("Courier", 15))
fastScanLabel.grid(row=1)

fastScanCB = tk.Checkbutton(
    fastScanLF,
    text="Enable fast scan",
    variable=fastScan,
    command=fast_scan_switch)
fastScanCB.select()
fastScanCB.grid(row=2, column=0, sticky=tk.E)

fastScanAmountLabel = tk.Label(
    fastScanLF,
    text="Amount of fast scans to perform:")
fastScanAmountLabel.grid(row=3, column=0, sticky=tk.W)

fastScanAmountEntry = tk.Entry(
    fastScanLF)
fastScanAmountEntry.insert(tk.END, fastScanAmount)
fastScanAmountEntry.grid(row=3, column=1)

fastScanFrequencyLabel = tk.Label(
    fastScanLF,
    text="Delay between scans (in ms):")
fastScanFrequencyLabel.grid(row=4, column=0, sticky=tk.W)

fastScanFrequencyEntry = tk.Entry(
    fastScanLF)
fastScanFrequencyEntry.insert(tk.END, fastScanFrequency)
fastScanFrequencyEntry.grid(row=4, column=1)

fastScanSaveButton = tk.Button(
    fastScanLF,
    text="Save",
    command=fast_scan_save
)
fastScanSaveButton.grid(row=5, column=0, sticky=tk.E)

pressedKeysLF = tk.LabelFrame(text="Pressed keys")
pressedKeysLF.grid(row=1, column=0, sticky=tk.E)

pressedKeysLabel = tk.Label(
    pressedKeysLF,
    text="Last 5 pressed keys:",
    font=("Courier", 13))
pressedKeysLabel.grid()

pressedKeysTV = ttk.Treeview(
    pressedKeysLF,
    height=5
)
pressedKeysTV['columns'] = ('Time', 'Key')
pressedKeysTV.column('#0', width=0, stretch=tk.NO)
pressedKeysTV.column('Time', anchor=tk.CENTER, width=120)
pressedKeysTV.column('Key', anchor=tk.CENTER, width=80)
pressedKeysTV.heading('#0', text='', anchor=tk.CENTER)
pressedKeysTV.heading('Time', text='Time', anchor=tk.CENTER)
pressedKeysTV.heading('Key', text='Key', anchor=tk.CENTER)
pressedKeysTV.grid(row=0)

clearPressedKeysButton = tk.Button(
    pressedKeysLF,
    text="Clear",
    command=clear_pressed_keys
)
clearPressedKeysButton.grid(row=1)

usernameLF = tk.LabelFrame(text="Username")
usernameLF.grid(row=2, column=0, columnspan=3, sticky=tk.W)

usernameLabel = tk.Label(
    usernameLF,
    text="Username:")
usernameLabel.grid(row=0, column=0)

usernameEntry = tk.Entry(
    usernameLF)
usernameEntry.insert(tk.END, username)
usernameEntry.grid(row=0, column=2)

usernameSaveButton = tk.Button(
    usernameLF,
    text="Save",
    command=save_username
)
usernameSaveButton.grid(row=0, column=4)

window.config(menu=menubar)
window.mainloop()
