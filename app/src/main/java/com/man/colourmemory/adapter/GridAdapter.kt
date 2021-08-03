package com.man.colourmemory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.man.colourmemory.model.CardItemModel
import com.man.colourmemory.R
import com.man.colourmemory.view.CardClickCallBackInterface
import com.man.colourmemory.viewModel.ColourMemoryViewModel

class GridAdapter(
    private val context: Context,
    viewModel: ColourMemoryViewModel,
    private val cardClickListener: CardClickCallBackInterface
) :
    BaseAdapter() {

    private var isCardClickable = true

    var list: List<CardItemModel>? = viewModel.listMemoryCardLiveDataModel.value
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.cell_grid_item, null)
        }
        imageView = convertView!!.findViewById(R.id.imageView)

        // decide the card is front and back
        val cardResource = if(list!![position].isSelected) list!![position].imageSrc else R.drawable.card_bg

        imageView.setImageResource(cardResource)

//        Picasso.with(context)
//            .load(cardResource)
//            .placeholder(R.drawable.placeholder)
//            .error(R.drawable.error_image)
//            .into(imageView)

        /* Card's click functions*/
        if (!list!![position].isSelected && isCardClickable && !list!![position].isMatched) {
            imageView.isClickable = true
            imageView.isEnabled = true
            imageView.setOnClickListener {
                cardClickListener.CardClickCallBack(list!![position], position)
            }
        } else {
            imageView.isClickable = false
            imageView.isEnabled = false
        }

        /* if Card matched */
        if(list!![position].isMatched){
            imageView.visibility = View.INVISIBLE
        }else{
            imageView.visibility = View.VISIBLE
        }

        return convertView
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list?.count() ?: 0
    }

    fun disableCardsClick() {
        isCardClickable = false
        notifyDataSetChanged()
    }

    fun enableCardsClick() {
        isCardClickable = true
        notifyDataSetChanged()
    }

    fun flipCard(cardPosition: Int) {
        list!![cardPosition].apply {
            isSelected = !isSelected
        }
        notifyDataSetChanged()
    }

    fun markCardsAsMatchFound(card1Position: Int, card2Position: Int) {
        list!![card1Position].apply {
            isSelected = true
            isMatched = true
        }
        list!![card2Position].apply {
            isSelected = true
            isMatched = true
        }
        notifyDataSetChanged()
    }

}
