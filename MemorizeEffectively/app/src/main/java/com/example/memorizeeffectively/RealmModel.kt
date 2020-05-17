package com.example.memorizeeffectively

import io.realm.RealmObject

open class RealmModel : RealmObject() {

    var word : String = ""

    var definition : String = ""

    var ifMemorized : Boolean = false

    var dateString : String = ""

    var timeString : String = ""

    var statusWords : String = StatusWords.STATUS_WORD.name

}