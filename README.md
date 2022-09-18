# Annotator
### Apk is included in project
## Structure
The core structure of this project will revolve around finding a way to send audio data to the application from the lock screen of the phone. Once this is has been implemented efficiently, this can be passed through a natural language processor then sent for data storage. The TextToSpeech class could be a possible option for turning the raw audio recordings into text. Then, any form of natural language processsing would suffice to identify keywords. For example, A POS Tagger utilizing nltk and then Spaccy and nltk would be able to retrieve the words/actions we want to store. If the nlp portion of the processing is not viable on an android device for battery or performance reasons, perhaps it can be simply uploaded to a server for pre-processing. This action could be done once a day, or once a week, or over a different period. Regarding methods of storing the raw audio files, it may be more efficient to use a single file and keep adding to it then split it prior to processing.
<br>
## Challenges
The first challenge, above all, would be getting permission from android to use the microphone on demand. The easiest way to circumvent this is to use Ok Google to pipe input directly into the app, allowing for better battery performance, built in parsing, and no worry of permission problems. If the route of implementing the recording through the app is chosen, Read, Write, and Record permissions are needed; however, after long durations of microphone usage, results may begin to vary. A solution to this would be to have a notification on a timer and upon notifying the user, create and start a MediaRecorder and then stop it after the desired amount of time. Something critical to note is the following:
<br>
***Note: On devices running Android 9 (API level 28) or higher, apps running in the background cannot access the microphone. Therefore, your app should record audio only when it's in the foreground or when you include an instance of MediaRecorder in a foreground service.***
<br>
Due to this the user must manually start the recording service and it potentially needs to work off of pauses and resumes.I am currently unsure how this will effect the battery performance or if the microphone will function as intended after X pauses and resumes.
<br>
## Implementation/Testing
For this basic implementation I have created an app that will once prompted, wait 10 seconds then play a notification sound (through android notifications) and then take recording input for 10 more seconds. This can all be done from the app or from the lock screen. This proves the concept of an app being able to notify the user from the background and recieve input and save it all from the lockscreen. For testing, the app's implementation also includes a playback button to ensure the recording is as intended.
## Limitations
The current limitations of this design include potential battery life excessive drain, no way to distinguish between other application notifications and this app's, demanding the user have their phone notifications on, and no built in processing of audio input. Utilizing the google assistant to pipe data into the application would be a good first step in addressing these problems.
## Future
<ul>
  <li>Be able to launch app from notification</li>
  <li>Allow repeated execution of recording notification</li>
  <li>Convert Audio to Text</li>
  <li>Parse text into keywords</li>
  <li>Store keywords</li>
</ul> 
