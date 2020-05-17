package com.example.memorizeeffectively

import android.widget.TextView


enum class IntentKey {
    WORD, DEFINITION, MODE, IF_MEMORIZED, POSITION, DATE_STRING, TIME_STRING
}

enum class Mode {
    NEW_WORD, EDIT
}

enum class StatusWords {
    STATUS_WORD, STATUS_DEFINITION
}