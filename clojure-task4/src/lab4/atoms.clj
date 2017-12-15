(ns lab4.atoms)

;constant
(defn constant [val]
	{:pre [(or (true? val) (false? val))]}
	(list :const val))
  
(defn constant? [expr]
	(= (first expr) :const))

(defn constant-value [expr]
	(second expr))

	
;variables
(defn variable [name]
	{:pre [(keyword? name)]}
	(list :var name))

(defn variable-name [expr]
	(second expr))

(defn variable? [expr]
	(= (first expr) :var))

	
;operations
;not
(defn my-not [expr]
	(list :not expr))

(defn my-not? [expr]
	(= (first expr) :not))


;and
(defn my-and [& expr]
	(cons :and expr))
	
(defn my-and? [expr]
	(= (first expr) :and))

	
;or
(defn my-or [& expr]
	(cons :or expr))
	
(defn my-or? [expr]
	(= (first expr) :or))

	
;implication
(defn my-impl [expr1 expr2]
	(list :impl expr1 expr2))
	
(defn my-impl? [expr]
	(= (first expr) :impl))
	

(defn get-expr-arg [expr i]
	(nth expr i))

(defn atom? [expr]
	(or (constant? expr) (variable? expr)))
	
(defn only-args [expr]
	(if (or (my-or? expr) (my-and? expr))
		(rest expr)
		(list expr)))