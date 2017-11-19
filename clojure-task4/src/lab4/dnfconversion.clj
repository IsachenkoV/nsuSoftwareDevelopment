(ns lab4.dnfconversion
	(:use [lab4.atoms]))
	
(defn delete-impls [expr]
	;(:impl expr1 expr2) = (:or (:not expr1) expr2)
	(if (my-impl? expr)
		(my-or (my-not (get-expr-arg expr 1)) (get-expr-arg expr 2))
		(if (atom? expr)
			expr
			(cons (get-expr-arg expr 0) (map delete-impls (rest expr))))))

(defn move-negation [expr])
			
(defn de-morgan [expr]
	(print "START de ")
	(print expr)
	(println " ")
	(if (atom? expr)
		(if (constant? expr)
			(constant (not (second expr)))
			(my-not expr))
		(if (my-not? expr)
			(rest expr)
			(if (my-and? expr)
				(my-or  (map move-negation (map my-not (rest expr))))
				(my-and (map move-negation (map my-not (rest expr))))))))
				
(defn move-negation [expr]
	(print "START mv ")
	(print expr)
	(println " ")
	(if (my-not? expr)
		;then
		(de-morgan (apply concat (rest expr)))
		;else
		(if (atom? expr)
			expr
			(cons (get-expr-arg expr 0) (map move-negation (rest expr)))))) 

(defn make-dnf [expr]
	(move-negation 
		(delete-impls expr)))