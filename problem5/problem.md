## 문제5 - 개미수열
개미 수열(한국식 Look and Say)
(참고 - https://ko.wikipedia.org/wiki/%EC%9D%BD%EA%B3%A0_%EB%A7%90%ED%95%98%EA%B8%B0_%EC%88%98%EC%97%B4)

### 문제5-1
람다식으로 풀기(n = 10)

### 문제5-2
코루틴을 이용해서 풀기(n = 100)
코루틴으로 풀면 ant(1000)를 풀 수 있다고 한다.
```
fun main(args: Array<String>) {
    ant(10)
}

output
11221131132111311231
```
