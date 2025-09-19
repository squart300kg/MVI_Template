package kr.co.architecture.feature.detail

import android.app.Activity
import android.os.Bundle

class DetailActivity: Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    val uri = intent?.data
    val id = uri?.getQueryParameter("id")

    println("detailLog : $id")
    if (id.isNullOrBlank()) {
      finish() // 잘못된 호출 방어
      return
    }

    // id로 Repository 조회 후 바인딩 갱신
    // viewModel.load(id)
  }
}