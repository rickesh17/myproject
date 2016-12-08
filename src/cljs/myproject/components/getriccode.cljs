(ns myproject.components.getriccode
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST]]))

(defn get-ric [ric isin]
  (GET "/get-isin"
       {:params {:ric ric}
        :headers {"Accept" "application/transit+json"}
        :handler #(do
                   (.log js/console (str "get-msg response : " %))
                   (reset! isin %))}))

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))

(defn isin-form [isin]
  (let [fields (atom {})
        errors (atom nil)]
    (fn []
      [:div.content
       [errors-component errors :server-error]
       [:div.form-group
        [errors-component errors :name]
        [:p "Enter RIC-Code:"
         [:input.form-control
          {:type      :text
           :name      :ric
           :on-change #(swap! fields assoc :ric (-> % .-target .-value))
           :value     (:ric @fields)}]]
        [errors-component errors :message]
        [:input.btn.btn-primary
         {:type     :submit
          :on-click #(get-ric (:ric @fields) isin)
          :value    "Search RIC-Code"}]]])))

(defonce isin (atom " "))

(defn homescreen []
  [:div
   [:div.container
    [:div.span12
     [isin-form isin]]]
   [:div.container
    [:div.span12
     "The ISIN-Code is: " @isin]]
   ])