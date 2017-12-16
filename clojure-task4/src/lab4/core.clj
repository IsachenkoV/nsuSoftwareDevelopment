(ns lab4.core
	(:use [lab4.atoms])
	(:use [lab4.dnfconversion])
	(:use [lab4.significator])
	(:gen-class))

(defn -main
  [& args]
  ;(println (make-dnf (my-and (my-or (variable :a) (constant true)) (my-or (variable :x) (variable :y)))))
  ;(println (assign-values (my-not (variable :x)) {:x true}))
  (println (assign-values (my-or (my-and (my-not (variable :x)) (constant true)) (variable :a) (constant false) (my-and (constant false) (variable :a))) {:x true :a false}))
  )
  