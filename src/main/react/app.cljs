(ns react.app
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

(defn square []
  [:button {:className "square"} ;;TODO
   ])

(defn board []
  (let [render-square (fn [i] [square])
        status "Next Player X"]
    [:div
     [:div {:className "status"} status]
     [:div {:className "board-row"}
      [render-square 0]
      [render-square 1]
      [render-square 2]]
     [:div {:className "board-row"}
      [render-square 3]
      [render-square 4]
      [render-square 5]]
     [:div {:className "board-row"}
      [render-square 6]
      [render-square 7]
      [render-square 8]]])) 

(defn game []
  [:div {:className "game"}
   [:div {:className "game-board"}
    [board]]
   [:div {:className "game-info"}
    [:div ;; status
     ]
    [:ol ;; TODO
     ]]])

(defn init []
  (rdom/render [game] (js/document.getElementById "app")))
