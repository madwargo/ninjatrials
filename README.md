# NinjaTrails

Ninja Trials is an old school style Android Game, developed specifically for [OUYA](https://www.ouya.tv/ "Ouya console"), using [AndEngine](https://github.com/nicolasgramlich/AndEngine "AndEngine by Nicolas Gramlich").

The game features several minigames, they all have simple gameplay, like quick repeating button presses or accurate timing button presses.

We started making this game to test the capabilities of AndEngine and to pay homage to great old games like [Track & Field](http://en.wikipedia.org/wiki/Track_&_Field_(video_game)) and [Combat School / Boot Camp](http://en.wikipedia.org/wiki/Combat_School) (no copyright infringement intended, both titles belong to their respective legal owners).


## Requirements:

* AndEngine (branch GLES2-AnchorCenter), http://www.andengine.org/
* android-sdk 17, http://developer.android.com/sdk/index.html


## Download:

The source code is available from github, you can clone the git tree by doing:

   * git clone https://github.com/madwargo/ninjatrials.git


## Compilation:

Ninja Trials uses [javac](http://en.wikipedia.org/wiki/Javac) (included in [JDK](http://en.wikipedia.org/wiki/Java_Development_Kit)) as build system. The complete compilation process depends on the system you are using (Linux, Mac OS X or Windows).

### Compilation without eclipse about Linux:

When you install android-sdk, you will need add to PATH the next:

    export PATH=$ANDROID\_HOME/tools:$ANDROID\_HOME/platform-tools:$PATH (replace $ANDROID\_HOME by the path of android-sdk)

Install AndEngine:

    git clone https://github.com/nicolasgramlich/AndEngine.git
    cd AndEngine
    git checkout -t origin/GLES2-AnchorCenter
    android list targets (replace target-id in the next command by ID)
    android update project --target target-id --name project-name --path /path/to/project
    ant release

Config adb (command line tool that lets you communicate with an emulator instance or connected Android-powered device):

    sudo adb kill-server
    sudo adb start-server
    sudo adb devices (aqui deberiamos ver nuestros dispositivo si los tenemos enchufados)

Develop project and load in device:

    git clone https://github.com/madwargo/ninjatrials.git
    android update project --target target-id --name ninjatrials --path /path/to/project --library /relative/path/AndEngine
    cd ninjatrials
    ant debug or ant realease
    ant debug install or ant release install (this command install the project in the device)

### Add language

* Make a copy of the file "" placed in "", adding to the end the two letter suffix pertaining to your language.
* Translate the sentences to your language.


## Other info:

For remove warnings caused by accent mark, add the next line in android-sdk/ant/build.xml:

    <property name="java.encoding" value="ISO-8859-1" />


## Bibliography

    * http://incise.org/android-development-on-the-command-line.html
    * http://developer.android.com/tools/projects/projects-cmdline.html


## License

Copyright 2013 Mad Gear.

   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
