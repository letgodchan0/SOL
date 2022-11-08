package com.finance.android

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.finance.android.ui.fragments.*
import com.finance.android.ui.screens.AttendanceFragment
import com.finance.android.ui.theme.FinanceTheme
import com.finance.android.utils.Const
import dagger.hilt.android.HiltAndroidApp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FinanceApp() {
    FinanceTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Const.Routes.LOGIN
        ) {
            composable(Const.Routes.LOGIN) {
                LoginFragment(navController = navController)
            }
            composable(Const.Routes.SIGNUP) {
                SignupFragment(navController = navController)
            }
            composable(Const.Routes.MAIN) {
                MainFragment(navController = navController)
            }
            composable(
                route = "${Const.Routes.REMIT}/{accountName}/{accountNumber}/{balance}",
                arguments = listOf(
                    navArgument("accountName") { type = NavType.StringType },
                    navArgument("accountNumber") { type = NavType.StringType },
                    navArgument("balance") { type = NavType.IntType }
                    // TODD 받는계좌번호, 받는계좌은행이름, 금액
                )
            ) {
                RemitFragment(navController = navController)
            }
            composable(
                route = "${Const.Routes.ACC_DETAIL}/{acName}/{cpName}/{acNo}",
                arguments = listOf(
                    navArgument("acName") { type = NavType.StringType },
                    navArgument("cpName") { type = NavType.StringType },
                    navArgument("acNo") { type = NavType.StringType },
                )
            ) {
                AccountDetailFragment(
                    acName = it.arguments!!.getString("acName")!!,
                    cpName = it.arguments!!.getString("cpName")!!,
                    acNo = it.arguments!!.getString("acNo")!!,
                    navController = navController,
                    onClose = { navController.popBackStack() }
                )
            }
            composable(Const.Routes.ADD_ASSET) {
                AnimatedVisibility(
                    initiallyVisible = false,
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ),
                    exit = slideOutVertically()
                ) {
                    AddAssetFragment(
                        onClose = {
                            navController.popBackStack()
                        }
                    )
                }
            }
            composable(Const.Routes.ASSET) {
                AnimatedVisibility(
                    initiallyVisible = false,
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ),
                    exit = slideOutVertically()
                ) {
                    AssetFragment(navController = navController, onClose = {
                        navController.popBackStack()
                    })
                }
            }
            composable(Const.Routes.ATTENDANCE) {
                AnimatedVisibility(
                    initiallyVisible = false,
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ),
                    exit = slideOutVertically()
                ) {
                    AttendanceFragment(
                        onClose = {
                            navController.popBackStack()
                        }
                    )
                }
            }
            composable(
                route = "${Const.Routes.STOCK}/{fnName}/{close}/{per}",
                arguments = listOf(
                    navArgument("fnName") { type = NavType.StringType },
                    navArgument("close") { type = NavType.IntType },
                    navArgument("per") { type = NavType.FloatType },
                )
            ) {
                StockDetailFragment(
                    fnName = it.arguments!!.getString("fnName")!!,
                    close = it.arguments!!.getInt("close"),
                    per = it.arguments!!.getFloat("per"),
                    onClose = {
                        navController.popBackStack()
                    }
                )
            }
            //            composable(
//                route = "${Const.Routes.ACC_DETAIL}/{acName}/{cpName}/{acNo}/{balance}",
//                arguments = listOf(
//                    navArgument("acName") { type = NavType.StringType },
//                    navArgument("cpName") { type = NavType.StringType },
//                    navArgument("acNo") { type = NavType.StringType },
//                    navArgument("balance") { type = NavType.IntType }
//
//                )
//            ) {
//                AccountDetailFragment(
//                    acName = it.arguments!!.getString("acName"),
//                    cpName = it.arguments!!.getString("cpName"),
//                    acNo = it.arguments!!.getString("acNo"),
//                    balance = it.arguments!!.getInt("balance"),
//                )
//            }
            composable(Const.Routes.PEDOMETER) {
                AnimatedVisibility(
                    initiallyVisible = false,
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ),
                    exit = slideOutVertically()
                ) {

                    PedometerFragment(
                        onClose = { navController.popBackStack() }
                    )
                }
            }
            composable(Const.Routes.GROUPACCOUNT) {
                GroupAccountFragment(navController = navController)
            }
        }
    }
}

@HiltAndroidApp
class FinanceApplication : Application()
