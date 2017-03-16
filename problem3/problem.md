## 문제3 - Simple Lotto Recommend System

심플한 로또 번호 추천 시스템을 만들어봅시다.<br>
역대 로또 당첨번호를 조회하여 자릿수마다 가장 많이 노출된 숫자 또는 가장 노출되지않는 숫자 열거를 출력합니다.<br>
만약, 두번째 자릿수의 가장 많이 노출된 숫자의 노출횟수가 같을 경우 그에 따른 조합들을 모두 출력합니다.

```text
API LINK : http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=

example : http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=644

output : {"bnusNo":8,"firstWinamnt":1831451204,"totSellamnt":61846599000,"returnValue":"success","drwtNo3":17,"drwtNo2":13,"drwtNo1":5,"drwtNo6":36,"drwtNo5":28,"drwtNo4":23,"drwNoDate":"2015-04-04","drwNo":644,"firstPrzwnerCo":8}


```

### 제한

* 가장 간결한 람다형식으로 변경합니다.
* 외부 라이브러리는 사용하지 않습니다.
* 요청한 URL의 json 형식은 파싱을 통해 map 형태로 변환합니다.

### 메인
```kotlin

fun main(args: Array<String>) {

    val simpleLottoRecommnedSystem : simpleLottoRecommendSystem = SimpleRecoomendSystem()
    simpleLottoRecommnedSystem.printRecommendNumber(MAX_EXPOSE) // 최다 노출 숫자형
    simpleLottoRecommnedSystem.printRecommendNumber(MIN_EXPOSE) // 최소 노출 숫자형
}

```


###출력
```text
이번의 행운의 추첨 숫자 ~ [최다 노출 기준]
1. 1, 2, 3, 4, 5, 6, 7 
2. 1, 2, 3, 4, 5, 6, 7 
3. 1, 2, 3, 4, 5, 6, 7 
          .
          .
          .
          
 
이번의 행운의 추첨 숫자 ~ [최소 노출 기준]
1. 1, 2, 3, 4, 5, 6, 7 
2. 1, 2, 3, 4, 5, 6, 7 
3. 1, 2, 3, 4, 5, 6, 7 
         .
         .
         .
 
```

### 선택사항
```text
API 요청 횟수가 많아 속도가 느립니다.
이를 개선해보세요.
```
