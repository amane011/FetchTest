package com.example.fetchtest

import kotlin.random.Random

sealed interface UiState {
    data object Loading : UiState
    data class Error(
        val type: BaseErrorType? = null
    ) : UiState

    // Overriding equals and hashCode so StateFlow picks up the changes when the same ShowContent is emitted,
    // but the exact type value is different.
    data class ShowContent(val type: BaseType) : UiState {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as ShowContent
            return type == other.type
        }

        override fun hashCode(): Int {
            return Random.nextInt()
        }
    }
}
interface BaseType

interface BaseErrorType