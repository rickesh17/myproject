(ns myproject.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [myproject.layout :refer [error-page]]
            [myproject.routes.home :refer [home-routes]]
            [myproject.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [myproject.env :refer [defaults]]
            [mount.core :as mount]
            [myproject.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'service-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
