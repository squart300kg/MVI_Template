package kr.co.architecture.core.common.formatter

/**
 * 금액을 사용자에게 표시하기 좋은 문자열로 변환하는 포맷터.
 *
 * 예:
 * ```
 * val formatter: MoneyTextFormatter = KoreanMoneyTextFormatter()
 * formatter(1000)        // "1,000"
 * formatter(123123123)   // "123,123,123"
 * ```
 *
 * 보통 UI 계층에서 금액을 표시할 때 사용하며,
 * 숫자 단위 구분 기호(,) 등을 로캘에 맞게 적용한다.
 */
interface MoneyTextFormatter {

  /**
   * 주어진 정수 금액을 포맷팅하여 문자열로 반환한다.
   *
   * @param amount 포맷팅할 금액 (정수)
   * @return 단위 구분 기호가 적용된 문자열 표현
   */
  operator fun invoke(amount: Int): String
}
