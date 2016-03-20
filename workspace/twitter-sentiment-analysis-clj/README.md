# Twitter Sentiment Analysis

##About the project

[Twitter] (https://twitter.com/) is a social network that allows users to publish and read short messages that are called tweets and that express their oppinion on different aspects of life. The idea of this project is creating an app that is capable to learn the difference between statuses with positive and negative sentiment, based on the collection of tweets and, after that, successfully classify a tweet that the user chooses. Trening is achieved by using unsupervised method for machine learning.
This project is based on paper [Twitter Sentiment Classification using Distant Supervision] (http://s3.eddieoz.com/docs/sentiment_analysis/Twitter_Sentiment_Classification_using_Distant_Supervision.pdf). 

##Solution

Application execution consists of the following steps:

1.	Status collection

2.	Processing of the previously saved data

3.	Creation of data for trening

4.	Application of machine learning methods.

###Status collection

For tweet collection [Twitter Search API] (https://dev.twitter.com/rest/public/search) was used, a part of Twitter's **REST API**. On za zadati upit sa definisanim parametrima vraća statuse koji odgovaraju tim parametrima. Pre korišćenja ovog API-ja neophodno je registrovati aplikaciju na Twitter-u.
 
Potrebno je napisati upit koji će eliminisati tweet-ove koje predstavljaju retweet-ove (status koji je određeni korisnik preuzeo od nekog drugog korisnika), a za kriterijum pretrage prvo je postavljen emotikon :) za dobijanje statusa koji izražavaju pozitivna osećanja a zatim emotikon :(, za dobijanje tweet-ova koji će kasnije biti označeni kao negativni. Dobijeni upiti su:
```
:) + exclude:retweets

:( + exclude:retweets
```

U narednoj tabeli su dati emotikoni koje twitter mapira na :) i emotikoni koje twitter mapira na :( :


| Mapirani na :)  | Mapirani na :( |
| ------------- | ------------- |
| :)  | :(  |
| :-)  | :-(  |
| : )  | : (  |
| :D  |   |
| =)  |   |

Kako postoji ograničenje na broj statusa koje klijentska aplikacija može da dobije u jednom zahtevu (najviše 100), neophodna je iteracija kroz rezultat pretrage.

Da se u rezultatu ne bi javljali duplikati, koristi se parametar **max_id**. U prvom zahtevu zadaje se samo broj statusa, a za sledeće se beleži najmanji ID od svih primljenih tweet-ova. Taj ID se umanjuje za jedan i prosleđuje kao vrednost za **max_id** narednog zahteva. Tako se obezbeđuje da on vraća samo statuse sa ID-ijem koji je manji ili jednak vrednosti **max_id** parametra.

Za upit se postavlja još jedan parametar koji se odnosi na jezik na kojem će biti statusi koji su rezultat pretrage. Taj parametar je **lang** i dodeljuje mu se vrednost **sr** kako bi upit vratio samo tweet-ove na srpskom jeziku. 

U radu je potrebno prikupiti 1000 negativnih i 1000 pozitivnih statusa, pa se ovaj postupak ponavlja po 10 puta za oba slučaja. Za ovaj projekat, pozitivni statusi su prikupljeni za dve nedelje, a statusi sa negativnim sentimentom za mesec dana.

Prikupljeni statusi se skladište u json fajlovima [data/positive.json] (https://raw.githubusercontent.com/DunjaKlisic/TwitterSentimentAnalysis/master/data/positive.json) i [data/negative.json](https://raw.githubusercontent.com/DunjaKlisic/TwitterSentimentAnalysis/master/data/negative.json).
