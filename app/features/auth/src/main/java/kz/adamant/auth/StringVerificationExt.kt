package kz.adamant.auth


private val PASSWORD_PATTERN by lazy(LazyThreadSafetyMode.NONE) {
    "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$.%]).{${PASSWORD_AT_LEAST_SIZE},20})".toRegex()
}

const val PASSWORD_AT_LEAST_SIZE = 8
const val USERNAME_AT_LEAST_SIZE = 5

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidName() = length >= USERNAME_AT_LEAST_SIZE

fun String.isValidPassword(): Boolean {
    return isNotEmpty() && PASSWORD_PATTERN.matches(this)
}