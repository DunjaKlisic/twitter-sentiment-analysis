(ns twitter-sentiment-analysis-clj.handler_test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [web_app.handler :as handler]))

(deftest test-web-app
  (testing "main route"
    (let [response (handler/app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Twitter sentiment analysis"))))
  (testing "not-found route"
    (let [response (handler/app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(run-all-tests)