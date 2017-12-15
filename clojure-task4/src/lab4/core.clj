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
  ;(println (make-dnf (my-not (my-and (variable :x) (variable :y)))))
  (println (make-dnf (my-not (my-or (my-or (my-not (variable :x)) (variable :y)) (my-not (my-or (my-not (variable :y)) (variable :z)))))))
  ;(println (only-args (variable :x)))
  ;(println (only-args (constant true)))
  ;(println (make-dnf (my-impl (constant false) (constant true))))
  ;(println (only-args (make-dnf (my-impl (constant false) (constant true)))))
  ;(println (only-args (my-and (variable :a) (my-or (variable :x) (variable :y)))))
  ;(println "a * (x + y)")
  ;(println (distribution (variable :a) (my-or (variable :x) (variable :y))))
  ;(println "(a + true)*(x + y)")
  ;(println (distribution (my-or (variable :a) (constant true)) (my-or (variable :x) (variable :y))))
  ;(println "a*b*(x + y + z)")
  ;(println (distribution (my-and (variable :a) (variable :b)) (my-or (variable :x) (variable :y) (variable :z))))
  ;(println "a*b*x*y");
  ;(println (distribution (my-and (variable :a) (variable :b)) (my-and (variable :x) (variable :y) (variable :z))))
  ;(println "a*b*(z)");
  ;(println (distribution (my-and (variable :a) (variable :b)) (variable :z)))
  ;(println (normalize (my-and (variable :a) (my-or (variable :x) (variable :y)))))
  ;(println (make-dnf (my-and (variable :a) (my-or (variable :x) (variable :y)) (constant true))))
  ;(println (make-dnf (my-and (variable :a) (my-and (variable :x) (variable :y)))))
  )
  