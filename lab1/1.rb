a = ["a", "b", "c"]
x = 3

def f(ar, n, a)
  t = []
  ar.each { |s| t = t + a.map{ |i| s + i }.reject{|i| i[i.length - 1] == i[i.length - 2]} }
  if (n > 0)
    return f(t, n - 1, a)
  else
    return t
  end
end

b = a;
puts f(b, x - 1, a)

