package com.authblue.authbluekotlinsdk.View.Component

import android.content.Context
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//@Composable
//fun TopAppBarScreen(
//    accountButtonCallback: () -> Unit,
//    notificationButtonCallback: () -> Unit
//) {
//    val context = LocalContext.current
//    val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
////    val name = sharedPref.getString("personal_name", "")
//
//    var mncRegisterStatus by remember { mutableStateOf(false) }
//    var personalName by remember { mutableStateOf("") }
//    var personalEmail by remember { mutableStateOf("") }
//    var personalBirthday by remember { mutableStateOf("") }
//    var personalAge by remember { mutableStateOf("") }
//    var personalSex by remember { mutableStateOf("") }
//    var personalAddress by remember { mutableStateOf("") }
//    var personalPhone by remember { mutableStateOf("") }
//
//    var notificationNumber: Int by remember{
//        mutableStateOf(0)
//    }
//
//    LaunchedEffect(key1 = Unit){
//        mncRegisterStatus = sharedPref.getBoolean("mnc_register_status", false)
//        personalName = sharedPref.getString("personal_name", "").toString()
//        personalEmail = sharedPref.getString("personal_email", "").toString()
//        personalBirthday = sharedPref.getString("personal_birthday", "").toString()
//        personalAge = sharedPref.getString("personal_age", "").toString()
//        personalSex = sharedPref.getString("personal_sex", "").toString()
//        personalAddress = sharedPref.getString("personal_address", "").toString()
//        personalPhone = sharedPref.getString("personal_phone", "").toString()
//
//        if(mncRegisterStatus == false){
//            notificationNumber += 1
//        }
//        if(personalName == ""){
//            notificationNumber += 1
//        }
//        if(personalEmail == ""){
//            notificationNumber += 1
//        }
//        if(personalBirthday == ""){
//            notificationNumber += 1
//        }
//        if(personalAge == ""){
//            notificationNumber += 1
//        }
//        if(personalSex == ""){
//            notificationNumber += 1
//        }
//        if(personalAddress == ""){
//            notificationNumber += 1
//        }
//        if(personalPhone == ""){
//            notificationNumber += 1
//        }
//
//    }
//
//    TopAppBar(
//        backgroundColor = Color.White,
//        title = {
//            if (personalName != null) {
//                Text(personalName)
//            }else{
//                Text("")
//            }
//        },
//        navigationIcon = {
////            IconButton(onClick = { /* do something */ }) {
////                Icon(Icons.Filled.Apps, contentDescription = "Open Apps")
////            }
//        },
//        actions = {
//            if(notificationNumber > 0){
//                IconButton(onClick = {
//                    Log.d("DEBUG", "TopAppBarScreen button clicked")
//                    notificationButtonCallback()
//                }) {
//                    BadgedBox(badge = { Badge { Text("${notificationNumber}") } }) {
//                        Icon(Icons.Outlined.Notifications, contentDescription = "Notification")
//                    }
//                }
//            }else{
//                Log.d("DEBUG", "TopAppBarScreen button clicked with ${notificationNumber}")
//                IconButton(onClick = {
//                    Log.d("DEBUG", "TopAppBarScreen button clicked")
//                    notificationButtonCallback()
//                }) {
//                    Icon(Icons.Outlined.Notifications, contentDescription = "Notification")
//                }
//            }
//            IconButton(onClick = {
//                Log.d("DEBUG", "TopAppBarScreen button clicked")
//                accountButtonCallback()
//            }) {
//                Icon(Icons.Outlined.AccountCircle, contentDescription = "AccountCircle")
//            }
//        }
//    )
//}


