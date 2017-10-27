(ns .main
    (require [clojure.test :as test]))

(defn f [start, alp, n] 
  (nth
    (iterate
      (fn [cur_strs]
        (mapcat
          (fn [el]
              (map 
                (fn [sym] 
                   (cons sym el)
                )
                (remove (fn [sym] (identical? (first el) sym)) alp)
              )
          )
          cur_strs
        )
      )
      start
    )
    n
  )
)

(println 
    (f (list ()) (list "a" 42 "c") 3))

(test/deftest test1
  (test/is (= (f '(()) '("a" "b" "c") 1) '(("a") ("b") ("c")))))

(test/deftest test2
  (test/is (= (f '(()) '("a" "b" "c") 0) '(()))))

(test/deftest test3
  (test/is (= (f '(()) '() 100) '())))

(test/run-tests '.main)