package com.example.ecommercei.utils

import android.util.Patterns
import java.util.regex.Pattern


fun validationEmail(email:String):RegisterValidation{
    if (email.isEmpty())
        return RegisterValidation.Failed("Email can't be empty!")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email Format!")

    return RegisterValidation.Success
}

fun validationPassword(password:String):RegisterValidation{
    if (password.isEmpty())
        return RegisterValidation.Failed("Password can't be empty!")

    if (password.length <6)
        return RegisterValidation.Failed("Password should 6 char!")

    return RegisterValidation.Success
}