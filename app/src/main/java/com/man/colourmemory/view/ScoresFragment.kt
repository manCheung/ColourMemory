package com.man.colourmemory.view

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.man.colourmemory.R
import com.man.colourmemory.adapter.ScoreRecordAdapter
import com.man.colourmemory.db.DatabaseRepository
import com.man.colourmemory.db.RoomDbHelper
import com.man.colourmemory.db.ScoreEntity
import com.man.colourmemory.factory.ScoreViewModelFactory
import com.man.colourmemory.viewModel.ScoresViewModel
import kotlinx.android.synthetic.main.scores_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScoresFragment : Fragment() {

    companion object {
        fun newInstance() = ScoresFragment()
    }

    private lateinit var viewModel: ScoresViewModel
    private lateinit var scoreRecordAdapter: ScoreRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scores_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        loading_bar.visibility = View.VISIBLE

        viewModel =
            ViewModelProvider(
                this,
                ScoreViewModelFactory(DatabaseRepository(RoomDbHelper(this.requireContext())))
            ).get(
                ScoresViewModel::class.java
            )

        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_scores.layoutManager = layoutManager
        scoreRecordAdapter = ScoreRecordAdapter(viewModel)
        rv_scores.adapter = scoreRecordAdapter

        viewModel.scoreRecordList.observe(this.viewLifecycleOwner, Observer {
            loading_bar.visibility = View.GONE
            if(it.isNotEmpty()){
                tv_no_score_record.visibility = View.GONE
            }else{
                tv_no_score_record.visibility = View.VISIBLE
            }
            scoreRecordAdapter.setListRecord(it)
            scoreRecordAdapter.notifyDataSetChanged()
        })

        GlobalScope.launch {
            viewModel.getScoreRecord()
        }


    }

}