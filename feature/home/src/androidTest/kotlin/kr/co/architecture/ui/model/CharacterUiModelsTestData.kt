package kr.co.architecture.ui.model

import kr.co.architecture.core.model.GeneralNewsListUiModel

val newsListUiModelsTestData = GeneralNewsListUiModel(
    newsList = listOf(
        GeneralNewsListUiModel.News(
            author = "송상윤1",
            thumbnail = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available/portrait_xlarge.jpg",
            title = "놀라운 기사1",
            publishedAt = "2024/01/01",
            newspaperCompany = "동아일보1",
        ),
        GeneralNewsListUiModel.News(
            author = "송상윤2",
            thumbnail = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available/portrait_xlarge.jpg",
            title = "놀라운 기사2",
            publishedAt = "2024/01/02",
            newspaperCompany = "동아일보2",
        ),
        GeneralNewsListUiModel.News(
            author = "송상윤3",
            thumbnail = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available/portrait_xlarge.jpg",
            title = "놀라운 기사3",
            publishedAt = "2024/01/03",
            newspaperCompany = "동아일보3",
        ),
    )
)