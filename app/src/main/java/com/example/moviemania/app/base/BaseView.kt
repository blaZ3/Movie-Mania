package com.dailytools.healthbuddy.base

import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent

interface BaseView{
    fun initView()
    fun getParentView(): BaseView?
    fun updateView(stateModel: StateModel)
    fun handleEvent(event: ViewEvent)
}