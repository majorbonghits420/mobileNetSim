# mobileNetSim
Simulation of mobile nodes transfering DAG blockchain data in an AD-HOC network for my MEng project

## Overview
Our project uses Java and the Gradle build tool.

To compile the code run the command

$ ./gradlew assemble

We have several unit tests and also uses FindBugs analysis. To run tests and run the FindBugs analysis you can run the command

$ ./gradlew check

## Running Simulations
There are two different simulations that can be run.
The first one measures the duration it takes for a block to spread to all nodes in scenarios that differ based on the range that nodes have. This test can be run with the following command

$ ./gradlew run -Dexec.args="range"

The second simulation measures the duration it takes for a block to spread to all nodes in scenarios that varies the number of passive nodes in the network.
This simulation can be run with the command

$ ./gradlew run -Dexec.args="passive"

The results of the simulations will then be outputted to standard out.
