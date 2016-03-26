(ns web_app.views
 (:require [clojure.string :as str]
          [hiccup.page :as hic-p]
           [twitter_sentiment_analysis.data_analysis :as da]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "Twitter sentiment analysis: " title)]
   (hic-p/include-css "/css/style.css" "/css/reset.css")])

(defn home-page
  []
  (hic-p/html5
   (gen-page-head "Classify a tweet")
   [:div {:class "pen-title"}
    [:h1 "Twitter sentiment analysis"]]
   [:div {:class "module form-module"}
    [:div {:class "form"
           :style "display:block"}
	   [:h2 "Please insert text of a status that you wish to classify"]
	   [:form {:action "/classify-tweet"
             :method "POST"} 
	    [:input {:type "text"
               :name "tweet" 
               :placeholder "Insert text for classification"}]
	    [:button "Classify"]]]]))

(defn classified-tweet-results-page
  [{:keys [tweet]}]
  (let [sentiment (da/get-sentiment "resources/classifier.txt" tweet)]
    (hic-p/html5
     (gen-page-head "Result")
     [:div {:class "pen-title"}
      [:h1 "Twitter sentiment analysis"]]
     [:div {:class "module form-module"}
      [:div {:class "form" 
             :style "display:block"}
       [:h2 "This tweet expresses  "sentiment" emotions"]]
      [:div {:class "cta"}
       [:a {:href "/"} "Try again"]]])))


