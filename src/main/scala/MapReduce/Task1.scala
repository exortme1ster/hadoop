package MapReduce

import Generation.LogMsgSimulator.logger
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapred.*
import collection.JavaConverters.asScalaIteratorConverter
//import jdk.CollectionConverters.IteratorHasAsScala
import java.io.IOException
import java.util
import com.typesafe.config.{Config, ConfigFactory}

object Task1:
  /*
    First map/reduce is used to generate distributions of various type of log messages in a specific time interval
  */

  class Map extends MapReduceBase with Mapper[LongWritable, Text, Text, IntWritable] :
    private final val one = new IntWritable(1)
    private val word = new Text()
    // load the config
    val config: Config = ConfigFactory.load("application.conf").getConfig("randomLogGenerator")

    @throws[IOException]
    def map(key: LongWritable, value: Text, output: OutputCollector[Text, IntWritable], reporter: Reporter): Unit =
      val line: String = value.toString
      if (config.getString("Pattern").r.findAllIn(line).nonEmpty == true) {
        // get the time
        val time = line.split(" ")(0).substring(0, 5)
        // get the logtype
        val logType = line.split(" ")(2)
        // combine to get token
        val token = time + ", " + logType
        word.set(token)
        // [21:00, ERROR], [1]
        output.collect(word, one)
      }

  class Reduce extends MapReduceBase with Reducer[Text, IntWritable, Text, IntWritable] :
    override def reduce(key: Text, values: util.Iterator[IntWritable], output: OutputCollector[Text, IntWritable], reporter: Reporter): Unit =
      // group up and sum values
      val sum = values.asScala.reduce((valueOne, valueTwo) => new IntWritable(valueOne.get() + valueTwo.get()))
      // reduce
      output.collect(key, new IntWritable(sum.get()))

  @main def main(inputPath: String, outputPath: String) =
    logger.info("Job 1 started!");
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
    conf.setJarByClass(classOf[Map])
    conf.setReducerClass(classOf[Reduce])
    conf.setInputFormat(classOf[TextInputFormat])
    conf.setOutputFormat(classOf[TextOutputFormat[Text, IntWritable]])
    FileInputFormat.setInputPaths(conf, new Path(inputPath))
    FileOutputFormat.setOutputPath(conf, new Path(outputPath))
    JobClient.runJob(conf)
    logger.info("Job 1 finished!");
