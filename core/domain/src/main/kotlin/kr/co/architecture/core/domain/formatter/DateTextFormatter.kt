package kr.co.architecture.core.domain.formatter

import java.util.Date

/**
 * 날짜를 사용자에게 표시하기 좋은 문자열로 변환하는 포맷터.
 *
 * 예:
 * ```
 * val formatter: DateTextFormatter = KoreanDateTextFormatter()
 * formatter(Date())  // "25년 8월 27일 15시 23분 22초"
 * ```
 *
 * 보통 UI 계층에서 날짜를 표시할 때 사용하며,
 * 로캘과 포맷 패턴("yy년 M월 d일 H시 m분 s초") 등에 따라 다르게 구현될 수 있다.
 */
interface DateTextFormatter {

    /**
     * 주어진 [Date] 객체를 포맷팅하여 문자열로 반환한다.
     *
     * @param raw 포맷팅할 [String]타입의 날짜
     * @return 포맷팅된 문자열 표현 (예: "25년 8월 27일 15시 23분 22초")
     */
    operator fun invoke(raw: String): String
}