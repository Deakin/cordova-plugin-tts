# Cordova Text-to-Speech Plugin

## Platforms

iOS 7+  
Windows Phone 8  
Android 4.0.3+ (API Level 15+)

## Installation

```sh
cordova plugin add https://github.com/Mhitra/cordova-plugin-tts.git
```

## Usage

```javascript
// make sure your the code gets executed only after `deviceready`.
document.addEventListener('deviceready', function () {
    var options = { text: 'Hello World!', locale: 'en-AU', rate: 0.75 };
    $window.TTS.speak(options, function (success) {
        logService.log('TTS::speak::success:', success);
    }, function (error) {
        logService.error('TTS::speak::error:', error);
    });
}, false);
```


## API Definitions

The `onfulfilled` callback will be called when the speech finishes,
and the `onrejected` callback will be called when an error occurs.

speak() adds an utterence to a queue and returns immediately. 

To interurupt and flush the queue, call stop()

```typescript
declare module TTS {
    interface IOptions {
        /** text to speak */
        text: string;
        /** a string like 'en-US', 'zh-CN', etc (default: en-US)*/
        locale?: string;
        /** speed rate, 0 ~ 1 (default: 1)*/
        rate?: number;
        /** ms delay before utterance (default: 0) **/
        preDelay?: number;
        /** ms delay after utterance (default: 0)**/
        postDelay?: number;
    }

    function speak(options: IOptions, onfulfilled: () => void, onrejected: (reason) => void): void;
    function speak(text: string, onfulfilled: () => void, onrejected: (reason) => void): void;
    function stop(): void;
}
```
