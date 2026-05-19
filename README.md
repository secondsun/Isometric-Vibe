# Isometric Map Viewer

A Kotlin-based isometric map renderer utilizing Java Swing for display. This project features a custom software renderer capable of handling cubes, ramps, and pyramids with texture mapping and animated sprites.

## Features

- **Isometric Rendering**: Custom software-based renderer for isometric geometry.
- **Geometry Types**: Supports Cubes, Ramps (multiple angles), and Pyramids.
- **Texturing**: UV-mapped textures for tiles and sprites.
- **Animated Sprites**: Character sprites with walking animations.
- **Interactive Camera**: Support for panning, zooming, and rotation (yaw).
- **Fixed-Point Math**: Optimized calculations using fixed-point arithmetic.

## Requirements

- **Java JDK 8** or higher.
- **Maven** 3.6+.

## Setup & Run

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn exec:java
```

The application entry point is `dev.secondsun.vibe.isomap.MainKt`.

## Controls

The viewer is interactive. Use the following keys to navigate:

- **W / S**: Pan Forward / Backward
- **A / D**: Pan Left / Right
- **Q / E**: Rotate Camera (Yaw)
- **+ / -**: Zoom In / Out

## Project Structure

- `src/main/kotlin`: Core logic and rendering engine.
  - `dev.secondsun.vibe.isomap.Main`: Application entry point and UI setup.
  - `dev.secondsun.vibe.isomap.Renderer`: High-level rendering orchestration.
  - `dev.secondsun.vibe.isomap.SoftwareRenderer`: Low-level pixel and polygon rasterization.
  - `dev.secondsun.vibe.isomap.Model`: Data structures for Tiles, Sprites, and the Map.
  - `dev.secondsun.vibe.isomap.FixedMath`: Utility for fixed-point math operations.
- `src/main/resources`: Textures, sprites, and other assets.
- `pom.xml`: Maven configuration and dependencies.

## Project Purpose
This project's purpose is to provide me a way to test different optimizations for rendering. 
I'm currently working on a SuperFX homebrew engine for a tactical rpg game. It is heavily inspired by the visual style
of Final Fantasy Tactics and Vandal Hearts on the PlayStation.  

The game will target 20fps at 4bpp color ina  single palette for the rendered layer with the SNES PPU handling a sprite
layer. This will let sprites have their own palettes and take some pressure off the GSU memory space.

## License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for details.
