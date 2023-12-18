# JEngine 2
An OpenGL upgrade of JEngine, a JavaFX based game engine.

## Documentation
You can find all the JEngine 2 documentation [here](https://noahfreelove.github.io/JE2/)

## Setup
1. Download the latest release from the [releases page](https://github.com/NoahFreelove/JE2/releases/)
2. Add JE2 jar to your project. In IntelliJ this is done by going to `File > Project Structure > Libraries > + > Java > (select the JE2 jar) > OK > Apply`
3. Read the documentation for how to use the engine but to quickly open a window use `org.JE.JE2.Manager.run()`;

## Roadmap
* **JE 1.0** - Fully document the engine. Create test cases for the engine.
* **JE 0.95** - Fix up confusing I/O system. Fix broken systems ✅
* **JE 0.9** - Implement post processing ✅

## Features
- Easy setup✅
- 2D vertex rendering ✅
- Shaders ✅
- Textures ✅
- Simple lighting ✅
- Audio ✅
- GUI ✅
- Basic Physics ✅
- Post Processing ✅
- Object Scripting ✅

## Goals
- User won't have to directly interact with any OpenGL or OpenAL
- The Engine is easy to set up.
- The Engine can be modified to fit the user's needs.
- The Engine is well documented.

## Advantages over JEngine
- Much lower memory usage (not perfect but much better)
- More control over objects and scripts
- Uses the JBox2D physics library (not the homemade one from JEngine)
- Built in audio playback + Filters
- More customizable Lighting
