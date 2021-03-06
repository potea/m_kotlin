# m_kotlin

## 문제1 - Simple Reactive System

간단한 Reactive 시스템을 구현해봅시다.

Reactive Programing은, 어떤 값이 변경되면, 그 값을 이용하고 있는 곳으로 상태가 전파됩니다.
잘 이해가 안되면, 엑셀의 수식을 생각하면 됩니다.
셀A와 셀B에 정수값이 있을 때, 셀C는 A+B라는 수식의 결과값이라고 가정해봅시다.
이 때, 셀A의 값을 변경하면, 셀 C의 값은 자동으로 변경됩니다.
그래도 잘 이해가 안가면, 이번 기회에 Reactive의 개념을 공부하세요.

https://en.wikipedia.org/wiki/Reactive_programming

알고리즘 문제가 아니고, 설계 문제입니다. 
알고리즘 문제를 푸는것 처럼 접근하면 안됩니다.

이번 문제의 입력과 출력은 아래와 같습니다.

### 입력
행이 하나밖에 없는 엑셀을 가정하면, 배열처럼 보일것입니다.
각각의 인덱스는 알파벳으로 이루어져있고, A~Z 까지입니다.

1. 이 배열은 **정수** 또는 **두개의 값으로 이루어진 사칙연산 수식**으로 이루어져 있습니다. (+ - * /)
2. 각각의 셀은 쉼표로 구분합니다.
3. 수식은 괄호로 묶여있고, 알파벨으로된 인덱스 또는 정수로 이루어집니다. 
4. 이 배열 다음에는 셀의 값이 어떻게 변경되는지가 순서대로 주어집니다.
5. 순환참조는 허용하지 않습니다. (A번째 인덱스의 수식에는 A가 포함되지 않는다는 뜻)
6. 값을 계산할 수 없다면(0으로 나누는 경우 등) #err을 출력합니다.

예를 들면, 아래와 같을 것입니다.

```text
100, 20, 3023, (A+E), 10, -30, (D+10), 0, 12345
A=>10
C=>(A*E)
E=>100
```

### 출력
셀의 값이 변경될 때 마다, 영향을 받는 셀의 변경값을 출력합니다.

```text
A=>10 : A=>10, D=>20, G=>30
C=>(A*E) : C=>100
E=>100 : E=>100, D=>110, G=>120, C=>1000
```

### 주의사항
* 기존의 Reactive 라이브러리(Rx등등)을 사용하면 반칙입니다.
* Reactive Programing의 모든 요소를 구현해낼 필요는 없고, Propagation(전파)만 제대로 되면 됩니다.

### kwi선임님을 위한 특별 문제
1. 입력과 출력을 안드로이드 GUI로 구현합니다.
2. Column이 하나뿐인 스프레드시트를 구현한다고 생각하면됩니다.
3. 세로 RecyclerView로 스크롤 가능하게 만들면 되겠죠? 거기에 선택된 셀의 값을 입력하는 EditText가 있으면 되겠네요.
4. Propagation은 셀에 직접 반영되면 됩니다.

---
## 문제2 - Simple Reactive System Extension
첫 문제인 Simple Reactive System을 통해 서로 비슷하게 구현된 부분이 있었습니다.
그것은 바로 **Cell**이라는 클래스였습니다.
**셀**에서 **표현식**을 파싱하여 이것이 숫자인지, 수식인지를 구분하여 값 계산에 활용하였습니다.
이제 수식 뿐만이 아니라 실제 엑셀 처럼 =Fun() 과 같이 여러 함수들을 제공하도록 확장한다고 가정해보죠.
지원되는 함수가 추가될 때마다 이전처럼 **isExpression**과 같이 특정 수식임을 구분하기위한 변수들을 계속 생성한다고 생각해보세요.
조건문이 난무하고 코드가 지저분해 보여 수정하고 싶어질 겁니다.
자 그럼 여기서 두번째 문제 입니다.

###Simple Reactive System 수식 확장
정수와 사칙연산만 제공하던 Simple Reactive System에 간단한 함수를 지원하도록 수식계산 부분을 확장합니다.
각 **수식 타입**에 알맞는 클래스들을 구현하여 위와 같이 지저분해질 부분들을 깔끔하게 정리해 봅시다. 

###입력 & 출력
* 기존 정수와 사칙연산에 대한 동작은 문제1과 동일합니다.
* 단, 수식 입력은 '='을 시작하는 것으로 변경합니다.(함수 입력과 동일하게 맞추도록 수정)
* 수식 입력대해 괄호 규칙도 편의상 제거하셔도 됩니다. =(A+E) 또는 =A+E 는 같은것으로 봄.
* 추가되는 함수에 대한 입력 형식은 다음과 같습니다.
* **=SUM(A:Z)**
함수의 시작은 '='로 시작하고 그뒤에 '함수명'과 '범위'가 입력됩니다.
* 지원할 함수는 SUM, AVERAGE, MAX, MIN 네가지 입니다.
범위내 값들중 비어있는 셀은 값을 무시하고 계산합니다.
(실제 엑셀처럼 AVERAGE의 경우 나누는 수에 카운팅을 포함하지 않음을 말합니다.)
```text
=SUM(A:Z) : A부터 Z셀 까지의 합을 구함
=AVERAGE(A:Z) : A부터 Z셀 까지의 평균을 구함
=MAX(A:Z) : A부터 Z셀 값중 가장 큰 값을 구함
=MIN(A:Z) : A부터 Z셀 값중 가장 작은 값을 구함
```
```
범위 제한 : A부터 Z셀 까지 입력 가능
(A:D), (C:Y), (E:F)와 같이 범위의 최소는 2셀이상이며 (A:A)와 같은 케이스는 지원되지 않음. 
(C:A)처럼 역으로 지정된 범위는 입력되지 않는다 가정
```

### 선택사항
문제1와 연결된 문제이므로, 문제1의 미완성에 따른 패널티가 존재합니다.
문제1을 마저 완성하고 구현해 보는것을 권장합니다만 상황을 고려하여 다음을 선택하실 수 있습니다.
가급적이면 문제1의 코드를 활용해 보는게 입출력 테스트 확인에 좋겠죠~?
* 풀었던 문제1 코드에 추가 구현
* 문제2에 해당되는 수식 확장에 대한 부분만 구현

---
## 문제3 - 로또 수열

로또 번호들을 보던도중 재밌는 수열이 생각났습니다.<br>
매회 당첨번호에 당첨자들의 수를 곱한 누적값을 기준으로 정렬합니다.<br>
단, 당첨자가 0일 경우 1을 더해줍니다.<br>
이를 기준으로 통계값이 가장 높은 수열과 가장 낮은 수열을 구합니다.<br>
만약, 7번째로 등장한 숫자의 통계값이 8-10번째와 같다면 모두 출력합니다.

```text
예를 들어 당첨번호가 1,2,3,4,5,6,7 일때 1등 수가 8이라면,
lottoCnt[1]+=8
lottoCnt[2]+=8
lottoCnt[3]+=8
lottoCnt[4]+=8
lottoCnt[5]+=8
lottoCnt[6]+=8
lottoCnt[7]+=8

1등수가 0 이라면,
lottoCnt[1]++
lottoCnt[2]++
lottoCnt[3]++
lottoCnt[4]++
lottoCnt[5]++
lottoCnt[6]++
lottoCnt[7]++

API 문서 
API LINK : http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=

example : http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=644

output : {"bnusNo":8,"firstWinamnt":1831451204,"totSellamnt":61846599000,"returnValue":"success","drwtNo3":17,"drwtNo2":13,"drwtNo1":5,"drwtNo6":36,"drwtNo5":28,"drwtNo4":23,"drwNoDate":"2015-04-04","drwNo":644,"firstPrzwnerCo":8}
```

### 제한

* 가장 간결한 람다형식으로 변경합니다.
* 외부 라이브러리는 사용하지 않습니다.
* flatmap을 사용합니다.
* 요청한 URL의 json 형식은 파싱을 통해 map 형태로 변환합니다.

### 메인
```kotlin

fun main(args: Array<String>) {

    val lottoNumberSequence : lottoNumberSequence = LottoNumberSequence()
    lottoNumberSequence print MAX_EXPOSE // 최다 노출 숫자형
    lottoNumberSequence print MIN_EXPOSE // 최소 노출 숫자형
}

```


### 출력
```text
로또 수열 출력 ~ [최다 노출 기준]
1. 1, 2, 3, 4, 5, 6, 7 
          .
          .
          .
          
=> 마지막 자릿수의 숫자 7과 숫자 10 노출횟수가 200번으로 같을경우 출력
1. 1, 2, 3, 4, 5, 6, 7 
2. 1, 2, 3, 4, 5, 6, 10
          
 
로또 수열 출력 ~ [최소 노출 기준]
1. 15, 16, 17, 18, 19, 20, 21 
         .
         .
         .
 
=> 마지막 자릿수의 숫자 21과 숫자 22 노출횟수가 63번으로 같을경우 출력
1. 15, 16, 17, 18, 19, 20, 21 
2. 15, 16, 17, 18, 19, 20, 22
         .
         .
         .

```

### 선택사항
```text
API 요청 횟수가 많아 속도가 느립니다.
이를 개선해보세요.
```

---
## 문제4 - 함수형 연습
함수형 자료형과 함수형 프로그래밍 연습을 합시다.

### 문제4-1 로또
로또 번호를 추출하는 함수를 만듭니다.
N개의 서로 다른 랜덤 숫자를 1~M 범위에서 추출합니다.
```
lotto(4, 10) // 1~10까지의 숫자중에서 4개의 랜덤 숫자 추출
1, 2, 3, 4
```

### 문제4-2
정렬 함수를 만듭니다.
이 정렬 함수는 리스트 내부의 서브리스트의 길이를 기준으로 정렬합니다.
(길이가 같으면 서브리스트의 문자 오름차순으로 정렬합니다.)
```
입력값=> [['a', 'b', 'c'], ['d', 'e'],['f', 'g', 'h'], ['d', 'e'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o']]
정렬하면 => [['o'], ['d', 'e'], ['d', 'e'], ['m', 'n'], ['a', 'b', 'c'], ['f', 'g', 'h'], ['i', 'j', 'k', 'l']]
```

### 문제4-3
소수를 찾는 함수를 만듭니다.
이 함수는 주어진 범위 내에 존재하는 소수의 리스트를 리턴합니다.
```
시작 7, 끝 31
출력 => [7, 11, 13, 17, 19, 23, 29, 31]
```

### 문제4-4
N bit Gray code를 생성하는 함수를 만듭니다.
https://en.wikipedia.org/wiki/Gray_code
(그레이 코드는 한 비트씩 바뀝니다.)
```
GrayCode(1) = ["0", "1"]
GrayCode(2) = ["00", "01", "11", "10"]
GrayCode(3) = ["000", "001", "011", "010", "110", "111", "101", "100"]
GrayCode(N) = ??
```

---
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

---
## 문제6 - Guess my number
이번에는 인공지능 게임을 하나 만들어봅시다. 게임의 규칙은 단순합니다.

1. 플레이어가 1~100사이의 정수를 입력합니다.
2. 그러면 컴퓨터는 플레이어가 입력한 숫자를 맞춥니다.

### 입력
1보다 크거나 같고, 100보다 작거나 같은 정수

```text
ex1) 90
ex2) 71
```

### 출력
딱 한번에 사용자의 입력값을 맞출 필요는 없습니다. 여러번 시도해서 사용자의 입력값을 드디어 찾게되면 !를 함께 출력합니다.

```text
ex1) 100 99 98 97 96 95 94 93 92 91 90!
ex2) 50 75 63 69 72 70 71!
```

### 규칙
1. 어떤 방법으로 입력값을 찾아도 상관 없지만, 빨리 찾을 수록 좋습니다.
2. 단, **최소한의 코드**로 구현해야 합니다. 같은 성능이라면, 코드가 제일 짧은 사람이 1등!

---
## 문제7 - Josephus permutation

원형으로 n명의 사람들이 동그랗게 둘러 앉아 서바이벌 게임을 합니다.
서바이벌 게임이 규칙은 다음과 같습니다.
- 첫번째 사람부터 시작해 k번째의 사람을 총으로 쏘아 제거 합니다.
- 제거된 사람의 다음을 기준으로 다시 k번째 사람을 쏘아 제거 합니다.
- 이렇게 마지막 사람이 제거될 때까지 진행합니다.

이러한 규칙을 진행하면서 제거되는 사람 순서를 순열로 만든것이 조세푸스(요세푸스)순열 이라고 합니다.
https://ko.wikipedia.org/wiki/%EC%9A%94%EC%84%B8%ED%91%B8%EC%8A%A4_%EB%AC%B8%EC%A0%9C


### 조세푸스 순열
(n, k) 조세푸스 순열은 n명의 사람이 있고 k번째 마다의 사람을 제거해 나가는 순서입니다.
예를들어 (7, 3) 조세푸스 순열은 [3,6,2,7,5,1,4] 이며.
마지막에 제거되는 사람은 4 입니다.

``` 예시
- 아래는 (7, 3) 조세푸스 순열의 생성 예시 입니다.

1,2,3,4,5,6,7
1부터 시작, 세번째인 3 제거
1,2,4,5,6,7
4부터 시작, 3번째인 6 제거
1,2,4,5,7
7부터 시작, 3번째인 2 제거
1,4,5,7
4부터 시작, 3번째인 7 제거
1,4,5
1부터 시작, 3번째인 5 제거
1,4
1부터 시작, 3번째인 1 제거
4
마지막 4 제거
제거된 순서에 의한 순열은 [3,6,2,7,5,1,4] 입니다.
```

### 문제
그럼 여기서 문제입니다.
조세푸스 순열 순서에 따라 어짜피 죽는거 내가 몇번째에 죽는지가 궁금한 사람이 있습니다.
이러한 사람을 위해 특정 타겟(t)이 몇번째에 제거되는지 알수있는 코드를 작성해 봅시다.

```
Josephus(n, k, t) : (n, k) 조세푸스 순열에 대해 타겟(t)이 몇번째에 죽는지 순서를 알려줍니다. 
출력 : 위의 예시에서 (7, 3, 2) 는 3입니다.
```

코드의 양은 최소한의 코드만 사용하도록 합니다.
문제의 단순화를 위해 3 < n, k, t <= 1000 으로 제한 합니다.

---
## 문제8 - Android App 

매주 로또를 사러가는 퀴선은 번호 선택이 고민입니다.
퀴선을 잘 챙겨주는 후임은 [문제3][1]을 통해서 로또 수열을 만들게 했네요.
매번 코틀린 코드를 돌려서 숫자를 뽑은 후에 로또를 사러갈수는 없습니다.
생각났을때 로또 수열을 얻어야 합니다.
<br>
이제 퀴선을 위해서 로또 번호 앱을 만들어 줍시다.
* 필수
    * 마지막 당첨 번호가 나타나야 합니다.
    * [문제3][1]의 로또 수열 기능이 있어야 합니다.
    * [문제4-1][2]의 랜덤 숫자 기능이 있어야 합니다.
    * 퀴선의 데이터 비용을 아껴줄 방법이 있어야 합니다.
* 권장
    * 퀴선이 얼마나 앱을 사용했는지 확인하고 싶습니다.
    * 토요일이 되기 전에 로또를 사도록 알려주고 싶습니다.


[1]: https://github.com/dry-p/m_kotlin/blob/master/problem3/problem.md
[2]: https://github.com/dry-p/m_kotlin/blob/master/problem4/problem.md