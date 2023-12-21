package com.themoviedb.weektvshow.data.source

import com.themoviedb.weektvshow.data.Result
import com.themoviedb.weektvshow.data.network.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

suspend fun <R : Any, E : Any> mapResponse(
    request: suspend () -> NetworkResponse<R, E>
): Result<R, E> {
    Result.Loading
    return when (val result = request()) {
        is NetworkResponse.Success -> Result.Success(result.body)
        is NetworkResponse.ApiError -> Result.Error(result.body, result.code)
        is NetworkResponse.NetworkError -> Result.Error(exception = result.error)
        is NetworkResponse.UnknownError -> Result.Error(exception = result.error)
    }
}

//with flows
fun <R : Any, E : Any> mapResponse(
    dispatcher: CoroutineDispatcher,
    request: suspend () -> NetworkResponse<R, E>
): Flow<Result<R, E>> = flow {
    emit(Result.Loading)
    when (val result = request()) {
        is NetworkResponse.Success -> emit(Result.Success(result.body))
        is NetworkResponse.ApiError -> emit(Result.Error(result.body, result.code))
        is NetworkResponse.NetworkError -> emit(Result.Error(exception = result.error))
        is NetworkResponse.UnknownError -> emit(Result.Error(exception = result.error))
    }
}.flowOn(dispatcher)