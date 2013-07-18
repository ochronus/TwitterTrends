package ochronus.twitter.trends

import twitter4j._
import com.typesafe.config.ConfigFactory

object StatusStreamer {

    val config = ConfigFactory.load

    object Util {
      val twitter_config = new twitter4j.conf.ConfigurationBuilder()
        .setOAuthConsumerKey(config.getString("twitter.OAuthConsumerKey"))
        .setOAuthConsumerSecret(config.getString("twitter.OAuthConsumerSecret"))
        .setOAuthAccessToken(config.getString("twitter.OAuthAccessToken"))
        .setOAuthAccessTokenSecret(config.getString("twitter.OAuthAccessTokenSecret"))
        .build

        val places = scala.collection.mutable.Map.empty[String, Int]
        val hashtags = scala.collection.mutable.Map.empty[String, Int]
        var message_count = 0

        def simpleStatusListener = new StatusListener() {
          def onStatus(status: Status) {
            message_count += 1
            val msg = status.getText.toLowerCase

            for (m <- """#[^\s]+""".r findAllMatchIn msg) {
              val word = m.matched
              hashtags(word) = if (hashtags contains word) hashtags(word) + 1 else 1
            }

            if (status.getPlace != null) {
              val country =  status.getPlace.getCountry

              places(country) = if (places contains country) places(country) + 1 else 1
            }
          }

          def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
          def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
          def onException(ex: Exception) { ex.printStackTrace }
          def onScrubGeo(arg0: Long, arg1: Long) {}
          def onStallWarning(warning: StallWarning) {}
        }
    }



    def main(args: Array[String]) {

        val twitterStream = new TwitterStreamFactory(Util.twitter_config).getInstance
        twitterStream.addListener(Util.simpleStatusListener)
        twitterStream.sample
        Thread.sleep(6000)
        twitterStream.cleanUp
        twitterStream.shutdown
        Console.println(Util.places)
        Console.println(Util.hashtags.filter( e => e._2 >4 ).toList sortBy {_._2})
        Console.println(Util.message_count)
        Console.println(Util.hashtags.keys.toList.length)
  }
}