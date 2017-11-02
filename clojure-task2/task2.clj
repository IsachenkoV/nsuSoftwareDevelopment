(ns .main
    (require [clojure.test :as test]))

(def step 0.005)

(defn getIntegral [f cur_x x]
  (if 
      (< 
         (+ cur_x step) 
         x)
      (+
        (getIntegral f (+ cur_x step) x)
        (* (/ (+ 
                 (f cur_x) 
                 (f (+ cur_x step))
              ) 
            2) 
        step)
      )
      (* (/ (+ 
               (f cur_x) 
               (f x)) 
          2) 
      step)
  )
)

(defn foo [x]
  (* 2 x)
)

(defn getPrimitive [f x]
  (def fm (memoize getIntegral))
  (fm f 0 x)
)

(time (getPrimitive foo, 5))
(time (getPrimitive foo, 1))
(time (getPrimitive foo, 2))

(test/deftest test1
  (test/is (= (int (Math/floor (getIntegral foo 0 5))) 25)))

(test/deftest test2
  (test/is (= (int (Math/floor (getPrimitive foo 5))) 25)))

(test/run-tests '.main)
