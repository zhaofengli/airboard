# AirBoard
The ![keyboard](https://storage.googleapis.com/material-icons/external-assets/v4/icons/svg/ic_keyboard_black_24px.svg) on air. Mirrors what you type on your keyboard to your phone, using ultrasonic waves.

This is a submission to Beach Hacks 2017 at California State University, Long Beach. See [our original submission](https://devpost.com/software/airboard) on Devpost.

## See it in action
You need [gulp](http://gulpjs.com/) to build our web-based transmitter/receiver, and the [Android SDK](https://developer.android.com/studio/index.html) and [NDK](https://developer.android.com/ndk/index.html) to build the custom IME.

1. Make a recursive clone of this repo
2. Navigate into `web`, and install the dependencies with `npm install`
3. Fire up the server with `gulp serve`
4. Open up the page, and turn up the speakers
5. On another computer, click on the logs to activate the receiver
    - On Chrome, the page has to be served on localhost or via HTTPS to access microphone
5. Type away and watch magic happen

Not enough magic? Build and install the custom IME under `android`, grant it microphone permission and focus on any text field.

## How it's done
We used [libquiet](https://github.com/quiet/quiet) to encode data into soundwaves.

## Who did this
[Tommy Yang](https://github.com/M0gician), [Junlin Wang](https://github.com/IsThatYou) and [Zhaofeng Li](https://github.com/zhaofengli) hacked for a day to make this happen.

## Licensing
Code in this repo is licensed under the BSD 2-Clause License (see `LICENSE`). [libquiet](https://github.com/quiet/quiet/blob/master/LICENSE) and its [JavaScript](https://github.com/quiet/quiet-js/blob/master/LICENSE) and [Java](https://github.com/quiet/org.quietmodem.Quiet/blob/master/licenses/org.quietmodem.Quiet) bindings are licensed under the BSD 3-Clause License. Licenses of other libraries can be found on their respective locations.

Have fun hacking :-)
