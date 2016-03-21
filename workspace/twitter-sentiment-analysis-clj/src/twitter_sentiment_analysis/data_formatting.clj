(ns twitter_sentiment_analysis.data_formatting
  (:use
   [clojure.java.io :as io])
  (:require
   [clojure.data.json :as json])

(defn strip-emoticons [text]
  (clojure.string/replace text #"[:;=B](')*(-)*[)(P3DSO*sp]+" ""))

(defn strip-usernames [text]
  (clojure.string/replace text #"@[A-Za-z0-9_]*" "USERNAME"))

(defn strip-urls [text]
  (clojure.string/replace text #"http(s)*://t.co/[A-Za-z0-9_]*" "URL"))

(defn strip-repeated-letters [text]
  (clojure.string/replace text #"([a-zA-Z])\1+" "$1$1"))

(defn strip-commas [text]
  (clojure.string/replace text "," " "))

(defn strip-apostrophe [text]
  (clojure.string/replace text "\'" ""))

(defn strip-double-apostrophe [text]
  (clojure.string/replace text "\"" ""))


(defn reduce-features [path-positive path-negative path-dataset]
  (with-open [out-file (io/writer path-dataset) rdrp (reader path-positive) rdrn (reader path-negative)]
    (csv/write-csv out-file [["text" "sentiment"]])
    (doseq [line (line-seq rdrp)]
      (csv/write-csv out-file [[((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons strip-commas strip-apostrophe strip-double-apostrophe) (:text (json/read-str line :key-fn keyword))) "positive"]]))
    (doseq [line (line-seq rdrn)]
      (csv/write-csv out-file [[((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons strip-commas strip-apostrophe strip-double-apostrophe) (:text (json/read-str line :key-fn keyword))) "negative"]]))))