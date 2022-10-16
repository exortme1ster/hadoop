# Homework 2 - Nikita Mashchenko
The goal of this homework is to use Hadoop map/reduce framework in scala to generate distributions of type log messages according to given regex pattern and time interval.

AWS deployment video - [video link](https://www.youtube.com/watch?v=A3g_ttNZq98)
## Prerequisites
Since we are using Hadoop, require Hadoop 3.0.1 installed on Windows 10. <br>
SBT installed and configured in Intellij. <br>
Scala 3.0.1. <br>

## Instructions to run locally in hadoop
clone the repo - ```https://github.com/Ashwin1234/CS441LogFileGenerator.git``` <br>
First step is to generate the jar file in Intellij.
run the command - ```sbt clean compile assembly``` this will generate the jar file. Then copy this jar file into a folder which has access. <br>
Run the hadoop clusters by running ```Start-all.cmd``` with cmd in admin mode in sbin folder. <br>
Create an input folder by executing command - ```hadoop fs -mkdir /input_dir``` <br>
Copy the input log files (input_file.txt) into this folder - ```hadoop fs -put C:/input_file.txt /input_dir``` <br>
Run the command - ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.<Task Number> /input_dir /output_dir``` this will start the hadoop map/reduce job. <br>
Finally to view the output run the command - ```hadoop dfs -cat /output_dir/* ``` <br>


## Project structure
1) resources - Contains the various config files
2) Generation - Responsible for generating the log files
3) MapReduce/One.scala - Task1
4) MapReduce/Two.scala - Task2
5) MapReduce/Three.scala - Task3
6) MapReduce/Four.scala - Task4
7) HelperUtils - HelperUtils
8) Test/test.scala - contains various tests to be performed on the four tasks.

## Tasks
### Task1
Task1 is to generate the distributions of various type of log messages in a specific time interval.
To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.One /input_dir /output_dir``` <br>
Output -
```18:23:12 DEBUG  0
18:23:12 ERROR  0
18:23:12 INFO   1
18:23:12 WARN   0
18:23:13 DEBUG  0
18:23:13 ERROR  0
18:23:13 INFO   3
18:23:13 WARN   2
18:23:14 DEBUG  4
18:23:14 ERROR  2
18:23:14 INFO   17
18:23:14 WARN   12
18:23:15 DEBUG  2
18:23:15 ERROR  0
18:23:15 INFO   26
18:23:15 WARN   8
18:23:16 DEBUG  5
18:23:16 ERROR  0
18:23:16 INFO   34
18:23:16 WARN   11
18:23:17 DEBUG  2
18:23:17 ERROR  0
18:23:17 INFO   36
18:23:17 WARN   9
18:23:18 DEBUG  4
18:23:18 ERROR  1
18:23:18 INFO   41
18:23:18 WARN   11
18:23:19 DEBUG  5
18:23:19 ERROR  0
18:23:19 INFO   40
18:23:19 WARN   11
18:23:20 DEBUG  8
18:23:20 ERROR  0
18:23:20 INFO   41
18:23:20 WARN   10
18:23:21 DEBUG  7
18:23:21 ERROR  0
18:23:21 INFO   48
18:23:21 WARN   6
18:23:22 DEBUG  6
18:23:22 ERROR  0
18:23:22 INFO   42
18:23:22 WARN   11
18:23:23 DEBUG  8
18:23:23 ERROR  0
18:23:23 INFO   42
18:23:23 WARN   10
18:23:24 DEBUG  4
18:23:24 ERROR  1
18:23:24 INFO   40
18:23:24 WARN   12
18:23:25 DEBUG  6
18:23:25 ERROR  0
18:23:25 INFO   47
18:23:25 WARN   11
18:23:26 DEBUG  5
18:23:26 ERROR  1
18:23:26 INFO   46
18:23:26 WARN   10
18:23:27 DEBUG  3
18:23:27 ERROR  1
18:23:27 INFO   50
18:23:27 WARN   10
18:23:28 DEBUG  11
18:23:28 ERROR  0
18:23:28 INFO   43
18:23:28 WARN   9
18:23:29 DEBUG  11
18:23:29 ERROR  0
18:23:29 INFO   35
18:23:29 WARN   17
18:23:30 DEBUG  6
18:23:30 ERROR  0
18:23:30 INFO   44
18:23:30 WARN   13
18:23:31 DEBUG  10
18:23:31 ERROR  0
18:23:31 INFO   44
18:23:31 WARN   11
18:23:32 DEBUG  3
18:23:32 ERROR  0
18:23:32 INFO   54
18:23:32 WARN   8
18:23:33 DEBUG  8
18:23:33 ERROR  0
18:23:33 INFO   41
18:23:33 WARN   14
18:23:34 DEBUG  5
18:23:34 ERROR  1
18:23:34 INFO   41
18:23:34 WARN   17
18:23:35 DEBUG  2
18:23:35 ERROR  2
18:23:35 INFO   50
18:23:35 WARN   10
18:23:36 DEBUG  8
18:23:36 ERROR  1
18:23:36 INFO   46
18:23:36 WARN   9
18:23:37 DEBUG  9
18:23:37 ERROR  1
18:23:37 INFO   44
18:23:37 WARN   10
18:23:38 DEBUG  5
18:23:38 ERROR  0
18:23:38 INFO   48
18:23:38 WARN   11
18:23:39 DEBUG  4
18:23:39 ERROR  0
18:23:39 INFO   19
18:23:39 WARN   5
```

In this example I have taken the time interval as one second and have calculated the number of log messages of each type in each time interval.


### Task2
Task2 is to generate the number of ERROR messages in each time interval and to sort the time intervals in ascending order of the number of messages.
To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Two /input_dir /output_dir``` <br>

Output

```18:23:35        2
18:23:14        2
18:23:24        1
18:23:26        1
18:23:27        1
18:23:36        1
18:23:37        1
18:23:18        1
18:23:34        1
18:23:25        0
18:23:28        0
18:23:29        0
18:23:20        0
18:23:21        0
18:23:22        0
18:23:23        0
18:23:13        0
18:23:15        0
18:23:16        0
18:23:38        0
18:23:17        0
18:23:39        0
18:23:19        0
18:23:30        0
18:23:31        0
18:23:32        0
18:23:33        0
18:23:12        0
```
In this example I have used 1 second as time interval and calculated the number of ERROR messages in each interval and sorted the intervals based on the number of ERROR messages using cleanup method in Reducer class and sorting by values to get sorted keys.

### Task3
Task3 is basically calculating the number of each message type.
To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Three /input_dir /output_dir``` <br>

Output
```
DEBUG   151
ERROR   11
INFO    1063
WARN    278
```
In this example I have used calculated the number of INFO, ERROR, WARN and DEBUG messages in the input file.

### Task4
Task4 is basically finding the length of the longest string in each message type which matches the given regex pattern.<br>
To run ```hadoop jar C:/LogFileGenerator-assembly-0.1.jar MapReduce.Four /input_dir /output_dir``` <br>

Output

```
DEBUG   68
ERROR   58
INFO    87
WARN    81
```

In this example I have calculated the length of the longest string in each message type which matches the regex pattern ```"(.*?)".r```

## Tests
1) The test to check the regex pattern
2) Test to check the time interval

## Future work
A more complex problem can be solved by using the Map/Reduce concept and deployed in AWS.
