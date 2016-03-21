(ns twitter_sentiment_analysis.data_collection
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.streaming]
   [clojure.java.io :as io])
  (:require
   [clojure.data.json :as json]
   [http.async.client :as ac])
  (:import
   (twitter.callbacks.protocols AsyncStreamingCallback)))

(def my-creds (make-oauth-creds "nemg8aXAA9wtSo8h6ZZbrsYwk"
                                "XmRZzVqlj1i5Ag72RJpd99IQLPoK6dfvbna89SwQ1NHIJVqN1l"
                                "3021555060-YgU9ZHHTKVQGpkbDqNw9By4VNcFz7Ph455VAY0G"
                                "XR9MZ3SE6quDbTfGcPNTiG2QebsdYnepStnZnrrzUeuye"))
(def no-of-positive (atom 0))
(def no-of-negative (atom 0))

(defn collect-positive-tweets []
	(let [w (io/writer "resources/positive_en.json")
	      callback (AsyncStreamingCallback.
	                 (fn [_resp payload]
	                   (if (< @no-of-positive 1000)
		                   (do
		                      (.write w (str payload))
		                      (swap! no-of-positive inc))
		                   (.close w)))
	                 (fn [_resp]
	                   (.close w))
	                 (fn [_resp ex]
	                   (.close w)
	                   (.printStackTrace ex)))]
	  (statuses-filter
	    :params {:lang "en" :track ":)"}
	    :oauth-creds my-creds
	    :callbacks callback)))


(defn collect-negative-tweets []
	(let [w (io/writer "resources/negative_en.json")
	      callback (AsyncStreamingCallback.
	                 (fn [_resp payload]
	                   (if (< @no-of-negative 1000)
		                   (do
		                      (.write w (str payload))
		                      (swap! no-of-negative inc))
		                   (.close w)))
	                 (fn [_resp]
	                   (.close w))
	                 (fn [_resp ex]
	                   (.close w)
	                   (.printStackTrace ex)))]
	  (statuses-filter
	    :params {:lang "en" :track ":("}
	    :oauth-creds my-creds
	    :callbacks callback)))