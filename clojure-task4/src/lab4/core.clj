(ns lab4.core
	(:use [lab4.atoms])
	(:use [lab4.dnfconversion])
	(:gen-class))

(defn -main
  [& args]
  ;(println (my-impl (variable :x) (constant true)))
  ;(println (delete-impls (my-impl (constant false) (constant true))))
  ;(println (make-dnf (my-impl (constant false) (constant true))))
  ;(println (make-dnf (my-and (variable :x) (my-impl (variable :x) (constant true)))))
  (println (make-dnf (my-not (my-and (variable :x) (variable :y))))))
  