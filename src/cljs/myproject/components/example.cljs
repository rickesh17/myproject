(ns myproject.components.example
  (:require [reagent.core :as r]
            [ajax.core :as ajax]))

(defn input-ric [ric]
  [:input {:type  "text"
           :value @ric
           :on-change #(reset! ric (-> % .-target .-value))
           }])

(defn output-ric [ric]
  (if (= @ric "XOM.N")
    (reset! ric "US30231G1022")))

(defn enter-ric []
  (let [val (r/atom "")]
    (fn []
      [:div
       [:p "RIC Code: " [input-ric val] [output-ric val]]
       ])))