(ns myproject.routes.services.riccode
  (require [myproject.db.core :as db]
           [ring.util.http-response :refer :all]))

(defn get-code [ric]
  (ok (db/get-code {:ric ric})))

(defn search-code [ric]
  (ok (db/search-code {:ric ric})))