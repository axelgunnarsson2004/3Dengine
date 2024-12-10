# Simple 3D Engine

This project implements a simple 3D engine that can draw 3D-like objects by leveraging the ability to draw a line between two 2D points on the screen. The engine is based on the `introprog` package and demonstrates basic concepts of 3D rendering using simple 2D projections.

## Files

- `graphics.scala`: Contains the functionality for drawing shapes and handling the 3D rendering.
- `introprog_3-1.3.0.jar`: The external library for drawing lines between 2D points.
- `main.scala`: The entry point for the program, which runs the engine and handles initialization.
- `world.scala`: Contains the definitions for the virtual world and defined 3D objects

## Setup

1. Ensure you have **Scala**

2. Place the `introprog_3-1.3.0.jar` file in your project directory or adjust the classpath to point to it.

## Instructions

### 1. How to run 

Navigate to the project directory and run:

```bash
scala run . --jar introprog_3-1.3.0.jar --main-class=run
```

