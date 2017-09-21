alp = ["a", 1, [2, "x"]]
n = 4

def f(cur_ar, alp, n)
  if (n == 0)
    return cur_ar
  else
    return f(cur_ar.inject([]) { |tmp, s| tmp + alp.map{ |i| s + Array.new(1, i) }.reject{|i| i[-1] == i[-2]} }, alp, n - 1)
  end
end

b = Array.new(1, [])
f(b, alp, n).each { |tr| p tr }

(1..n).inject([[]]) { |memo, tmp| memo.flat_map { |s| alp.select { |i| i != s[-1] }.map { |i| s + [i] } } }
