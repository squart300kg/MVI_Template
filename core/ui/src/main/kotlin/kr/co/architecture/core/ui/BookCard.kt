package kr.co.architecture.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.core.ui.util.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString
import kr.co.architecture.test.testing.ui.SearchTags
import kr.co.architecture.core.ui.R as coreUiR

data class BookCardUiModel(
  val isbn: String,
  val thumbnail: String,
  val title: UiText,
  val publisher: UiText,
  val authors: UiText,
  val isBookmarked: Boolean,
  val price: UiText,
  val publishDate: UiText,
) {

  companion object {
    fun mapperToUi(
      searchedBooks: SearchedBooks,
      dateTextFormatter: DateTextFormatter,
      moneyTextFormatter: MoneyTextFormatter
    ): ImmutableList<BookCardUiModel> {
      return mapperToUi(
        book = searchedBooks.books,
        dateTextFormatter = dateTextFormatter,
        moneyTextFormatter = moneyTextFormatter
      ).toImmutableList()
    }

    fun mapperToUi(
      book: List<Book>,
      dateTextFormatter: DateTextFormatter,
      moneyTextFormatter: MoneyTextFormatter
    ): ImmutableList<BookCardUiModel> {
      return book.map {
        BookCardUiModel(
          isbn = it.isbn,
          thumbnail = it.thumbnail,
          title = UiText.DynamicString(it.title),
          publisher = UiText.StringResource(
            resId = coreUiR.string.publisher,
            args = listOf(it.publisher)
          ),
          authors = UiText.StringResource(
            resId = coreUiR.string.authors,
            args = listOf(it.authors.joinToString(", "))
          ),
          isBookmarked = it.isBookmarked,
          price = run {
            val displayedPrice = when (val price = it.price) {
              is Price.Discount -> price.discounted
              is Price.Origin -> price.origin
            }
            UiText.StringResource(
              resId = coreUiR.string.won,
              args = listOf(moneyTextFormatter(displayedPrice))
            )
            },
          publishDate = UiText.StringResource(
            resId = coreUiR.string.publishDate,
            args = listOf(dateTextFormatter(it.dateTime))
          )
        )
      }
        .toImmutableList()
    }
  }
}

@Composable
fun BookCard(
  modifier: Modifier = Modifier,
  uiModel: BookCardUiModel,
  onClickedBookmark: (isbn: String, isBookmarked: Boolean) -> Unit = {_,_->},
  onClickedItem: (isbn: String) -> Unit = {}
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag(SearchTags.bookCard(uiModel.isbn)),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    onClick = { onClickedItem(uiModel.isbn) }
  ) {
    Row(
      modifier = Modifier.padding(10.dp)
    ) {
      CoilAsyncImage(
        modifier = Modifier.weight(0.3f),
        url = uiModel.thumbnail,
      )

      Column(
        modifier = Modifier
          .weight(0.5f)
          .align(Alignment.CenterVertically),
      ) {
        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = stringResource(id = coreUiR.string.book),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.title.asString(),
          style = TextStyle(
            fontWeight = FontWeight.Bold
          ),
          maxLine = 2
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.publisher.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.authors.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.publishDate.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        Text(
          modifier = Modifier.padding(4.dp),
          text = uiModel.price.asString(),
          fontWeight = FontWeight.Bold
        )
      }

      Image(
        modifier = Modifier
          .wrapContentWidth(Alignment.End)
          .weight(0.1f)
          .testTag(SearchTags.bookmark(uiModel.isbn))
          .baseClickable { onClickedBookmark(uiModel.isbn, uiModel.isBookmarked) },
        imageVector =
          if (uiModel.isBookmarked) Icons.Filled.Favorite
          else Icons.Outlined.FavoriteBorder,
        contentDescription = null
      )
    }
  }
}