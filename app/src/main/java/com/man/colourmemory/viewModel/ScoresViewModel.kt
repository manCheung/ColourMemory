package com.man.colourmemory.viewModel

import androidx.lifecycle.*
import com.man.colourmemory.db.DatabaseRepository
import com.man.colourmemory.db.DatabaseRepositoryImp
import com.man.colourmemory.db.ScoreEntity
import com.man.colourmemory.model.ScoreRecordItem
import kotlinx.coroutines.launch

class ScoresViewModel(
    private val roomDatabaseRepository: DatabaseRepository
) : ViewModel() {

    val scoreRecordList: MutableLiveData<List<ScoreRecordItem>> = MutableLiveData()

    fun getScoreRecord() {

        roomDatabaseRepository.getScores(object : DatabaseRepositoryImp.ItemCallback {
            override fun onItemsResult(items: List<ScoreRecordItem>) {
                viewModelScope.launch {
                    scoreRecordList.value = items
                }
            }
        })

    }

}