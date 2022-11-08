package com.finance.android.ui.fragments

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.finance.android.ui.screens.groupAccount.GroupAccountDetailScreen
import com.finance.android.ui.screens.groupAccount.GroupAccountMainScreen
import com.finance.android.utils.Const
import com.finance.android.viewmodels.GroupAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupFragment(
    navController: NavController,
    groupAccountViewModel: GroupAccountViewModel = hiltViewModel()
) {
    val innerNavController = rememberNavController()

    NavHost(
        navController = innerNavController,
        startDestination = Const.GROUP_ACCOUNT_MAIN_SCREEN
    ) {
        composable(Const.GROUP_ACCOUNT_MAIN_SCREEN){
            GroupAccountMainScreen(navController = innerNavController, groupAccountViewModel = groupAccountViewModel)
        }
        composable(Const.GROUP_ACCOUNT_MAKE_SCREEN){
            //
        }
        composable(Const.GROUP_ACCOUNT_DETAIL_SCREEN){
            GroupAccountDetailScreen(navController = innerNavController)
        }

    }
}