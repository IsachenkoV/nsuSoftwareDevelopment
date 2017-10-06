strs = File.readlines('details.csv', encoding: 'utf-8')

count_str = "Количество: "
cost_str = "Стоимость: "
phone_str = "Номер телефона: "

cost_regexp = '.*;(?<cost>\d+\.?\d*)'
phone_number_regexp = '(?<phone>\+\d{11})'
date_regexp = '(?<date>\d{2}\.\d{2}\.\d{2};)'

queries = [
    ["SMS: ", ".*(SMS|СМС)#{cost_regexp}"],
    ["MMS: ", ".*MMS#{cost_regexp}"],
    ["Звонки: ", ".*вызовы#{cost_regexp}"],
    ["Интернет: ", ".*Передача данных#{cost_regexp}"],
    ["Доп. услуги: ", ".*(Дополнительные|Прочие)#{cost_regexp}"]
]
puts phone_str
strs.each() {|s| x = s.match(phone_number_regexp); if x != nil then puts x[:phone]; break; end }

queries.map() { |el| strs.reduce([el[0], 0, 0.0]) do |memo, s|
  x = s.match(el[1])
  if x != nil then
    memo[1] = memo[1] + 1
    memo[2] = memo[2] + x[:cost].to_f
  end
  memo
end
}.each { |el| puts el[0] + "\n" + count_str + el[1].to_i.to_s + "\t" + cost_str + el[2].to_s }

quants = {}
#strs.each() {|s| x = s.match(date_regexp); if x != nil then quants.key?(x[:date].to_s) ? quants[x[:date].to_s] += 1 : quants[x[:date].to_s] = 1 end }
#puts quants.key(quants.values.max)
quants = strs.map() { |s| x = s.match(date_regexp).to_s }.reject{|el| el == "" }.group_by {|val| val}
puts quants.max_by {|key, val| val.length }[0]