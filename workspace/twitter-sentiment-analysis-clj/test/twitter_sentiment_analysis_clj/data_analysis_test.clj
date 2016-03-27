(ns twitter-sentiment-analysis-clj.data_analysis_test
  (:require [clojure.test :refer :all]
            [twitter_sentiment_analysis.data_analysis :as da]))

(deftest test-get-sentiment
  (is (or (= "This tweet expresses positive emotions" (da/get-sentiment "resources/classifier.txt" "positive :)"))
          (= "This tweet expresses negative emotions" (da/get-sentiment "resources/classifier.txt" "positive :)"))))
  (is (= "You have to provide a text for classification" (da/get-sentiment "resources/classifier.txt" "")))
  (is (thrown? java.io.FileNotFoundException (da/get-sentiment "classifier.txt" "positive :)"))))

(run-all-tests)