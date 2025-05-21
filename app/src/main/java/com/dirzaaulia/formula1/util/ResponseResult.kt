package com.dirzaaulia.formula1.util

/**
 * A generic class that holds a value or an exception
 */
sealed class ResponseResult<out R> {

    object Loading: ResponseResult<Nothing>()
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val throwable: Throwable) : ResponseResult<Nothing>()
}

inline fun <T> executeWithData(body: () -> T): ResponseResult<T> {
    return try {
        ResponseResult.Success(body.invoke())
    } catch (e: Exception) {
        e.printStackTrace()
        ResponseResult.Error(e)
    }
}

//inline fun <T> executeWithResponse(body: () -> Response<T>): ResponseResult<T> {
//    return try {
//        val response = body.invoke()
//        response.body()?.let {
//            ResponseResult.Success(it)
//        } ?: run {
//            throw HttpException(response)
//        }
//    } catch (throwable: Throwable) {
//        throwable.printStackTrace()
//        ResponseResult.Error(handleNetworkError(throwable))
//    }
//}
//
//fun handleNetworkError(throwable: Throwable): Throwable {
//    return when (throwable) {
//        is HttpException -> {
//            // We had non-2XX http error
//            val response: Response<*>? = throwable.response()
//            val errorBody: ResponseBody? = response?.errorBody()
//
//            if (errorBody != null) {
//                // You can get the error body as a string
//                val errorString = errorBody.string()
//                println("Error body as string: $errorString")
//
//                // Or, if you know the structure of the error response, you can parse it
//                // Example: Assuming your error response is a JSON like: { "message": "Error message" }
//                val gson = Gson()
//                val type: Type = object : TypeToken<Map<String, String>>() {}.type
//                val errorMap: Map<String, String> = gson.fromJson(errorString, type)
//                val errorMessage = errorMap["message"] ?: "No error message found"
//                Throwable(errorMessage)
//            } else {
//                println("Error body is null")
//                throwable
//            }
//        }
//        else -> {
//            // Handle other types of errors (e.g., network issues)
//            println("Other error: ${throwable.message}")
//            throwable
//        }
//    }
//}
