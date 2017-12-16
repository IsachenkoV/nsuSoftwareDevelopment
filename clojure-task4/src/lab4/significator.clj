(ns lab4.significator
	(:use [lab4.atoms]))

(defn assign-values [expr vmap]
	(if (constant? expr)
		(constant-value expr)
		(if (variable? expr)
			(get vmap (variable-name expr))
			(if (my-not? expr)
				(not (assign-values (apply concat (rest expr)) vmap))
				(if (my-and? expr)
					(every? true? (map (fn [el] (assign-values el vmap)) (rest expr)))
					(if (nil? (some true? (map (fn [el] (assign-values el vmap)) (rest expr))))
						false
						true))))))