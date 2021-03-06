package com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jarroyo.firstkotlinmultiplatform.data.LocationModel
import com.jarroyo.firstkotlinmultiplatform.domain.usecase.location.deleteLocation.DeleteLocationRequest
import com.jarroyo.firstkotlinmultiplatform.domain.usecase.location.deleteLocation.DeleteLocationUseCase
import com.jarroyo.firstkotlinmultiplatform.domain.usecase.location.saveLocation.SaveLocationRequest
import com.jarroyo.firstkotlinmultiplatform.domain.usecase.location.saveLocation.SaveLocationUseCase
import com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location.getLocation.ErrorGetLocationListState
import com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location.getLocation.GetLocationListState
import com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location.getLocation.SuccessGetLocationListState
import com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location.saveLocation.ErrorSaveLocationState
import com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location.saveLocation.SaveLocationState
import com.jarroyo.firstkotlinmultiplatform.ui.viewModel.location.saveLocation.SuccessSaveLocationState
import com.jarroyo.firstkotlinmultiplatform.utils.launchSilent
import com.jarroyo.kotlinmultiplatform.domain.Response
import com.jarroyo.kotlinmultiplatform.domain.model.Location
import com.jarroyo.kotlinmultiplatform.domain.usecase.location.GetLocationMPPListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocationViewModel
    @Inject
    constructor(private val getLocationListUseCase: GetLocationMPPListUseCase,
                private val saveLocationUseCase: SaveLocationUseCase,
                private val deleteLocationUseCase: DeleteLocationUseCase,
                private val coroutineContext: CoroutineContext)
    : ViewModel() {

    private var job: Job = Job()

    var getLocationListLiveData = MutableLiveData<GetLocationListState>()
    var saveLocationListLiveData = MutableLiveData<SaveLocationState>()

    init {
    }

    /**
     * SAVE LOCATION
     */
    fun saveLocation(location: Location) = launchSilent(coroutineContext, job) {
        val request = SaveLocationRequest(location)
        val response = saveLocationUseCase.execute(request)
        processSaveWeatherLocationResponse(response)
    }

    fun processSaveWeatherLocationResponse(response: Response<List<LocationModel>>){
        if (response is Response.Success) {
            saveLocationListLiveData.postValue(
                SuccessSaveLocationState(
                    response
                )
            )
        } else if (response is Response.Error) {
            saveLocationListLiveData.postValue(
                ErrorSaveLocationState(
                    response
                )
            )
        }
    }

    /**
     * GET WEATHER LOCATION LIST
     */
    fun getLocationList() = launchSilent(coroutineContext, job) {
        val response = getLocationListUseCase.execute()
        processGetWeatherLocationListResponse(response)
    }

    fun processGetWeatherLocationListResponse(response: Response<List<LocationModel>>){
        if (response is Response.Success) {
            getLocationListLiveData.postValue(
                SuccessGetLocationListState(
                    response
                )
            )
        } else if (response is Response.Error) {
            getLocationListLiveData.postValue(
                ErrorGetLocationListState(
                    response
                )
            )
        }
    }

    /**
     * DELETE LOCATION
     */
    fun deleteLocation(location: Location) = launchSilent(coroutineContext, job) {
        val request = DeleteLocationRequest(location)
        val response = deleteLocationUseCase.execute(request)
        processGetWeatherLocationListResponse(response)
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}