package ighorosipov.cocktailapp.domain.utils

sealed class Result<T: Any>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T: Any>(data: T): Result<T>(data)
    class Error<T: Any>(message: String?, data: T? = null): Result<T>(data, message)
    class Loading<T : Any> : Result<T>()
}