package com.xeniac.ethereumwalletdemo.core.util

abstract class ResultError {
    object BlankField : ResultError()
    data object ShortPassword : ResultError()
    data object InvalidPassword : ResultError()
}