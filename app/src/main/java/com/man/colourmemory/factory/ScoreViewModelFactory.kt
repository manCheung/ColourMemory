package com.man.colourmemory.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.man.colourmemory.db.DatabaseRepository
import com.man.colourmemory.viewModel.ScoresViewModel

class ScoreViewModelFactory(
    private val databaseRepository: DatabaseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoresViewModel::class.java)) {
            return ScoresViewModel(databaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
