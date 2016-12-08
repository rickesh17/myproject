(ns myproject.routes.home
  (:require [myproject.layout :as layout]
            [myproject.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [clojure.tools.logging :as log]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [myproject.routes.services.riccode :as r]))

(defn home-page []
  (layout/render "home.html"))

(defn get-isin2 [ric]
  (log/debug "The passed in ric is " ric)
  "XY5504013")

(defn get-isin [ric]
  (log/debug "The passed in ric is " (str ric "%"))
  (map (-> (r/get-code (str ric \%)) :body) [:isin]))

(defn search-isin [ric]
  (log/debug "The passed in ric is " (str ric "%"))
  (into () (mapv (juxt :ric :isin :currency :description) ((r/search-code (str ric \%)) :body))))

(defroutes home-routes
           (GET "/" [] (home-page))
           (GET "/get-isin" [ric] (response/ok (get-isin ric)))
           (GET "/search-isin" [ric] (response/ok (search-isin ric)))
           (GET "/docs" [] (response/ok (-> "docs/docs.md" io/resource slurp))))

