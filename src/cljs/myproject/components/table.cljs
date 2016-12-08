(ns myproject.components.table
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-table.core :as rt]
            [goog.events :as events]
            [ajax.core :refer [GET POST]]))

(defn get-ric [ric isin]
  (GET "/search-isin"
       {:params {:ric ric}
        :headers {"Accept" "application/transit+json"}
        :handler #(do
                   (.log js/console (str "get-msg response : " %))
                   (reset! isin %))}))

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))

(def table-data {:headers ["RIC-Code" "Isin" "Currency" "Description"]
                 :rows []})

(def table-data2 {:headers ["Col1" "Col2" "Col3"]
                  :rows    [["R11R" "R12R" "R13R"]
                            ["R21R" "R22R" "R23R"]
                            ["R31R" "R32R" "R33R"]]})

(defn update-table [tdata]
  (do
    (.log js/console (str @tdata))
    (.log js/console (str table-data2))
    (reset! tdata table-data2)))

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

(defn tablescreen []
  (let [tdata (atom table-data)
        isin (atom "test")]
    (fn []
      [:div
       [:div.container
        [:div.span12
         [isin-form isin]]]
       [:div.container
        [rt/reagent-table tdata]]
       [:input.btn.btn-primary
        {:type     :submit
         :on-click #(reset! tdata {:headers ["RIC-Code" "Isin" "Currency" "Description"]
                                   :rows    @isin})
         :value    "update me"}]
       ]
      )))