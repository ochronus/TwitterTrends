package ochronus.twitter.trends
//http://bcomposes.wordpress.com/2013/02/09/using-twitter4j-with-scala-to-access-streaming-tweets/
import twitter4j._
 
object StatusStreamer {
    object Util {
      val config = new twitter4j.conf.ConfigurationBuilder()
        .setOAuthConsumerKey("")
        .setOAuthConsumerSecret("")
        .setOAuthAccessToken("")
        .setOAuthAccessTokenSecret("")
        .build
        
        def simpleStatusListener = new StatusListener() {
          def onStatus(status: Status) { println(status.getText) }
          def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
          def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
          def onException(ex: Exception) { ex.printStackTrace }
          def onScrubGeo(arg0: Long, arg1: Long) {}
          def onStallWarning(warning: StallWarning) {}
        }
    }
    
    

        
    def main(args: Array[String]) {

        val twitterStream = new TwitterStreamFactory(Util.config).getInstance
        twitterStream.addListener(Util.simpleStatusListener)
        twitterStream.sample
        Thread.sleep(20000)
        twitterStream.cleanUp
        twitterStream.shutdown 
 
  }
}