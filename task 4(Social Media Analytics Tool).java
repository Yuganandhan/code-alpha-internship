import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import opennlp.tools.sentiment.SentimentModel;
import opennlp.tools.sentiment.SentimentAnalyzer;
import opennlp.tools.sentiment.SentimentFactory;
import opennlp.tools.sentiment.SentimentME;
import opennlp.tools.util.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SocialMediaAnalyzer {

    public static void main(String[] args) {
        // Set your Twitter API keys
        String apiKey = "YOUR_API_KEY";
        String apiSecretKey = "YOUR_API_SECRET_KEY";
        String accessToken = "YOUR_ACCESS_TOKEN";
        String accessTokenSecret = "YOUR_ACCESS_TOKEN_SECRET";

        // Initialize Twitter configuration
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(apiKey)
                .setOAuthConsumerSecret(apiSecretKey)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        // Create TwitterFactory
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            // Search for tweets with a specific hashtag
            String hashtagToSearch = "Java";
            Query query = new Query("#" + hashtagToSearch);
            query.setCount(100); // Adjust the number of tweets to retrieve
            QueryResult result = twitter.search(query);

            // Analyze and display insights
            analyzeAndDisplayInsights(result.getTweets());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static void analyzeAndDisplayInsights(List<Status> tweets) {
        // Collect hashtags and user mentions
        List<String> hashtags = tweets.stream()
                .flatMap(tweet -> tweet.getHashtagEntities().length > 0
                        ? List.of(tweet.getHashtagEntities()).stream().map(HashtagEntity::getText)
                        : List.<String>of().stream())
                .collect(Collectors.toList());

        List<String> userMentions = tweets.stream()
                .flatMap(tweet -> tweet.getUserMentionEntities().length > 0
                        ? List.of(tweet.getUserMentionEntities()).stream().map(UserMentionEntity::getScreenName)
                        : List.<String>of().stream())
                .collect(Collectors.toList());

        // Display insights
        System.out.println("Total Tweets: " + tweets.size());
        System.out.println("Popular Hashtags: " + hashtags);
        System.out.println("User Mentions: " + userMentions);

        // Perform sentiment analysis
        try {
            InputStream modelIn = new FileInputStream("path/to/sentiment/model"); // Replace with your model file
            SentimentModel model = new SentimentModel(modelIn);
            SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer(model, new SentimentFactory());
            SentimentME sentimentME = new SentimentME(model);

            // Analyze sentiment of each tweet
            for (Status tweet : tweets) {
                double[] outcomes = sentimentME.predictProbabilities(tweet.getText());
                String sentiment = sentimentAnalyzer.getBestCategory(outcomes);
                System.out.println("Sentiment of tweet: " + tweet.getText() + " is " + sentiment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
