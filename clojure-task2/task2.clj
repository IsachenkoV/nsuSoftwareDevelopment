(ns .main
    (require [clojure.test :as test]))

(defn getIntegral [f x segs]
  ( 
    (fn [args]
        (reduce + 
          (for [[i j] (partition 2 1 args)] (* (/ (+ (f i) (f j)) 2) (/ x segs)))
        )
    )
    (take (+ 1 segs) (iterate (partial + (/ x segs)) 0))
  )
)

(defn foo [x]
  (* 2 x)
)

(print (getIntegral foo, 7, 4))