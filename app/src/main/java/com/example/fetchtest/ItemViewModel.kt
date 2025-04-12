package com.example.fetchtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel
class ItemViewModel(private val repository: ItemRepository) : ViewModel() {
    private val mutableUiStateFlow = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = mutableUiStateFlow
    fun loadData() {
        viewModelScope.launch {
            try {
                val items = repository.fetchItems()
                mutableUiStateFlow.value = UiState.ShowContent(ContentType.ShowData(items))
            } catch (e: Exception) {
                mutableUiStateFlow.value = UiState.Error(Error.ConnectionError("Connection Error"))
            }
        }
    }
}

class ItemViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

internal sealed interface ContentType : BaseType {
    data class ShowData(val list: List<Item>): ContentType
}

internal sealed interface Error : BaseErrorType {
    data class ConnectionError(val message: String): Error
}