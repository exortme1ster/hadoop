# Homework 2 - Nikita Mashchenko
The goal of this homework is for students to gain experience with solving a distributed computational problem using cloud computing technologies. I designed and implemented an instance of the map/reduce computational model

AWS deployment video - [video link]
## Prerequisites
🚀 Since we are using Hadoop, require Hadoop 3.2.4 installed on Windows 10. <br>
🚀 IntelliJ <br>
🚀 the JDK (between versions 8 and 18) <br>
🚀 the Scala runtime <br>
🚀 the IntelliJ Scala plugin <br>
🚀 the Simple Build Toolkit (SBT) <br>

## Instructions to run locally in hadoop

✨ run the command - ```sbt clean compile assembly``` this will generate the jar file. Then copy this jar file into a folder which has access. <br>
✨ Run the hadoop clusters by running ```Start-all.cmd``` with cmd in admin mode in sbin folder. <br>
✨ Create an input folder by executing command - ```hadoop fs -mkdir /input_dir``` <br>
✨ Copy the input log files (input_file.txt) into this folder - ```hadoop fs -put C:/input_file.txt /input_dir``` <br>
✨ Run the command - ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.<Task Number> /input_dir /output_dir``` this will start the hadoop map/reduce job. <br>
✨ Finally to view the output run the command - ```hadoop dfs -cat /output_dir/* ``` <br>

## Tasks
### Task1
Task1 is to generate the distributions of various type of log messages in a specific time interval.
To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Task1 /input_dir /output_dir``` <br>
Output -
```
12:53, INFO, 1
12:54, INFO, 1
12:55, DEBUG, 1
12:57, DEBUG, 1
12:57, INFO, 1
12:58, INFO, 1
12:59, INFO, 2
13:00, INFO, 2
13:02, DEBUG, 1
13:03, INFO, 1
13:04, DEBUG, 1
13:04, INFO, 2
13:05, WARN, 2
13:06, INFO, 1
13:06, WARN, 1
13:07, INFO, 3
13:08, INFO, 1
13:09, INFO, 1
13:09, WARN, 1
13:10, INFO, 1
13:11, INFO, 2
13:13, DEBUG, 1
13:13, INFO, 1
13:14, INFO, 1
13:15, INFO, 1
13:16, DEBUG, 1
13:18, WARN, 2
13:19, DEBUG, 1
13:19, INFO, 1
13:19, WARN, 2
13:20, WARN, 1
13:21, WARN, 1
14:18, INFO, 2
14:19, WARN, 1
14:21, ERROR, 1
14:22, INFO, 1
14:23, INFO, 1
14:24, INFO, 3
14:24, WARN, 1
14:25, INFO, 1
14:28, INFO, 2
14:29, INFO, 3
14:30, INFO, 1
14:31, INFO, 5
14:32, INFO, 1
14:33, INFO, 3
14:34, ERROR, 1
14:34, INFO, 1
14:36, INFO, 3
14:36, WARN, 1
14:37, INFO, 4
14:38, DEBUG, 1
14:38, INFO, 1
14:40, INFO, 1
14:41, INFO, 2
14:42, INFO, 1
14:42, WARN, 1
14:44, ERROR, 2
14:45, ERROR, 1
14:45, INFO, 1
```

As we can notice file in the csv format and distributions of various type of log messages in a specific time interval is generated as expected


### Task2
Task2 is to generate the number of ERROR messages in each time interval that match the regex pattern and to sort the time intervals in ascending order of the number of messages.
To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Task2 /input_dir /output_dir``` <br>

In this task we run two maps, first will get the desired result and second will sort it in descending order, let's see the results of first map:

```
14:21, ERROR, 1
14:34, ERROR, 1
14:44, ERROR, 2
14:45, ERROR, 1
```

As we can see, result is correct but we don't get it sorted, so next map will automatically take the previous input and process it further:

```
2, 14:44, ERROR
1, 14:45, ERROR
1, 14:34, ERROR
1, 14:21, ERROR
```

In this example I have used the input file called moreerrors.log to generate more errors so result is clear

### Task3
Task3 is to calculate ALL types of errors across the file

To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Task3 /input_dir /output_dir``` <br>

Output
```
DEBUG, 48
ERROR, 31
INFO, 416
WARN, 111
```
We can see that after running it we can the desired output.

### Task4
Task4 is basically finding the length of the longest string in each message type which matches the given regex pattern.<br>

To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Task4 /input_dir /output_dir``` <br>

Output

```
DEBUG   68
ERROR   58
INFO    87
WARN    81
```

In this example I have calculated the length of the longest string in each message type which matches the regex pattern that is specified in application.conf:
```scala
Pattern = "([a-c][e-g][0-3]|[A-Z][5-9][f-w]){5,15}"
```

## Tests
For running tests you need to do:
sbt clean compile test

There are 5 tests implemented as required by specification that will allow you to test getting data from configuration file:
```scala
  behavior of "common configuration parameters"
  val config: Config = ConfigFactory.load("application.conf").getConfig("randomLogGenerator")

  it should "obtain the Pattern" in {
    config.getString("Pattern") shouldBe "([a-c][e-g][0-3]|[A-Z][5-9][f-w]){5,15}"
  }

  it should "obtain the MinString" in {
    config.getInt("MinString") shouldBe 10
  }

  it should "obtain the MaxString" in {
    config.getInt("MaxString") shouldBe 50
  }

  it should "obtain the Frequency" in {
    config.getDouble("Frequency") shouldBe 0.07
  }

  it should "obtain the MaxCount" in {
    config.getInt("MaxCount") shouldBe 5000
  }
 ```
 
 
