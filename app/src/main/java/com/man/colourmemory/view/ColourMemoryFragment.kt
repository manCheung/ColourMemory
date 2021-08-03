package com.man.colourmemory.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.man.colourmemory.R
import com.man.colourmemory.adapter.GridAdapter
import com.man.colourmemory.model.CardItemModel
import com.man.colourmemory.model.SelectedCardItemModel
import com.man.colourmemory.db.DatabaseRepository
import com.man.colourmemory.db.RoomDbHelper
import com.man.colourmemory.factory.ColourMemoryViewModelFactory
import com.man.colourmemory.viewModel.ColourMemoryViewModel
import kotlinx.android.synthetic.main.colour_memory_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ColourMemoryFragment : Fragment(), CardClickCallBackInterface{

    companion object {
        fun newInstance() = ColourMemoryFragment()
    }

    private var firstSelectedCardModelModel: SelectedCardItemModel<Int, CardItemModel>? = null
    private var secondSelectedCardModelModel: SelectedCardItemModel<Int, CardItemModel>? = null

    private lateinit var adapter: GridAdapter
    private lateinit var viewModel: ColourMemoryViewModel

    private var scoreNum = 0

    private val flippedCardsDurationTime by lazy {
        1000.toLong()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.colour_memory_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        viewModel =
            ViewModelProvider(
                this,
                ColourMemoryViewModelFactory(
                    DatabaseRepository(RoomDbHelper(this.requireContext()))
                )
            ).get(
                ColourMemoryViewModel::class.java
            )

        adapter = GridAdapter(this.requireContext(), viewModel, this)
        gridview_main.adapter = adapter

        viewModel.score.observe(this.viewLifecycleOwner, Observer { score ->
            tv_current_score.text = getString(R.string.score, score)
            scoreNum = score
        })

        viewModel.isWin.observe(this.viewLifecycleOwner, Observer { isWin ->
            if (isWin) {
                GlobalScope.launch(Dispatchers.IO) {
                    viewModel.getCurrentRank(scoreNum)
                    launch(Dispatchers.Main) {
                        showNameDialog(scoreNum, viewModel.currentRank)
                    }
                }
                tv_iswin.visibility = View.VISIBLE
//                showNameDialog(scoreNum, viewModel.currentRank)
//                tv_iswin.visibility = View.VISIBLE
            }else{
                tv_iswin.visibility = View.GONE
            }
        })

        viewModel.isDone.observe(this.viewLifecycleOwner, Observer { isDone ->
            if(isDone) {
                tv_iswin.visibility = View.VISIBLE
            }else{
                tv_iswin.visibility = View.GONE
            }
        })

        highScoresButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_colourMemoryFragment_to_scoresFragment)
        }

    }

    private fun checkCardsForMatch() {
        adapter.disableCardsClick()
        val firstCardWithPosition = firstSelectedCardModelModel ?: return
        val secondCardWithPosition = secondSelectedCardModelModel ?: return
        val firstCard = firstCardWithPosition.card
        val firstCardPosition = firstCardWithPosition.position
        val secondCard = secondCardWithPosition.card
        val secondCardPosition = secondCardWithPosition.position

        if (firstCard.imageSrc == secondCard.imageSrc) {
            // Cards matched
            Handler().postDelayed({
                adapter.markCardsAsMatchFound(firstCardPosition, secondCardPosition)
                clearSelectedCard()
                viewModel.matchCardFound()
                adapter.enableCardsClick()
            }, flippedCardsDurationTime)
        } else {
            // No match
            Handler().postDelayed({
                clearSelectedCard()
                viewModel.matchCardNotFound()
                adapter.flipCard(firstCardPosition)
                adapter.flipCard(secondCardPosition)
                adapter.enableCardsClick()
            }, flippedCardsDurationTime)
        }
    }

    private fun clearSelectedCard() {
        firstSelectedCardModelModel = null
        secondSelectedCardModelModel = null
    }

    private fun showNameDialog(score: Int, rank: Int) {
        val dialog = NameDialog(score, rank)
        val bundle = Bundle()

        dialog.arguments = bundle
        dialog.show(childFragmentManager, NameDialog::class.java.simpleName)
        dialog.setOnDialogClickListener(object : DialogClickListener {

            override fun onClick(userName: String) {
                if (userName.isNotEmpty()) {
                    GlobalScope.launch {
                        viewModel.insertScore(
                            name = userName,
                            score = scoreNum
                        )
                    }
                    viewModel.submittedScoreForm()
                    tv_iswin.visibility = View.VISIBLE
                    dialog.dismiss()
                } else {
                    showToastMessage(getString(R.string.name_space_error))
                }
            }

        })
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun CardClickCallBack(cardItemModel: CardItemModel, position: Int) {
        if (firstSelectedCardModelModel != null && secondSelectedCardModelModel != null) return
        // If the card is facing backwards
        if (!cardItemModel.isSelected) {
            adapter.flipCard(position)
            if (firstSelectedCardModelModel == null) {
                firstSelectedCardModelModel = SelectedCardItemModel(
                    position,
                    cardItemModel
                )
            } else {
                secondSelectedCardModelModel = SelectedCardItemModel(
                    position,
                    cardItemModel
                )
                checkCardsForMatch()
            }
        }
    }

}

interface CardClickCallBackInterface {

    fun CardClickCallBack(cardItemModel: CardItemModel, position: Int)

}