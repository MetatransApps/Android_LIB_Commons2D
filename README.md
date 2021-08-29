# License

The license is GPL-2.0-only

# Overview

This is a simple 2D Graphics Framework.
After git clone, this library for Android, could be imported directly to Android Studio. Then it can be extended and used in Java programming language from your Android projects.

It represents a library, which is simple, light and small, without any other dependencies.

Not powerful as Unity 3D or similar frameworks, but still enough to build old school 2D games.

# How it works? Implementation details ...

It works on low level by drawing pixels, lines, circles, bitmaps/images on [android.graphics.Canvas](https://developer.android.com/reference/android/graphics/Canvas)
Also painting and filling out shapes with given color.
It is working fast as it relies on the build-in double buffering approach in **android.graphics.***

Of course, it supports Camera view and related features. The camera representation supports static camera (like in Gravity and Balloons games) as well as moving camera (like in the Maze game)

# Exemplary games, uploaded to various App Stores, which use this Framework and has its dependency:
  - https://metatransapps.com/gravity-force-finger-137-cross-the-orbits/
  - https://metatransapps.com/non-stop-balloons-shooter-for-kids-and-adults/
  - https://metatransapps.com/maze-runner-2d-old-school-labyrinth-offline-game/
  
# How to extend it and use it for game development?
Here are 2 examples:
  - https://github.com/MetatransApps/Android_LIB_2DGravity, search for **com.gravityplay.model.WorldGenerator_Gravity.java**
  - https://github.com/MetatransApps/Android_LIB_2DBalloons, search for **com.stoptheballs.model.WorldGenerator_StopTheBalls.java**
