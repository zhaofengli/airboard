const profile = 'ultrasonic-experimental';
const keyMapping = {
  8: -5,
  13: -4
}

let tx = false;
let rx = false;

Quiet.init({
  profilesPrefix: './',
  memoryInitializerPrefix: "./",
  libfecPrefix: './'
});

function log(message) {
  $('#log').append(message + '\n');
  $('#log').scrollTop($('#log').prop('scrollHeight'));
  console.log(message);
}

function onQuietReady() {
  log('quiet.js is ready\n');
  $('#inputfield').hide();
  tx = Quiet.transmitter({profile: profile, onFinish: onTransmitFinish, clampFrame: false});
  log('[TX] using profile ' + profile);

  // RX logic
  $('#log').click((e) => {
    if (rx) return;
    rx = Quiet.receiver({profile: profile, onReceive: onReceiveFinish, onCreateFail: onCreateReceiverFail, onReceiveFail: onReceiveFail});
    log('[RX] using profile ' + profile);
    $('#inputfield').show();
  });
  log('[RX] tap here to start receiver');

  // TX logic
  $(document).keypress((e) => {
    let keycode = e.which;
    if (keycode >= 65 && keycode <= 90) {
      if (!e.shiftKey) {
        keycode += 32;
      }
    }
    if (keycode == 16) return; // Ignore shift key
    if (keycode == 186) keycode = 58; // :
    log('[TX] sending keypress keycode ' + keycode);
    tx.transmit(Quiet.str2ab(keycode));
  });
  $(document).keydown((e) => {
    let keycode = e.which;
    if (keycode == 27 || keycode == 8 || (keycode >= 37 && keycode <= 40)) {
      log('[TX] sending keydown keycode ' + keycode);
      tx.transmit(Quiet.str2ab(keycode));
    }
  });
  log('[TX] type to send');
}

function onQuietFail() {
  log('quiet.js failed to load');
}

function onTransmitFinish() {
  log('[TX] sent; buffer cleared');
}

function onReceiveFinish(payload) {
  var str = Quiet.ab2str(payload);
  log('[RX] received keycode ' + str);
  $('#inputfield').val($('#inputfield').val() + String.fromCharCode(parseInt(str)));
}

function onReceiveFail() {
  log('[RX] failed to receive');
}

function onCreateReceiverFail() {
  log('[RX] failed to create receiver');
}

$(document).ready(() => {
  Quiet.addReadyCallback(onQuietReady, onQuietFail);
});
