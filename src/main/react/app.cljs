(ns react.app
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

(defn square [props]
  [:button
   {:class-name "square"
    :on-click #((props :on-click))}
   (props :value)])

(defn calculate-winner [squares]
  (let [rows (partition 3 (range 9))
        columns (partition 3 (apply interleave rows))               
        lines (concat rows columns '((0 4 8) (2 4 6)))]
    (->> (map
          (fn [l] (map #(nth squares %) l))
          lines)
         (map #(if (apply = %) (first %) nil))
         (some identity))))

;; https://github.com/reagent-project/reagent-cookbook/tree/master/basics/component-level-state
(defn board
  [props]
  (let [render-square (fn [i]
                        [square {:value (nth (props :squares) i)
                                 :on-click #((props :on-click) i)}])]
    [:div
     [:div {:class-name "board-row"}
      [render-square 0]
      [render-square 1]
      [render-square 2]]
     [:div {:class-name "board-row"}
      [render-square 3]
      [render-square 4]
      [render-square 5]]
     [:div {:class-name "board-row"}
      [render-square 6]
      [render-square 7]
      [render-square 8]]]))

(defn game
  ([] (fn [] [game nil]))
  ([props]
   (let [history (r/atom [{:squares (vec (repeat 9 nil))}])
         step-number (r/atom 0)
         x-is-next (r/atom true)
         handle-click (fn [i]
                        (let [h  (subvec @history 0 (inc @step-number))
                              squares ((last h) :squares)]
                          (when-not (or (calculate-winner squares) (nth squares i))
                            (do
                              (->> (assoc squares i (if @x-is-next "X" "O"))
                                   (assoc {} :squares)
                                   (conj h)
                                   (reset! history))
                              (reset! step-number (count h))
                              (swap! x-is-next not)))))
         jump-to (fn [step]
                   (do
                     (reset! step-number step)
                     (reset! x-is-next (even? step))))]
     (fn []
       (let [current (nth @history @step-number)
             winner (calculate-winner (current :squares))
             moves (map-indexed
                    (fn [move step]
                      [:li {:key move}
                       [:button {:on-click #(jump-to move)} (if (pos? move) (str "Go to move #" move) "Go to game start")]])
                    @history)
             status (if winner
                      (str "Winner: " winner)
                      (str "Next Player: " (if @x-is-next "X" "O")))]
         [:div {:class-name "game"}
          [:div {:class-name "game-board"}
           [board {:squares (current :squares)
                   :on-click (fn [i] (handle-click i))}]]
          [:div {:class-name "game-info"}
           [:div status]
           [:ol moves]]])))))

(defn init []
  (rdom/render [game] (js/document.getElementById "app")))
