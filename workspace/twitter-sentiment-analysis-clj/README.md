# Twitter Sentiment Analysis

##About the project

[Twitter] (https://twitter.com/) is a social network that allows users to publish and read short messages that are called tweets and that express their opinion on different aspects of life. The idea of this project is creating an app that is capable to learn the difference between statuses with positive and negative sentiment, based on the collection of tweets and, after that, successfully classify a tweet that the user chooses. Training is achieved by using unsupervised method for machine learning.
This project is based on paper  [Twitter Sentiment Classification using Distant Supervision] (http://s3.eddieoz.com/docs/sentiment_analysis/Twitter_Sentiment_Classification_using_Distant_Supervision.pdf). 

##Solution

Application execution consists of the following steps:

1.	Status collection

2.	Processing of the previously saved data

3.	Creation of data for training

4.	Application of machine learning methods.

5.	User interaction

###Status collection

For tweet collection a part of Twitter's **REST API**, [Twitter Search API] (https://dev.twitter.com/rest/public/search) was used. For a given query with defined parameters API returns statuses that match those parameters. Before using this API, application has to be registered on Twitter.
 
In order to get tweets with positive sentiment, emoticon :) was firstly used as query. For negative sentiment, emoticon :( was used.

Next table shows all the emoticons that Twitter maps to :), and emoticons that Twitter maps to :( :


| Mapped to :)  | Mapped to :( |
| ------------- | ------------- |
| :)  | :(  |
| :-)  | :-(  |
| : )  | : (  |
| :D  |   |
| =)  |   |


It’s necessary to set up one more parameter that refers to the language of the statuses. That parameter is **lang** and is assigned a value of **en**, so only tweets in English language are retrieved.

1000 positive and 1000 negative tweets were collected and stored in json files [resources/positive_en.json] (https://github.com/DunjaKlisic/twitter-sentiment-analysis/blob/master/workspace/twitter-sentiment-analysis-clj/resources/positive_en.json) and [resources/negative_en.json] (https://github.com/DunjaKlisic/twitter-sentiment-analysis/blob/master/workspace/twitter-sentiment-analysis-clj/resources/negative_en.json).

[twitter-api] (https://github.com/adamwynne/twitter-api) library was used for data collection. Before that, credentials given during application registration have to be used for authorization.

###Processing of the previously saved data

Data from the files [resources/positive_en.json] (https://github.com/DunjaKlisic/twitter-sentiment-analysis/blob/master/workspace/twitter-sentiment-analysis-clj/resources/positive_en.json) and [resources/negative_en.json] (https://github.com/DunjaKlisic/twitter-sentiment-analysis/blob/master/workspace/twitter-sentiment-analysis-clj/resources/negative_en.json) is loaded.

For each status, emoticons are removed because of the noise they would create in training of the classifier. Regular expression used in this case is:
```
[:;=B](')*(-)*[)(P3DSO*sp]+
```
Next step is replacing usernames and links with tokens USERNAME and URL. Regular expressions for finding usernames and links are, respectively:
```
@[A-Za-z0-9_]*

http(s)*://t.co/[A-Za-z0-9_]*
```
Last step is finding the words that contain letters that are repeated more than two times in a row and replacing that letters with only two occurrences. Regular expression used here is:
```
([a-zA-Z])\1+" "$1$1
```
Commas, apostrophes and double apostrophes are also removed, because data is going to be saved in a csv file.

###Creation of data for training

Text of the status from the previous step is stored in a file as a value for the first attribute of the data that will be used for training. The second attribute is the sentiment expressed in a status, and has two possible values, **positive** and **negative**. This dataset is stored in a file [resources/trainingDataNew.csv] (https://github.com/DunjaKlisic/twitter-sentiment-analysis/blob/master/workspace/twitter-sentiment-analysis-clj/resources/trainingDataNew.csv).

###Application of machine learning methods

**Naive Bayes** classifier was used. Training of the classifier is executed on previously processed data, with class index set to sentiment attribute. Classifier is saved in a file [resources/classifier.txt] (https://github.com/DunjaKlisic/twitter-sentiment-analysis/blob/master/workspace/twitter-sentiment-analysis-clj/resources/classifier.txt). After the training, classifier should be able to correctly classify given status as positive or negative. [Weka] (http://www.cs.waikato.ac.nz/ml/weka/) and [clj-ml] (http://clj-ml.artifice.cc/doc/clj-ml.attribute-selection.html) libraries are necessary for this step.

###User interaction

User should enter the text of a tweet that he wishes to classify. After clicking the button **classify**, classifier is loaded from the previously saved file and used to classify newly created instance. A message with the result of classification is shown to the user. Libraries used here are [hiccup] (https://github.com/weavejester/hiccup), [ring] (https://github.com/ring-clojure) and [compojure] (https://github.com/weavejester/compojure).
