## 문제3 - 로또 수열

로또 번호들을 보던도중 재밌는 수열이 생각났습니다.<br>
각 번호들이 나온 누적 숫자와 매 회 1등 당첨자들의 수를 곱한 통계값을 구합니다.<br>
이를 기준으로 통계값이 가장 높은 수열과 가장 낮은 수열을 구합니다.<br>
만약, 7번째로 등장한 숫자의 통계값이 8-10번째와 같다면 모두 출력합니다.

```text
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
