package me.safarov399.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Effect, Event> : ViewModel() {

    private val viewState: State by lazy { getInitialState() }

    abstract fun getInitialState(): State

    private val _state = MutableStateFlow(viewState)
    val state: StateFlow<State> = _state

    private val _event = MutableSharedFlow<Event>()
    private val event: SharedFlow<Event> = _event

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        initEventSubscribers()
    }

    protected fun setState(state: State) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    fun postEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    fun postEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun initEventSubscribers() {
        _event
            .onEach {
                onEventUpdate(it)
            }.launchIn(viewModelScope)
    }

    protected open fun onEventUpdate(event: Event) {}

    fun getCurrentState() = state.value
}