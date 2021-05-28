package ua.nure.myapplication.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

fun restartActivity(baseContext: Context, activity: Activity){
    val intent = baseContext.packageManager
        .getLaunchIntentForPackage(baseContext.packageName)
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    baseContext.startActivity(intent)
    activity.finish()
}

fun isDeadlinePassed(date:String) : Boolean{
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-ddHH:mm",Locale.UK)
    val strDate = sdf.parse(date)!!

    cal.time = strDate

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    val deadline = Calendar.getInstance()
    deadline.set(year, month, day)

    val currentDate = Calendar.getInstance()

    if (currentDate.after(deadline)) {
        return true
    }
    return false
}