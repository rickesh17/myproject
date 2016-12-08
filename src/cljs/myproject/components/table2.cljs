(ns myproject.components.table2
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-table.core :as rt]
            [goog.events :as events]
            [ajax.core :refer [GET POST]]))

(def table-data {:headers ["RIC-Code" "Isin" "Currency" "Description"]
                 :rows []})

(defonce fields (atom {}))
(defonce errors (atom nil))
(defonce tdata (atom table-data))

(defn get-ric [ric isin]
  (GET "/search-isin"
       {:params {:ric ric}
        :headers {"Accept" "application/transit+json"}
        :handler #(do
                   (.log js/console (str "get-msg response : " %))
                   (reset! isin %)
                   (reset! tdata {:headers ["RIC-Code" "Isin" "Currency" "Description"]
                                  :rows    @isin}))}))

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))

(defn isin-form2 [isin]
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
      :on-click #(do
                  (get-ric (:ric @fields) isin))
      :value    "Search RIC-Code"}]
    [rt/reagent-table tdata]]])

(defn tablescreen2 []
  (let [isin (atom "test")]
    (fn []
      [:div
       [:div.container
        [:div.span12
         [isin-form2 isin]]]
       ]
      )))