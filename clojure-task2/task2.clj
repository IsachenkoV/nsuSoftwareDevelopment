(ns .main
    (require [clojure.test :as test]))
	
(def step 0.02)
(defn getPart [f i]
  (* (/ (+ 
          (f (* i step)) 
          (f (* (+ i 1) step))
        ) 
      2)
  step)
)

(def getIntegral
  (memoize
    (fn [f cur_idx]
      (+
        (if 
          (> (- cur_idx 1) 0 )
          (getIntegral f (- cur_idx 1))
          0
        )
        (getPart f (- cur_idx 1))
      )
    )
  )
)

(defn getPrimitive [f x]
  (def all_idx (int (/ x step)))
  (+
    (getIntegral f all_idx)
    ; add tail to integral
    (* (/ (+ 
            (f (* all_idx step)) 
            (f x))
        2) 
     (- x (* all_idx step))
    )
  )
)

(defn foo [x]
  (* 2 x)
)

(time (getPrimitive foo, 3))
(time (getPrimitive foo, 2))
(time (getPrimitive foo, 2))
(time (getPrimitive foo, 1.5))

(test/deftest test1
  (test/is (= (int (Math/floor (getIntegral foo 150))) 9)))

(test/deftest test2
  (test/is (= (int (Math/floor (getPrimitive foo 3))) 9)))

(test/run-tests '.main)
