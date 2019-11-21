# Swingy

Small role-playing game with GUI made on Swing framework

![](doc/)

## Description

Create your hero and find the way out of a square map populated with various weird creatures. Fight or run, gain experience, collect loot, and level up to boost your hero's power.

### Features:

- project is built with Maven
- switch between Swing GUI and text mode at runtime
- heroes are stored in the embedded relational database (H2)
- annotation-based Hibernate Validation
- 7 classes of heroes, 10 kinds of enemies, map obstacles, dozens of artifacts to collect

The project implements Model-View-Controller, Builder, Singleton design patterns. 

*This is the second project of the Java branch at School 42.*

**Detailed description of the task: [Swingy.en.pdf](https://github.com/dstepanets/Swingy/blob/master/docs/Swingy.en.pdf)**

## Usage

Compile in terminal with:

`mvn clean package`

Run with one of the following commands:

`java -jar target/swingy-1.0-jar-with-dependencies.jar gui`

`java -jar target/swingy-1.0-jar-with-dependencies.jar console`

