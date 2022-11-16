package com.jdamcd.runlog.shared.api

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MapboxStaticTest {

    private val polyline = "mfyvFvg_cMs@{BIiD}B_Hk@sD@OLH]m@{CuLDeA[uAJ_BSwAi@yAYiC{BkFo@oDiA{CSsBgAiEc@aDeAyCw@uEqCeJAw@s@oCO}A{BqHOmAaAoDsHaMb@LSWQqCM[g@Q}@iCc@_@a@cAmA]iEuEgC_EiJoJ{VkSaKcGUq@mBy@sFgE}F{CkOqEu@~@iCdFtBjAUnAwBfF_IpNwAtCqc@cNcJgJsH_JyMuMuO_Q_P}OkbAeeAyOeQ{U}Us@{@Ds@i@g@eD}AyCwB_QsNkMgJuW{SuN}IeYmPoA_@cIiEqCx@aE~CWAcA}LCiDg@cGk@{Oq@kG@_C{@eK@}Bm@sFiAsSAuBe@uEoAeT[oJ_@cEaf@hGyTlB_HvAcEjD}LxNqBvAyEdGaAnBwFvFmAJiDu@yCAcIeBiBeBgHyCsJgGcJmJ}PmRwAuBDQ]q@}LyKgF{Fu@eAk@iCWSyRfKk@?kBhA_Cj@uIrDwAsOeFnAaNnAwE`AeP`AmAG_[mHsCWSH{AlK_@VuHuA}N{DeBkBBoBxAmOtAoJZiEdA}EDyAiA_DyAkB@aBeHeEUm@yAgAm@P[|As@v@kAbC}@vEq@zAMdBmCdE{@vCgDzGcCbHBh@]|AyB|FYbBw@rAwBpGkAfBC|CoArAuBrHsBvC{AbFyArASvCoAxBoA|DeC|AWhAl@~AExARXjBoCHe@aDuCgLwHoBy@HJ{BgAyCkCeAS}@eBkF}DkEsBg]yToAe@DRq@w@{IkGe@w@}@OkAiAkKcGiDoCqHuEwBwBcAa@k@NyDkDsAo@iNsKmB]_@}@g@_@mBg@aDyB}FqE{I}EiIyGuCyAoCeCiL{GmEuDiKcGoEqDm@y@aEkBuFoDmCeCkDwGuBmGgDwH}AmBsEcDeC_AiAtD]zBi@rBa@Ha@EiBuAuFaDS@a@z@M|Au@tBeAtAkE{AyEaCw@hCjEjCpAvARl@a@zAcFpHcEzLU`OUzAeAhDFVpKlGnFbElRtLtNzJJh@sAfE[pAFPrMlIZMbB{FXAn@h@j@JlLnH|AvA|FdDhI|FdBt@xZlSpAh@lB~AZBn@v@jDzBvBdAdEhDfHbEzLjIjB|AVvAXZnLbIx@~BOdHdAdExBxBlDdBxGnHlB~@zFzAfDZ~@z@hBpDhClBdA@fDgAtDnAzAnAnBtEhEvCnBrExAlAbCj@rBHjHeBnAV~EdCHh@q@hDJlAmBpFmBzD@vAyArBCf@q@`A?x@eBrBKpAk@pBu@bBKz@_@b@?]_Ak@iAaCeDwA_EwD"
    private val encodedPolyline = "mfyvFvg_cMs@%7BBIiD%7DB_Hk@sD@OLH%5Dm@%7BCuLDeA%5BuAJ_BSwAi@yAYiC%7BBkFo@oDiA%7BCSsBgAiEc@aDeAyCw@uEqCeJAw@s@oCO%7DA%7BBqHOmAaAoDsHaMb@LSWQqCM%5Bg@Q%7D@iCc@_@a@cAmA%5DiEuEgC_EiJoJ%7BVkSaKcGUq@mBy@sFgE%7DF%7BCkOqEu@~@iCdFtBjAUnAwBfF_IpNwAtCqc@cNcJgJsH_JyMuMuO_Q_P%7DOkbAeeAyOeQ%7BU%7DUs@%7B@Ds@i@g@eD%7DAyCwB_QsNkMgJuW%7BSuN%7DIeYmPoA_@cIiEqCx@aE~CWAcA%7DLCiDg@cGk@%7BOq@kG@_C%7B@eK@%7DBm@sFiAsSAuBe@uEoAeT%5BoJ_@cEaf@hGyTlB_HvAcEjD%7DLxNqBvAyEdGaAnBwFvFmAJiDu@yCAcIeBiBeBgHyCsJgGcJmJ%7DPmRwAuBDQ%5Dq@%7DLyKgF%7BFu@eAk@iCWSyRfKk@%3FkBhA_Cj@uIrDwAsOeFnAaNnAwE%60AeP%60AmAG_%5BmHsCWSH%7BAlK_@VuHuA%7DN%7BDeBkBBoBxAmOtAoJZiEdA%7DEDyAiA_DyAkB@aBeHeEUm@yAgAm@P%5B%7CAs@v@kAbC%7D@vEq@zAMdBmCdE%7B@vCgDzGcCbHBh@%5D%7CAyB%7CFYbBw@rAwBpGkAfBC%7CCoArAuBrHsBvC%7BAbFyArASvCoAxBoA%7CDeC%7CAWhAl@~AExARXjBoCHe@aDuCgLwHoBy@HJ%7BBgAyCkCeAS%7D@eBkF%7DDkEsBg%5DyToAe@DRq@w@%7BIkGe@w@%7D@OkAiAkKcGiDoCqHuEwBwBcAa@k@NyDkDsAo@iNsKmB%5D_@%7D@g@_@mBg@aDyB%7DFqE%7BI%7DEiIyGuCyAoCeCiL%7BGmEuDiKcGoEqDm@y@aEkBuFoDmCeCkDwGuBmGgDwH%7DAmBsEcDeC_AiAtD%5DzBi@rBa@Ha@EiBuAuFaDS@a@z@M%7CAu@tBeAtAkE%7BAyEaCw@hCjEjCpAvARl@a@zAcFpHcEzLU%60OUzAeAhDFVpKlGnFbElRtLtNzJJh@sAfE%5BpAFPrMlIZMbB%7BFXAn@h@j@JlLnH%7CAvA%7CFdDhI%7CFdBt@xZlSpAh@lB~AZBn@v@jDzBvBdAdEhDfHbEzLjIjB%7CAVvAXZnLbIx@~BOdHdAdExBxBlDdBxGnHlB~@zFzAfDZ~@z@hBpDhClBdA@fDgAtDnAzAnAnBtEhEvCnBrExAlAbCj@rBHjHeBnAV~EdCHh@q@hDJlAmBpFmBzD@vAyArBCf@q@%60A%3Fx@eBrBKpAk@pBu@bBKz@_@b@%3F%5D_Ak@iAaCeDwA_EwD"

    @Test
    fun generatesMapUrlWithUrlEncodedPolyline() {
        val mapUrl = MapboxStatic.makeUrl(
            pathPolyline = polyline,
            accessToken = "mytoken"
        )

        mapUrl shouldBe "https://api.mapbox.com/styles/v1/mapbox/light-v10/static/path($encodedPolyline)/auto/400x160@2x?logo=false&access_token=mytoken"
    }

    @Test
    fun generatesMapUrlWithRequestedSize() {
        val mapUrl = MapboxStatic.makeUrl(
            pathPolyline = "polyline",
            accessToken = "mytoken",
            width = 200,
            height = 100
        )

        mapUrl shouldBe "https://api.mapbox.com/styles/v1/mapbox/light-v10/static/path(polyline)/auto/200x100@2x?logo=false&access_token=mytoken"
    }

    @Test
    fun generatesMapUrlWithDarkModeStyling() {
        val mapUrl = MapboxStatic.makeUrl(
            pathPolyline = "polyline",
            accessToken = "mytoken",
            darkMode = true
        )

        mapUrl shouldBe "https://api.mapbox.com/styles/v1/mapbox/dark-v10/static/path+fff(polyline)/auto/400x160@2x?logo=false&access_token=mytoken"
    }
}
