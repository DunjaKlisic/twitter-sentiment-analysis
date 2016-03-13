(ns twitter_sentiment_analysis.data_analysis
  (:use [clj-ml.io] [clj-ml.filters] [clj-ml.classifiers] [clj-ml.data] [clj-ml.utils])
  (:import [weka.core.converters ArffLoader CSVLoader]
           [java.io File]))


(defn ->options
 [& opts]
 (into-array String
 (map str (flatten (remove nil? opts)))))

(defn load-csv
 ([filename & {:keys [header]
 :or {header true}}]
 (let [options (->options (when-not header "-H"))
 loader (doto (CSVLoader.)
 (.setOptions options)
 (.setSource (File. filename)))]
 (.getDataSet loader))))

(def data (load-csv "trainingDataNew.csv"))

(def filter-to-string (new weka.filters.unsupervised.attribute.NominalToString))

(defn transform-to-string []
  (.setAttributeIndexes filter-to-string "1")
  (.setInputFormat filter-to-string data)
  (weka.filters.Filter/useFilter data filter-to-string))

(def trainingData (transform-to-string))


(def classifier (make-classifier :bayes :naive))

(defn train-classifier [classifier data]
  (classifier-train classifier (dataset-set-class data 1)))



(defn save-classifier [classifier path]
  (serialize-to-file classifier path))

(defn classify-a-tweet [classifierFileName text]
  (let [to-classify (make-instance (make-dataset "name" [{:text [text]} {:sentiment [:positive :negative]}] []) {:text text, :sentiment :negative})]
	(classifier-classify (deserialize-from-file classifierFileName) to-classify)))

(defn get-sentiment [classifierFileName text]
  (let [sentiment (classify-a-tweet classifierFileName text)]
  (if(= sentiment 0.0)
    (print-str "positive")
    (print-str "negative"))))

