package com.finance.android.ui.components

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.finance.android.R
import com.finance.android.ui.theme.MainColor
import com.finance.android.utils.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Preview
@Composable
fun UserBalanceInfo(
    title: String = "신한 주거래 S20",
    isAccount: Boolean = true,
    type: String = "계좌",
    cpName: String? = "신한은행",
    acNo: String? = "1234567890",
    balance: String = "100,000,000원",
    cpLogo: String? = null,
    acMain: Int? = null,
    acType: Int? = null,
    onClickCrown : (acNo : String) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val bgColor = remember { mutableStateOf(Color.Transparent) }
    val textColor = remember { mutableStateOf(Color.Transparent) }
    val changeAc = remember { mutableStateOf(false) }

    val animatedColor = animateColorAsState(bgColor.value, animationSpec = tween(500))
    val animatedTextColor =
        animateColorAsState(textColor.value, animationSpec = tween(500))

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            if (cpLogo != null && Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
                val bitmap = BitmapUtil.getBitmapFromURL(cpLogo)
                bitmap?.let {
                    val palette = Palette.from(bitmap).generate()
                    bgColor.value = Color(palette.vibrantSwatch?.rgb ?: MainColor.toArgb())
                    textColor.value =
                        Color(palette.vibrantSwatch?.titleTextColor ?: Color.White.toArgb())
                }
            } else {
                bgColor.value = MainColor
                textColor.value = Color.White
            }
        }
    }

    val buttonText = when (type) {
        "포인트" -> "출금"
        else -> "송금"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_medium),
                start = 10.dp,
                end = 10.dp
            )
            .height(160.dp)
            .background(
                animatedColor.value,
                RoundedCornerShape(dimensionResource(R.dimen.calendar_default) / 2)
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(start = 23.dp, end = 23.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = title,
                        fontSize = 20.sp,
                        color = animatedTextColor.value,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (acType == 1)
                        Box(modifier = Modifier.size(27.dp)) {
                            Canvas(
                                modifier = if (acMain == 1) Modifier
                                    .size(27.dp)
                                else Modifier
                                    .size(27.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        changeAc.value = true
                                    }
                            ) {
                                drawCircle(
                                    color = Color.White
                                )
                            }
                            Image(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.Center),
                                painter = painterResource (if (acMain == 1) R.drawable.crown else R.drawable.empty_crown),
                                contentDescription = null
                            )
                            if (changeAc.value) {
                                CustomDialog(
                                    dialogType = DialogType.INFO,
                                    dialogActionType = DialogActionType.TWO_BUTTON,
                                    title = "대표계좌를 변경합니다",
                                    subTitle = "다시 한번 확인해주세요",
                                    onPositive = {
                                        changeAc.value = false
                                        onClickCrown(acNo!!)
                                    },
                                    onNegative = {
                                        changeAc.value = false
                                    },
                                    positiveText = "확인",
                                    negativeText = "취소"
                                )
                            }
                        }
                }
                if (isAccount) {
                    if (cpName != null && acNo != null) {
                        Text(
                            text = "$cpName ${formatAccount(cpName, acNo)}",
                            fontSize = 14.sp,
                            color = animatedTextColor.value,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = balance,
                    fontSize = 25.sp,
                    color = animatedTextColor.value,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(0.dp, Color.Transparent),
                    enabled = true
                ) {
                    Text(
                        text = buttonText,
                        fontSize = dimensionResource(id = R.dimen.font_size_btn_bottom_text).value.sp,
                        color = animatedColor.value
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Test() {
    UserBalanceInfo(
        title = "포인트",
        isAccount = false,
        balance = "100 포인트",
        type = "포인트"
    )
}
