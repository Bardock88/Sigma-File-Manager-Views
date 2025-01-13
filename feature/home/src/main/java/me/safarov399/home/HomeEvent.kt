package me.safarov399.home

sealed class HomeEvent {
    data class ChangePath(val newPath: String) : HomeEvent()
}