$a = ["a", "b", "c"]
$max_sz = 3

def f(ar, n)
  t = []
  ar.each { |s| t = t + $a.map{ |i| s + i }.reject{|i| i[i.length - 1] == i[i.length - 2]} }
  if (n < $max_sz - 1)
    return f(t, n + 1)
  else
    return t
  end
end

b = $a;
puts f(b, 1)
