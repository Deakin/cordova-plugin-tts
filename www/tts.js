/*
Cordova Text-to-Speech Plugin
https://github.com/Deakin/cordova-plugin-tts

by VILIC VANE
https://github.com/vilic

Forked from:
https://github.com/kanayo/cordova-plugin-tts
https://github.com/chemerisuk/cordova-plugin-tts

MIT License
*/

var exec = require('cordova/exec');

module.exports = {
    speak: function(param, resolved, rejected) {
      var options = (typeof param === "string" ? {text: param} : param);
      exec(resolved, rejected, "TTS", "speak", [options]);
    },

    stop: function() {
      exec(null, null, "TTS", "stop", []);
    },
}