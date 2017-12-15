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
	(if (atom? expr)
		(if (constant? expr)
			(constant (not (second expr)))
			(my-not expr))
		(if (my-not? expr)
			(apply concat (rest expr))
			(if (my-and? expr)
				(cons :or (map move-negation (map my-not (rest expr))))
				(cons :and (map move-negation (map my-not (rest expr))))))))
				
(defn move-negation [expr]
	(if (my-not? expr)
		;then
		(de-morgan (apply concat (rest expr)))
		;else
		(if (atom? expr)
			expr
			(cons (get-expr-arg expr 0) (map move-negation (rest expr)))))) 

(defn open-ands [expr]
	;(println "OPEN")
	;(println expr)
	(reduce
		(fn [res el]
			(if (my-and? el)
				(concat res (rest el))
				(concat res (list el))))
		(list :and)
		(only-args expr)))
			
(defn open-ors [expr]
	(reduce
		(fn [res el]
			(if (my-or? el)
				(concat res (rest el))
				(concat res (list el))))
		(list :or)
		(only-args expr)))
	
(defn distribution [expr1 expr2]
	;(println "DIST")
	;(println expr1)
	;(println expr2)
	(if (and (my-and? expr1) (my-and? expr2)) ; unite :and :and
		(list (concat expr1 (rest expr2)))
		(if (or (my-and? expr1) (my-and? expr2)) ; unite :and :or || :and :atom
			(if (my-and? expr1)
				(cons :or (map 
					(fn [el]
						(open-ands (concat expr1 (list el))))
					(only-args expr2)))
				(cons :or (map
					(fn [el]
						(open-ands (concat expr2 (list el))))
					(only-args expr1))))
			(cons :or (mapcat ;other variants
				(fn [el1]
					(map 
						(fn [el2] 
							(open-ands (list :and el1 el2)))
					(only-args expr2)))
				(only-args expr1))))))

(defn normalize [expr]
	(if (or (atom? expr) (my-not? expr))
		expr
		(let [nargs (map normalize (only-args expr))]
			(if (my-or? expr)
				(open-ors (cons :or nargs))
				(reduce distribution nargs)))))

(defn make-one [expr]
	(if (empty? (rest (rest expr)))
		(apply concat (rest expr))
		expr))
				
(defn make-dnf [expr]
	(make-one
		(normalize
			(move-negation 
				(delete-impls expr)))))
	