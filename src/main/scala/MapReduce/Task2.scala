package MapReduce

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapred.*
import collection.JavaConverters.asScalaIteratorConverter
//import jdk.CollectionConverters.IteratorHasAsScala
import java.io.IOException
import java.util
import com.typesafe.config.{Config, ConfigFactory}

object Task2:
  /*
    Second map/reduce is to generate the number of ERROR messages in each time interval and to sort the time intervals in ascending order of the number of messages.
  */

  // first job, get all instances of ERROR for specific pattern
  class Map extends MapReduceBase with Mapper[LongWritable, Text, Text, IntWritable] :
    private final val one = new IntWritable(1)
    private val word = new Text()
    val config: Config = ConfigFactory.load("application.conf").getConfig("randomLogGenerator")

    @throws[IOException]
    def map(key: LongWritable, value: Text, output: OutputCollector[Text, IntWritable], reporter: Reporter): Unit =
      val line: String = value.toString
      // check if pattern exist in line
      if (config.getString("Pattern").r.findAllIn(line).nonEmpty == true && line.split(" ")(2) == "ERROR") {
        // get the time
        val time = line.split(" ")(0).substring(0, 5)
        // get the logType
        val logType = line.split(" ")(2)
        // get the token
        val token = time + ", " + logType
        // set key - value pair
        word.set(token)
        output.collect(word, one)
      }

  // second job, sort the results from first job
  class Reduce extends MapReduceBase with Reducer[Text, IntWritable, Text, IntWritable] :
    override def reduce(key: Text, values: util.Iterator[IntWritable], output: OutputCollector[Text, IntWritable], reporter: Reporter): Unit =
      val sum = values.asScala.reduce((valueOne, valueTwo) => new IntWritable(valueOne.get() + valueTwo.get()))
      output.collect(key, new IntWritable(sum.get()))

  class Map2 extends MapReduceBase with Mapper[LongWritable, Text, IntWritable, Text]:
    private val word = new Text()
    val config: Config = ConfigFactory.load("application.conf").getConfig("randomLogGenerator")
    @throws[IOException]
    def map(key: LongWritable, value: Text, output: OutputCollector[IntWritable, Text], reporter: Reporter): Unit =
      // get the line
      val line: String = value.toString
      // get the time [14:21, ERROR, 1]
      val time: String = line.split(", ")(0)
      // get the logType [14:21, ERROR, 1]
      val logType: String = line.split(", ")(1)
      // get the count [14:21, ERROR, 1]
      val count: Int = line.split(", ")(2).toInt*(-1)
      // build the string
      word.set(time + ", " + logType)
      output.collect(new IntWritable(count), word)

  class Reduce2 extends MapReduceBase with Reducer[IntWritable, Text, IntWritable, Text] :
    override def reduce(key: IntWritable, values: util.Iterator[Text], output: OutputCollector[IntWritable, Text], reporter: Reporter): Unit =
      // get the current value
      val curNumber: Int = key.get()
      // match the case
      key match
        case negative if curNumber < 0 => values.forEachRemaining(value => output.collect(new IntWritable(-curNumber), new Text(value.toString)))
        case positive if curNumber > 0 => values.forEachRemaining(value => output.collect(new IntWritable(curNumber), new Text(value.toString)))


  @main def RunTask2(inputPath: String, outputPath: String) =
    // first setup
    val conf: JobConf = new JobConf(this.getClass)
    conf.setJobName("WordCount")
    conf.set("fs.defaultFS", "local")
    conf.set("mapreduce.job.maps", "1")
    conf.set("mapreduce.job.reduces", "1")
    conf.set("mapred.textoutputformat.separator", ", ")
    conf.setOutputKeyClass(classOf[Text])
    conf.setOutputValueClass(classOf[IntWritable])
    conf.setMapperClass(classOf[Map])
    conf.setCombinerClass(classOf[Reduce])
    conf.setReducerClass(classOf[Reduce])
    conf.setInputFormat(classOf[TextInputFormat])
    conf.setOutputFormat(classOf[TextOutputFormat[Text, IntWritable]])
    FileInputFormat.setInputPaths(conf, new Path(inputPath))
    FileOutputFormat.setOutputPath(conf, new Path(outputPath))
    JobClient.runJob(conf)

    Thread.sleep(5000) // wait for 5000 millisecond between tasks (to avoid delayed folder creation problem)

    // second setup
    val conf1: JobConf = new JobConf(this.getClass)
    conf1.setJobName("WordCount1")
    conf1.set("fs.defaultFS", "local")
    conf1.set("mapreduce.job.maps", "1")
    conf1.set("mapreduce.job.reduces", "1")
    conf1.set("mapred.textoutputformat.separator", ", ")
    conf1.setOutputKeyClass(classOf[IntWritable])
    conf1.setOutputValueClass(classOf[Text])
    conf1.setMapperClass(classOf[Map2])
    conf1.setCombinerClass(classOf[Reduce2])
    conf1.setReducerClass(classOf[Reduce2])
    conf1.setInputFormat(classOf[TextInputFormat])
    conf1.setOutputFormat(classOf[TextOutputFormat[IntWritable, Text]])
    FileInputFormat.setInputPaths(conf1, new Path("C:/Users/vader/IdeaProjects/MapReduceHW1-CS441/src/main/resources/output/task2predata/part-00000"))
    FileOutputFormat.setOutputPath(conf1, new Path("C:/Users/vader/IdeaProjects/MapReduceHW1-CS441/src/main/resources/output/task2"))
    JobClient.runJob(conf1)
