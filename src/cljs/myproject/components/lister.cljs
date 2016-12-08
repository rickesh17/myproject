(ns myproject.components.lister)

(defn lister [items]
  [:ul
   (for [item items]
     ^{:key item} [:li "Number " item])])

(defn lister-user []
  [:div
   "Here is a " [:strong "list"] ":"
   [lister (range 4)]])