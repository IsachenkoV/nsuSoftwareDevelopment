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

(def naturals
  (lazy-seq
    (cons 0 (map inc naturals))))

(def getIntegral
  (fn [f idx]
    (nth
      (map 
        (fn [el] (first el))
        (iterate
          (fn [sum_and_seq]
            (list (+  (first sum_and_seq) 
                      (first (second sum_and_seq))
                  )
                  (rest (second sum_and_seq)))
          )
          (list 0 (map (fn [el] (getPart f el)) naturals))
        )
      )
      idx
    )
  )
)

(defn foo [x]
  (* 2 x)
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

(time (getPrimitive foo, 3))
(time (getPrimitive foo, 2))
(time (getPrimitive foo, 2))
(time (getPrimitive foo, 1.5))

(test/deftest test1
  (test/is (= (int (Math/floor (getIntegral foo 150))) 9)))

(test/deftest test2
  (test/is (= (int (Math/floor (getPrimitive foo 3))) 9)))

(test/run-tests '.main)