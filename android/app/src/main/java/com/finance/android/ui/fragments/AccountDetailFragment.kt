package com.finance.android.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import com.finance.android.R
import com.finance.android.ui.components.AccountCardComp
import com.finance.android.ui.components.BackHeaderBar
import com.finance.android.utils.Const
@Composable
fun AccountDetailFragment(
    navController: NavController,
    onClose: () -> Unit,
    acName: String,
    cpName: String,
    acNo: String,
    balance: Int,
) {

    Scaffold(
        topBar = {
            BackHeaderBar(text = "내 계좌", modifier = Modifier, onClickBack = onClose)
        },

        ) { innerPaddingModifier ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPaddingModifier)
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            AccountCardComp(modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15)
                ),
                acName = acName,
                cpName = cpName,
                acNo = acNo,
                balance = balance,
                onClickButton = {
                    navController.navigate("${Const.Routes.REMIT}/${cpName}/${acNo}/${balance}")
                })
        }
    }

}