# AndroTron
[![](https://jitpack.io/v/EricDw/AndroTron.svg)](https://jitpack.io/#EricDw/AndroTron)

AndroTron is my spin on the Model-View-Intent architecture as described [here](http://hannesdorfmann.com/android/model-view-intent) by Hannes Dorfmann.

The goals of MVI and how it works are out of the scope of this README, I highly encourage you to read the above post and watch the YouTube video for that.

The goals of this library is to provide myself and other developers with a starting point for using the MVI architecture. 
The secondary goal of the library is to use Kotlin's new [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) as a replacment for RxJava.

If you know what MVI is then you might be looking at this code and wondering "Where are the Intents?". 
Well I didnt like the name overlap with android `Intent` so I instead named them `Command` this makes communication easier.
Another difference you may notice is the Transformers nomenclature used in places of the code, a mans gotta have some fun is all I have to say on the matter.
