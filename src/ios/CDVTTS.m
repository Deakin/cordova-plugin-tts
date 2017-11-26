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

#import <Cordova/CDV.h>
#import "CDVTTS.h"

@implementation CDVTTS

- (void)pluginInitialize {
    synthesizer = [AVSpeechSynthesizer new];
    synthesizer.delegate = self;
}

- (void)speechSynthesizer:(AVSpeechSynthesizer*)synthesizer didFinishSpeechUtterance:(AVSpeechUtterance*)utterance {
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    if (lastCallbackId) {
        [self.commandDelegate sendPluginResult:result callbackId:lastCallbackId];
        lastCallbackId = nil;
    } else {
        [self.commandDelegate sendPluginResult:result callbackId:callbackId];
        callbackId = nil;
    }

    //[[AVAudioSession sharedInstance] setActive:NO withOptions:0 error:nil];
    //[[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategoryAmbient
    //  withOptions: 0 error: nil];
    //[[AVAudioSession sharedInstance] setActive:YES withOptions: 0 error:nil];
}

- (void)speak:(CDVInvokedUrlCommand*)command {
    //[[AVAudioSession sharedInstance] setActive:NO withOptions:0 error:nil];
    [[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategoryPlayback
      withOptions:AVAudioSessionCategoryOptionDuckOthers error:nil];

    if (callbackId) {
        lastCallbackId = callbackId;
    }

    callbackId = command.callbackId;

    //[synthesizer stopSpeakingAtBoundary:AVSpeechBoundaryImmediate];

    NSDictionary* options = [command.arguments objectAtIndex:0];

    NSString* text = [options objectForKey:@"text"];
    NSString* locale = [options objectForKey:@"locale"];
    double rate = [[options objectForKey:@"rate"] doubleValue];

    if (!locale || (id)locale == [NSNull null]) {
        locale = @"en-US";
    }

    if (!rate) {
        rate = 1.0;
    }

    AVSpeechUtterance* utterance = [[AVSpeechUtterance new] initWithString:text];
    utterance.voice = [AVSpeechSynthesisVoice voiceWithLanguage:locale];
    // Rate expression adjusted manually for a closer match to other platform.
    utterance.rate = (AVSpeechUtteranceMinimumSpeechRate * 1.5 + AVSpeechUtteranceDefaultSpeechRate) / 2.5 * rate * rate;
    // workaround for https://github.com/vilic/cordova-plugin-tts/issues/21
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 9.0) {
       utterance.rate = utterance.rate * 2;
       // see http://stackoverflow.com/questions/26097725/avspeechuterrance-speed-in-ios-8
    }
    utterance.pitchMultiplier = 1.2;
    [synthesizer speakUtterance:utterance];
}

- (void)stop:(CDVInvokedUrlCommand*)command {
    [synthesizer stopSpeakingAtBoundary:AVSpeechBoundaryImmediate];
}
@end
