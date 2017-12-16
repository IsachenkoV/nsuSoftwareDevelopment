(ns lab4.core-test
	(:require [clojure.test :as test :refer :all]
            [lab4.core :refer :all])
	(:use [lab4.atoms])
	(:use [lab4.dnfconversion])
	(:use [lab4.significator]))

(deftest atom-check
	(test/is (and (atom? (constant true)) (atom? (constant false))))
	(test/is (atom? (variable :x))))
	
(deftest vartest
	(test/is (variable? (variable :x)))
	(test/is (= :x (variable-name (variable :x)))))

(deftest consttest
	(test/is (constant? (constant true)))
	(test/is (= false (constant-value (constant false)))))
  
(deftest args-methods-test
	(test/is (= (list (variable :x)) (only-args (variable :x))))
	(test/is (= (list (constant true)) (only-args (constant true))))
	(test/is (= (list (variable :a) (my-or (variable :x) (variable :y))) (only-args (my-and (variable :a) (my-or (variable :x) (variable :y)))))))

(deftest impl-test
	(test/is 
		(= 
			(my-or (my-not (constant false)) (constant true)) 
			(delete-impls (my-impl (constant false) (constant true)))))
	(test/is
		(=
			(delete-impls (my-impl (variable :x) (variable :y)))
			(my-or (my-not (variable :x)) (variable :y))))
	(test/is
		(=
			(delete-impls (my-or (my-not (variable :x)) (variable :y) (variable :z)))
			(my-or (my-not (variable :x)) (variable :y) (variable :z))))
			
	(test/is
		(=
			(delete-impls 
				(my-or
					(my-and
					  (variable :x)
					  (my-impl
						(my-impl (variable :x) (my-or (variable :x) (variable :y)))
						(my-or (variable :y) (my-impl (variable :x) (variable :y)))))
					(my-impl (variable :x) (constant true))
					(my-impl (constant false) (variable :y))))
      (my-or
        (my-and
          (variable :x) 
		  (my-or
            (my-not (my-or (my-not (variable :x)) (my-or (variable :x) (variable :y))))
            (my-or (variable :y) (my-or (my-not (variable :x)) (variable :y)))))
        (my-or (my-not (variable :x)) (constant true))
        (my-or (my-not (constant false)) (variable :y)))))
	(test/is
		(=
			(delete-impls (my-impl (variable :a) (my-impl (variable :x) (variable :y)))))
			(my-or 
				(my-not (variable :a))
				(my-or (my-not (variable :x)) (variable :y)))))

(deftest demorgan-test
	(test/is
		(=
			(move-negation (my-not (my-or (variable :x) (variable :y))))
			(my-and (my-not (variable :x)) (my-not (variable :y)))))
	(test/is
		(=
			(move-negation (delete-impls (my-impl (constant false) (constant true))))
			(my-or (constant true) (constant true))))
	
	(test/is
		(=
			(move-negation (delete-impls (my-and (variable :x) (my-impl (variable :x) (constant true)))))
			(my-and (variable :x) (my-or (my-not (variable :x)) (constant true)))))
	(test/is
		(=
			(move-negation (my-not (my-and (variable :x) (variable :y))))
			(my-or (my-not (variable :x)) (my-not (variable :y)))))
	(test/is
		(=
			(move-negation (my-not
								(my-or 
									(my-or (my-not (variable :x)) (variable :y))	
									(my-not (my-or (my-not (variable :y)) (variable :z))))))
			(my-and (my-and (variable :x) (my-not (variable :y))) (my-or (my-not (variable :y)) (variable :z)))))
	(test/is
		(=
			(move-negation (my-not (my-not (variable :x))))
			(variable :x)))
	(test/is
		(=
			(my-not (variable :x))
			(move-negation (my-not (variable :x))))))
			
(deftest makednf-test

	;(println "a * (x + y)")
	(test/is
		(=
			(make-dnf (my-and (variable :a) (my-or (variable :x) (variable :y))))
			(my-or (my-and (variable :a) (variable :x)) (my-and (variable :a) (variable :y)))))
			
	;(println "(a + true)*(x + y)")
	(test/is
		(=
			(make-dnf (my-and (my-or (variable :a) (constant true)) (my-or (variable :x) (variable :y))))	
			(my-or (my-and (variable :a) (variable :x)) (my-and (variable :a) (variable :y)) (variable :x) (variable :y))))
			
	;(println "(a + true) + (x + y)")
	(test/is
		(=
			(make-dnf (my-or (my-or (variable :a) (constant true)) (my-or (variable :x) (variable :y))))	
			(constant true)))
			
	;(println "a*b*(x + y + z)")
	(test/is
		(=
			(make-dnf (my-and (my-and (variable :a) (variable :b)) (my-or (variable :x) (variable :y) (variable :z))))
			(my-or (my-and (variable :a) (variable :b) (variable :x)) (my-and (variable :a) (variable :b) (variable :y)) (my-and (variable :a) (variable :b) (variable :z)))))
			
	;(println "a*b*x*y");
	(test/is
		(=
			(make-dnf (my-and (my-and (variable :a) (variable :b)) (my-and (variable :x) (variable :y) (variable :z))))
			(my-and (variable :a) (variable :b) (variable :x) (variable :y) (variable :z))))
			
	;(println "a*b*(z)");
	(test/is
		(=
			(make-dnf (my-and (my-and (variable :a) (variable :b)) (variable :z)))
			(my-and (variable :a) (variable :b) (variable :z))))
			
	(test/is
		(=
			(make-dnf (my-and (variable :a) (my-or (variable :x) (variable :y)) (constant false))))
			(constant false))
			
	(test/is
		(=
			(make-dnf (my-and (constant true) (my-or (variable :x) (constant false))))
			(variable :x))))
			
(deftest significator-test

	(test/is
		(= 
			(assign-values (constant true) {})
			true))

	(test/is
		(= 
			(assign-values (my-not (variable :x)) {:x true})
			false))
			
	(test/is
		(= 
			(assign-values (my-or (my-and (my-not (variable :x)) (constant true)) (variable :a) (constant false) (my-and (constant false) (variable :a))) {:x true :a false})
			false))
			
	(test/is
		(= 
			(assign-values (make-dnf (my-and (my-or (variable :a) (constant true)) (my-or (variable :x) (variable :y)))) {:x true :a false :y false})
			true))
)