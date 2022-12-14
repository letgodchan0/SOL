package com.finance.android.ui.components


import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.finance.android.R


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AccountLikeItem(
    modifier: Modifier = Modifier,
    bkStatus: Boolean,
    cpLogo: String,
    name: String,
    accountNumber: String,
    cpName: String,
    onClickItem: () -> Unit,
    onClickBookmark: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClickItem()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cpLogo)
                .crossfade(true)
                .build(),
            contentDescription = "회사 로고",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            Text(
                text = name, fontSize = dimensionResource(R.dimen.account_like_name).value.sp,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1

            )
            Spacer(modifier = Modifier.padding(1.dp))
            Row(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = cpName,
                    fontSize = dimensionResource(R.dimen.account_like_account_number).value.sp,
                    color = Color(R.color.noActiveColor),
                    softWrap = false,
                    maxLines = 1

                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = formatAccount(companyName = cpName, accountNumber = accountNumber),
                    fontSize = dimensionResource(R.dimen.account_like_account_number).value.sp,
                    color = Color(R.color.noActiveColor),
                    softWrap = false,
                    maxLines = 1
                )

            }

        }

        IconButton(onClick = {
            onClickBookmark()
        }, modifier = Modifier) {


            AnimatedVisibility(
                visible = !bkStatus,
                enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "like",
                    modifier = Modifier.size(35.dp),
                    tint = if (bkStatus) Color(0xffeeca66) else Color.LightGray

                )


            }

            AnimatedVisibility(
                visible = bkStatus,
                enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) +
                        fadeIn() + expandIn(expandFrom = Alignment.TopStart),
                exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) +
                        fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart)
            ) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "like",
                    modifier = Modifier.size(35.dp),
                    tint = if (!bkStatus) Color.LightGray else Color(0xffeeca66)

                )
            }
        }
    }
}

