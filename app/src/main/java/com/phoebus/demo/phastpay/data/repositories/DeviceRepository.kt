package com.phoebus.demo.phastpay.data.repositories

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getPackageName(): String {
        val packageInfo = getPackageInfo();
        return packageInfo.packageName
    }

    fun getVersionName(): String {
        val packageInfo = getPackageInfo();
        return packageInfo.versionName.orEmpty()
    }

    fun getAppName(): String {
        return context.applicationInfo.loadLabel(context.packageManager).toString()
    }

    fun getPackageInfo(): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
    }
}