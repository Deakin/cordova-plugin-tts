/*

    Cordova Text-to-Speech Plugin
    https://github.com/kanayo/cordova-plugin-tts
    
    Based on:
    https://github.com/vilic/cordova-plugin-tts

    by VILIC VANE
    https://github.com/vilic

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

