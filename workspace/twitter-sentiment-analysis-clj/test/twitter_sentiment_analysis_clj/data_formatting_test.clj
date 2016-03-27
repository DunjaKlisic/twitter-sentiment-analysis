(ns twitter-sentiment-analysis-clj.data_formatting_test
  (:require [clojure.test :refer :all]
            [twitter_sentiment_analysis.data_formatting :as df]))

(deftest test-strip-emoticons
  (is (= "" (df/strip-emoticons ":)")))
  (is (= "" (df/strip-emoticons ":(")))
  (is (= "" (df/strip-emoticons ":D")))
  (is (= "" (df/strip-emoticons ";)")))
  (is (= "" (df/strip-emoticons "")))
  (is (= "tweet " (df/strip-emoticons "tweet :P")))
  (is (thrown? NullPointerException (df/strip-emoticons nil))))

(deftest test-strip-usernames
  (is (= "USERNAME" (df/strip-usernames "@John")))
  (is (= "USERNAME tweet" (df/strip-usernames "@John tweet")))
  (is (= "tweet USERNAME tweet" (df/strip-usernames "tweet @John tweet")))
  (is (thrown? NullPointerException (df/strip-usernames nil))))

(deftest test-strip-urls
  (is (= "URL" (df/strip-urls "http://t.co/link")))
  (is (= "URL" (df/strip-urls "https://t.co/link")))
  (is (= "URL tweet" (df/strip-urls "http://t.co/link tweet")))
  (is (= "tweet URL tweet" (df/strip-urls "tweet https://t.co/link tweet")))
  (is (thrown? NullPointerException (df/strip-urls nil))))

(deftest test-strip-repeated-letters
  (is (= "aa" (df/strip-repeated-letters "aa")))
  (is (= "aa" (df/strip-repeated-letters "aaaaaaa")))
  (is (= "tweet" (df/strip-repeated-letters "tweet")))
  (is (= "tweet" (df/strip-repeated-letters "tweeeeeet")))
  (is (thrown? NullPointerException (df/strip-repeated-letters nil))))

(deftest test-strip-commas
  (is (= " " (df/strip-commas ",")))
  (is (= "tweet  tweet" (df/strip-commas "tweet, tweet")))
  (is (thrown? NullPointerException (df/strip-commas nil))))

(deftest test-strip-apostrophe
  (is (= "" (df/strip-apostrophe "'")))
  (is (= "tweets tweet" (df/strip-apostrophe "tweet's tweet")))
  (is (thrown? NullPointerException (df/strip-apostrophe nil))))

(deftest test-strip-double-apostrophe
  (is (= "" (df/strip-double-apostrophe "\"")))
  (is (= "tweet tweet" (df/strip-double-apostrophe "tweet \"tweet\"")))
  (is (thrown? NullPointerException (df/strip-double-apostrophe nil))))

(run-all-tests)
