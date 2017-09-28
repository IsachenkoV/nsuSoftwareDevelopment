class Array
  def threading (&func)
    thread_count = 4
    if thread_count > size then
      thread_count = size
    end
    part_size = size / thread_count
    
    (1..thread_count).map() do |t_ind| 
      _start = (t_ind - 1) * part_size 
      _end = t_ind == thread_count ? size : t_ind * part_size
      part = self[_start..._end]
      Thread.new(part) { Thread.current[:out] = yield part }
    end.flat_map { |thread| thread.join[:out] }
  end
  
  def my_all? (&block)
    result = threading { |part| part.all?(&block) }.all?
  end
  
  def my_any? (&block)
    result = threading { |part| part.any?(&block) }.any?
  end
  
  def my_map (&block)
    result = threading { |part| part.map(&block) }
  end
  
  def my_select (&block)
    result = threading { |part| part.select(&block) }
  end
end

a = ["asd", "aaa", "aa", "asdasdad", "asdaaa", "asddd"];
puts a.my_all? {|el| el.length > 3 }
puts a.my_any? {|el| el.length > 3 }
p a.my_map {|el| el + "3" }
p a.my_select {|el| el.length > 3 }
