package kr.co.architecture.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

data class CenterErrorDialogMessage(
    val errorCode: Int,
    val titleMessage: String,
    val contentMessage: String,
    val confirmButtonMessage: String
) {
    companion object {
        fun getDefault() =
            CenterErrorDialogMessage(-1, "", "", "")
    }
}

@Composable
fun BaseErrorCenterDialog(
    centerErrorDialogMessage: CenterErrorDialogMessage,
    onDismissDialog: () -> Unit = { },
    onClickedConfirm: () -> Unit = { }
) {
    Dialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_10dp))
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.dimen_16dp),
                    vertical = dimensionResource(id = R.dimen.dimen_10dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_12dp),
                        bottom = dimensionResource(id = R.dimen.dimen_16dp)
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                if (centerErrorDialogMessage.titleMessage.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = centerErrorDialogMessage.titleMessage,
                        style = TypoSubhead3.copy(
                            color = colorResource(id = R.color.gray_091E42),
                            fontWeight = FontWeight.W700
                        )
                    )
                }

                if (centerErrorDialogMessage.contentMessage.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.dimen_10dp)
                            )
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        text = centerErrorDialogMessage.contentMessage,
                        style = TypoBody1.copy(
                            color = colorResource(id = R.color.gray_7A869A),
                            fontWeight = FontWeight.W400
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Row(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_16dp))
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .weight(0.49f)
                        .height(dimensionResource(id = R.dimen.dimen_48dp))
                        .background(
                            color = colorResource(id = R.color.gray_091E42),
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_04dp))
                        )
                        .noRippledClickable(onClick = onClickedConfirm)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = centerErrorDialogMessage.confirmButtonMessage,
                        style = TypoSubhead2.copy(
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}