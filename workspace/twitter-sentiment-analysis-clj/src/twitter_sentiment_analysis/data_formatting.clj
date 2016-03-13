

(defn reduce-features [path]
  (with-open [rdr (reader path)]
  (doseq [line (line-seq rdr)]
    (swap! formatted-tweets conj ((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons) (:text (json/read-str line :key-fn keyword)))))))

(defn save-formatted-tweets [path]
  (let [io/writer path]
    (doseq [tweet formatted-tweets]
      (.write tweet))))

(defn reduce-features [path1 path2]
  (let [w (io/writer path2)]
  (with-open [rdr (reader path1)]
  (doseq [line (line-seq rdr)]
    (.write w ((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons) (:text (json/read-str line :key-fn keyword))))))))

(defn reduce-features-json [path1 path2]
  (let [w (io/writer path2)]
  (with-open [rdr (reader path1)]
  (doseq [line (line-seq rdr)]
    (.write w (json/write-str {:text ((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons) (:text (json/read-str line :key-fn keyword))) :sentiment "positive"}))
    (.write w "\n")))))

(defn reduce-features [path-positive path-negative path-dataset]
  (with-open [out-file (io/writer path-dataset) rdrp (reader path-positive) rdrn (reader path-negative)]
    (csv/write-csv out-file [["text" "sentiment"]])
    (doseq [line (line-seq rdrp)]
      (csv/write-csv out-file [[((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons strip-commas strip-apostrophe strip-double-apostrophe) (:text (json/read-str line :key-fn keyword))) "positive"]]))
    (doseq [line (line-seq rdrn)]
      (csv/write-csv out-file [[((comp strip-repeated-letters strip-urls strip-usernames strip-emoticons strip-commas strip-apostrophe strip-double-apostrophe) (:text (json/read-str line :key-fn keyword))) "negative"]]))))