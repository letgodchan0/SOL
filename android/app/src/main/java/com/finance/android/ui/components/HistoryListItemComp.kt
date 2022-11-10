package com.finance.android.ui.components

import android.util.DisplayMetrics
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finance.android.R
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.holix.android.bottomsheetdialog.compose.NavigationBarProperties
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

// HistoryList 를 사용하기 위한 엔티티
data class HistoryEntity(
    val date : LocalDateTime,
    val name : String,
    val value : Int
)

@Composable
fun showHistoryList(
    modifier : Modifier,
    onClick : (year : Int, month : Int) -> Unit = { year: Int, month: Int -> {}},
    type : String = "계좌",
    historyList : List<HistoryEntity>,
    emptyMessage : String = "거래 내역이 없어요."
) {
    val context = LocalContext.current
    var currentMonth = remember { mutableStateOf(YearMonth.now()) }
    var currentMenu = remember { mutableStateOf(0) }
    var column by remember {
         mutableStateOf(0)
    }
    var bf by remember {
        mutableStateOf(0
        )
    }
    var showMenuList by remember { mutableStateOf(false) }
    val menuList : List<String> = when(type) {
        "포인트" -> listOf("모두", "적립", "출금")
        else -> listOf("모두", "입금", "출금")
    }
    val moneyType = when(type) {
        "포인트" -> "포인트"
        else -> "원"
    }

    if(showMenuList) {
        val outMetrics = DisplayMetrics()

        BoxWithConstraints(modifier = Modifier.background(color = Color.Blue)) {
            BottomSheetDialog(
                onDismissRequest = {
                    showMenuList = false
                },
                properties = BottomSheetDialogProperties(
                    navigationBarProperties = NavigationBarProperties(),
                    behaviorProperties = BottomSheetBehaviorProperties(
//                        maxHeight = BottomSheetBehaviorProperties.Size(this@BoxWithConstraints.maxHeight.toPx(context)/2),
                    )
                )
            ) {
                Surface (
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 10.dp, bottom = 16.dp)
                            .verticalScroll(rememberScrollState()),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "보기 선택", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        repeat(3) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        column = 0
                                        currentMenu.value = it
                                        showMenuList = false
                                    }
                            ){
                                Text(text = menuList[it])
                                Spacer(modifier = Modifier.weight(1f))
                                if(it == currentMenu.value){
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(20.dp),
                                        contentDescription = "",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                RoundedCornerShape(dimensionResource(R.dimen.calendar_default) / 2)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.padding(10.dp, top = 20.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable {
                                column = 0
                                currentMonth.value = currentMonth.value.minusMonths(1)
                                onClick(
                                    currentMonth.value.year,
                                    currentMonth.value.monthValue
                                )
                            },
                        contentDescription = "",
                    )
                    Text(text = currentMonth.value.year.toString() + "년 " + currentMonth.value.monthValue.toString() + "월", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Image(
                        painter = painterResource(id = if(YearMonth.from(LocalDateTime.now()).isAfter(currentMonth.value)) R.drawable.ic_next else R.drawable.ic_next_disabled),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable {
                                column = 0
                                if (YearMonth
                                        .from(LocalDateTime.now())
                                        .isAfter(currentMonth.value)
                                ) {
                                    currentMonth.value = currentMonth.value.plusMonths(1)
                                    onClick(
                                        currentMonth.value.year,
                                        currentMonth.value.monthValue
                                    )
                                }
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .clickable { showMenuList = true }
                        .padding(10.dp)
                ) {
                    Text(text = menuList[currentMenu.value] + " ▾")
                }
            }
            if(historyList.isEmpty()) {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = emptyMessage, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Gray)
                }

            }
            else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    bf = findIdx(type = currentMenu.value, historyList = historyList)
                    for (history in historyList) {
                        when(currentMenu.value) {
                            1 -> {
                                if(history.value > 0 && compareDates(history.date, currentMonth.value)) {
                                    column++
                                    HistoryItem(
                                        date = history.date,
                                        title = history.name,
                                        amount = history.value,
                                        moneyType = moneyType,
                                        type = menuList[1],
                                        showDate = bf > history.date.dayOfMonth
                                    )
                                    bf = history.date.dayOfMonth
                                }
                            }
                            2 -> {
                                if(history.value < 0 && compareDates(history.date, currentMonth.value)) {
                                    column++
                                    HistoryItem(
                                        date = history.date,
                                        title = history.name,
                                        amount = history.value,
                                        moneyType = moneyType,
                                        type = menuList[2],
                                        showDate = bf > history.date.dayOfMonth
                                    )
                                    bf = history.date.dayOfMonth
                                }
                            }
                            else -> {
                                if(compareDates(history.date, currentMonth.value)) {
                                    column++
                                    HistoryItem(
                                        date = history.date,
                                        title = history.name,
                                        amount = history.value,
                                        moneyType = moneyType,
                                        type = if(history.value < 0) menuList[2] else menuList[1],
                                        showDate = bf > history.date.dayOfMonth
                                    )
                                    bf = history.date.dayOfMonth
                                }
                            }
                        }

                    }
                }
                if(column == 0){
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = emptyMessage, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun compareDates(d1 : LocalDateTime, d2 : YearMonth) : Boolean {
    return d1.year == d2.year && d1.month == d2.month
}

@Preview(showBackground = true)
@Composable
fun HistoryItem(
    date : LocalDateTime = LocalDateTime.now(),
    title : String = "사용처",
    amount : Int = 1000000000,
    moneyType : String = "원",
    type : String = "출금",
    showDate : Boolean = true
    ) {
    val dec = DecimalFormat("#,###")

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(

            ) {
                DateText(text = date.format(DateTimeFormatter.ofPattern("MM.dd")).toString(), show = showDate)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                BasicHistoryText(text = title)
                GrayHistoryText(date.format(DateTimeFormatter.ofPattern("HH:mm")).toString())
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            MoneyHistoryText(text = dec.format(amount) + moneyType, minus = amount < 0)
            GrayHistoryText(type)
        }
    }
}

@Composable
fun findIdx(
    type : Int,
    historyList: List<HistoryEntity>
): Int {
    when(type) {
        1 -> for(i in historyList) {
            if(i.value > 0) return i.date.dayOfMonth + 1
        }
        2 -> for(i in historyList) {
            if(i.value < 0) return i.date.dayOfMonth + 1
        }
        else -> return historyList[0].date.dayOfMonth + 1
    }
    return historyList[0].date.dayOfMonth + 1
}

@Preview
@Composable
fun BasicHistoryText(
    text : String = "Basic"
) {
    Text( text = text)
}

@Preview(showBackground = true)
@Composable
fun HistoryItem2(
) {
    HistoryItem( showDate = false )
}

@Preview
@Composable
fun DateText(
    text : String = "10.21",
    show : Boolean = true
) {
    Text( text = text, fontWeight = FontWeight.SemiBold, color = if(show) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface)
}

@Preview
@Composable
fun GrayHistoryText(
    text : String = "Gray"
) {
    Text( text = text, color = Color.Gray, fontSize = 12.sp)
}

@Preview
@Composable
fun MoneyHistoryText(
    text : String = "Money",
    minus : Boolean = true
) {
    val sign : String = if(minus) "" else "+"
    Text( text = sign+text, fontWeight = FontWeight.SemiBold, color = if(minus) Color(0xff3A00FF) else Color(0xffFF0046))
}

//@Preview
//@Composable
//fun test1() {
//    MoneyHistoryText(minus = false)
//}
//
//@Preview
//@Composable
//fun test2() {
//    val historyList = MutableList(30){i -> HistoryEntity(LocalDateTime.now().minusHours(i.toLong()).plusDays(i.toLong()), "사용처 $i", if(i % 2 == 0) -100000000 else 100000000)}
//    showHistoryList(historyList = historyList, type = "포인트")
//}
//
//@Preview
//@Composable
//fun test3() {
//    val historyList = MutableList(30){ i -> HistoryEntity(LocalDateTime.now().minusHours(i.toLong()).plusDays(i.toLong()), "사용처 $i", if(i % 2 == 0) -100000000 else 100000000)}
//    showHistoryList(historyList = historyList)
//}
