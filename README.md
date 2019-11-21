# Swingy

A small role-playing game with GUI made on the Swing framework.

![](doc/demo1.gif)

## Description

Create your hero and find the way out of a square map populated with various weird creatures. Fight or run, gain experience, collect loot, and level up to boost your hero's power.

### Features:

- build with Maven
- switch between Swing GUI and text mode at runtime
- heroes are stored in the embedded relational database (H2)
- annotation-based Hibernate Validation
- 7 classes of heroes, 10 kinds of enemies, map obstacles, dozens of artifacts to collect

The project implements Model-View-Controller, Builder, Singleton design patterns. 

*This is the second project of the Java branch at School 42.*

**Detailed description of the task: [Swingy.en.pdf](https://github.com/dstepanets/Swingy/blob/master/docs/Swingy.en.pdf)**

## Usage

*There is a pre-compiled jar archive ready to run.*

Start the game with one of the following commands in a terminal:

`java -jar swingy-1.0-jar-with-dependencies.jar gui`

`java -jar swingy-1.0-jar-with-dependencies.jar console`

Or recompile first (binaries will be in `target` directory):

`mvn clean package`

**You have to use these commands from the root directory!**

**Requires JRE 7 or higher.**
