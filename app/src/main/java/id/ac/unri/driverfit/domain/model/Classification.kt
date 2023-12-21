package id.ac.unri.driverfit.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classification(
    val label: String? = null,
    val scores: Float,
) : Parcelable
