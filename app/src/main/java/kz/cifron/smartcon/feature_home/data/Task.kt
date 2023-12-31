package kz.cifron.smartcon.feature_home.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class Item : ArrayList<Tasks>()

@Parcelize
data class Tasks(
    val ADDR: String,
    val HOME: String,
    val ID_CONTROLER: Int,
    val ID_SCH: Int,
    val IN_SCH: Int,
    val KV: @RawValue Any? = null,
    val LS: Int,
    val NAME_CLIENT: String,
    val NAME_REG5: String,
    val NAME_SCH: String,
    val NAME_TIPSCH: String,
    val OUTSALDO: String,
    val POVERKA_DATE: String,
    var RAZ_TIPSCH: Int,
    val TYPE_ORDER: Int,
    val id: Int
) : Parcelable