package cn.spacexc.wearbili.remake.common.networking.db

import android.app.Application
import androidx.room.Room

class CookiesRepository(application: Application) {
    private val database = Room.databaseBuilder(
        context = application,
        klass = CookiesDatabase::class.java,
        name = "wearbili_cookies"
    ).fallbackToDestructiveMigration().build()

    val dao = database.dao()
}