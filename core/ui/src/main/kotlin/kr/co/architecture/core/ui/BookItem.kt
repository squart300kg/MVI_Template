package kr.co.architecture.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.common.formatter.DateTextFormatter
import kr.co.architecture.core.common.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString
import kr.co.architecture.core.ui.R as coreUiR

data class BookUiModel(
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
    ): ImmutableList<BookUiModel> {
      return searchedBooks.books
        .map { book ->
          BookUiModel(
            isbn = book.isbn,
            thumbnail = book.thumbnail,
            title = UiText.DynamicString(book.title),
            publisher = UiText.StringResource(
              resId = coreUiR.string.publisher,
              args = listOf(book.publisher)
            ),
            authors = UiText.StringResource(
              resId = coreUiR.string.authors,
              args = listOf(
                book.authors
                  .joinToString(", ")
              )
            ),
            isBookmarked = book.isBookmarked,
            price = run {
              val displayedPrice = when (val price = book.price) {
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
              args = listOf(dateTextFormatter(book.dateTime))
            )
          )
        }
        .toImmutableList()
    }
  }
}

@Composable
fun BookItem(
  modifier: Modifier = Modifier,
  bookUiModel: BookUiModel,
  onClickedBookmark: (BookUiModel) -> Unit = {},
  onClickedItem: (BookUiModel) -> Unit = {}
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Max)
      .baseClickable { onClickedItem(bookUiModel) },
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier.padding(10.dp)
    ) {
      CoilAsyncImage(
        modifier = Modifier.weight(0.3f),
        url = bookUiModel.thumbnail,
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
          inputText = bookUiModel.title.asString(),
          style = TextStyle(
            fontWeight = FontWeight.Bold
          ),
          maxLine = 2
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = bookUiModel.publisher.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = bookUiModel.authors.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = bookUiModel.publishDate.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        Text(
          modifier = Modifier.padding(4.dp),
          text = bookUiModel.price.asString(),
          fontWeight = FontWeight.Bold
        )
      }

      Image(
        modifier = Modifier
          .wrapContentWidth(Alignment.End)
          .weight(0.1f)
          .baseClickable { onClickedBookmark(bookUiModel) },
        imageVector =
          if (bookUiModel.isBookmarked) Icons.Filled.Favorite
          else Icons.Outlined.FavoriteBorder,
        contentDescription = null
      )
    }
  }
}