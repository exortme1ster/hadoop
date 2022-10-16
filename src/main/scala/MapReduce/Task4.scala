package MapReduce

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapred.*
import collection.JavaConverters.asScalaIteratorConverter
//import jdk.CollectionConverters.IteratorHasAsScala
import java.io.IOException
import java.util
import com.typesafe.config.{Config, ConfigFactory}

object Task4:
  /*
    Fourth map/reduce is to finding the length of the longest string in each message type which matches the given regex pattern.
  */
  class Map extends MapReduceBase with Mapper[LongWritable, Text, Text, IntWritable] :
    private val word = new Text()
    // get the config
    val config: Config = ConfigFactory.load("application.conf").getConfig("randomLogGenerator")

    @throws[IOException]
    def map(key: LongWritable, value: Text, output: OutputCollector[Text, IntWritable], reporter: Reporter): Unit =
      val line: String = value.toString
      // check if pattern exists in line
      if (config.getString("Pattern").r.findAllIn(line).nonEmpty == true) {
        // get the log type
        val logType = line.split(" ")(2)
        // get the regex pattern value
        val regexPattern = config.getString("Pattern").r.findFirstIn(value.toString)
        word.set(logType)
        // check the log type and get length of substring
        if(logType == "INFO") {
          output.collect(word, new IntWritable(regexPattern.get.length))
        }
        else if (logType == "WARN") {
          output.collect(word, new IntWritable(regexPattern.get.length))
        }
        else if (logType == "ERROR") {
          output.collect(word, new IntWritable(regexPattern.get.length))
        }
        else if (logType == "DEBUG") {
          output.collect(word, new IntWritable(regexPattern.get.length))
        }
      }

  class Reduce extends MapReduceBase with Reducer[Text, IntWritable, Text, IntWritable] :
    override def reduce(key: Text, values: util.Iterator[IntWritable], output: OutputCollector[Text, IntWritable], reporter: Reporter): Unit =
      // The foldLeft method takes an associative binary operator function as parameter and will use it to collapse elements from the collection
      val max = values.asScala.foldLeft(0) { (t, i) => t max i.get }
      output.collect(key, new IntWritable(max))

  @main def RunTask4(inputPath: String, outputPath: String) =
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
