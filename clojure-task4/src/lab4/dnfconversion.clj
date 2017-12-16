(ns lab4.dnfconversion
	(:use [lab4.atoms]))
	
(defn delete-impls [expr]
	;(:impl expr1 expr2) = (:or (:not expr1) expr2)
	(if (my-impl? expr)
		(my-or (my-not (delete-impls (get-expr-arg expr 1))) (delete-impls (get-expr-arg expr 2)))
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
	(let [cur_res 
			(if (empty? (rest (rest expr)))
				(apply concat (rest expr))
				expr)]
			(if (empty? (rest cur_res))
				(constant false)
				cur_res)))
				
(defn have-false [arglist]
	(some #(= (constant false) %) arglist))

(defn have-true [arglist]
	(some #(= (constant true) %) arglist))
	
(defn simplify-or [expr]
	(if (have-true (rest expr))
		(constant true)
		(make-one
			(concat (list :or) 
				(distinct
				(reduce 
					(fn [res el]
						(if (= (constant false) el)
							(concat res '())
							(concat res (list el))))
					'()
					(rest expr)))))))

(defn remove-true [expr]
	(let [nexpr 
		(reduce
			(fn [res el]
				(if (= (constant true) el)
					(concat res '())
					(concat res (list el))))
			(list :and)
			(rest expr))]
		(if (empty? (rest nexpr))
			(list (constant true))
			(if (empty? (rest (rest nexpr)))
				(rest nexpr)
				(list nexpr)))))
	
(defn simplify [expr]
	(simplify-or
		(reduce
			(fn [res el]
				(if (my-and? el)
					(if (have-false (rest el))
						(concat res '())
						(concat res (remove-true el)))
					(concat res (list el))))
			(list :or)
			(rest expr))))
			
(defn my-distinct-and [expr]
	(reduce 
		(fn [res el]
			(if (or (atom? el) (my-not? el))
				(concat res (list el))
				(concat res (list (concat (list :and) (distinct (rest el)))))))
		(list :or)
		(rest expr)))
		
(defn make-dnf [expr]
	(simplify
		(my-distinct-and
			(normalize
				(move-negation 
					(delete-impls expr))))))