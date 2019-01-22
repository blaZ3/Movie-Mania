package com.dailytools.healthbuddy.base

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel: ViewModel() {
    private var modelSubject: BehaviorSubject<Model> = BehaviorSubject.create()
    private val eventsSubject: BehaviorSubject<ViewEvent> = BehaviorSubject.create()

    protected lateinit var model: Model
    protected lateinit var initEvent: ViewEvent

    fun getViewModelObservable(): Observable<Model> {
        return modelSubject
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(model)
    }

    fun getViewEventObservable(): Observable<ViewEvent> {
        return eventsSubject
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(initEvent)
    }

    fun updateModel(newModel: Model) {
        model = newModel
        modelSubject.onNext(model)
    }

    fun sendEvent(event: ViewEvent){
        eventsSubject.onNext(event)
    }
}

interface ViewEvent

abstract class Model
data class ProgressModel(
    val isShown: Boolean = false,
    val text: String = ""
): Model()
