# ZeroInputLag

Client-only Fabric mod for Minecraft 1.21.4 that reduces input latency. No config, no gameplay assists, nothing sent to servers.

## What it does
- **Late input polling:** drains pending OS input events (`glfwPollEvents`) immediately before the camera reads the mouse delta and before the tick consumes key/button presses, so fresher data reaches the vanilla pipeline.
- **Raw, unsmoothed mouse:** forces the vanilla cinematic/smoothing filter off and enables GLFW raw mouse motion (where the OS supports it). Sensitivity curve is untouched.

## Not included
No Auto Sprint, W-Tap, auto-strafe, movement helpers or combat automation. No native OS-level input bypass (see Kemmlow's mod for that).

## Build
Locally: JDK 21 + Gradle 8.12 → `gradle build` → `build/libs/zeroinputlag-1.0.0.jar`.
GitHub: push the repo, open Actions → `build` → download the `ZeroInputLag-jar` artifact.

## Credits / inspiration
- No Input Lag Tick Rate by incend1um3 (MIT) — idea: input handling tied to the render loop. https://github.com/incend1um3/No-Input-Lag-Tick-Rate
- Kemmlow's Input Optimizer by Kemmlow (LGPL-3.0) — idea: read input early, feed fresher data to vanilla. https://github.com/Kemmlow/inputoptimizer
- InputBooster (Modrinth) — idea: higher-frequency input polling. https://modrinth.com/mod/inputbooster

This mod contains independently written code; no source from those projects was copied. Licensed LGPL-3.0-only.
