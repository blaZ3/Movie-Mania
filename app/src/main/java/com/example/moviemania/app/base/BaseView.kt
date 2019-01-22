package com.dailytools.healthbuddy.base

interface BaseView{
    fun initView()
    fun getParentView(): BaseView?
    fun updateView(model: Model)
    fun handleEvent(event: ViewEvent)
}