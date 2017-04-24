package me.tommyyang.airboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.View;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.nio.charset.Charset;

/**
 * Created by tommy on 4/22/17.
 */

public class AirBoard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Subscription frameSubscription = Subscriptions.empty();

    private boolean caps = false;


    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        return kv;
    }

    public void onStartInputView(EditorInfo info, boolean restarting) {
        if (!frameSubscription.isUnsubscribed()) {
            frameSubscription.unsubscribe();
        }

        int granted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (granted == PackageManager.PERMISSION_GRANTED) {
            subscribeToFrames();
        }
    }

    private void subscribeToFrames() {
        InputConnection ic = getCurrentInputConnection();
        frameSubscription = FrameReceiverObservable.create(this, "ultrasonic-experimental").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {
            int keycode = Integer.parseInt(new String(buf, Charset.forName("UTF-8")));
            switch (keycode) {
                case 8:
                    ic.deleteSurroundingText(1, 0);
                    break;
                case 37:
                    keyDownUp(KeyEvent.KEYCODE_DPAD_LEFT);
                    break;
                case 38:
                    keyDownUp(KeyEvent.KEYCODE_DPAD_UP);
                    break;
                case 39:
                    keyDownUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                    break;
                case 40:
                    keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN);
                    break;
                case 127:
                    ic.deleteSurroundingText(0, 1);
                    break;
                case 13:
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                    break;
                default:
                    ic.commitText(Character.toString((char)keycode), 1);
		    break;
            }
        }, error-> {
        });
    }

    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    private void playclick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection() ;
        playclick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if(Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }

                ic.commitText(String.valueOf(code), 1);
		break;
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        frameSubscription.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        frameSubscription.unsubscribe();
    }
}
