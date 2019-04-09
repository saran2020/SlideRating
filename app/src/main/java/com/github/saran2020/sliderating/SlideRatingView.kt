package com.github.saran2020.sliderating

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import java.util.*
import kotlin.math.ceil

class SlideRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var ratingSpace = 0.0f
    private var maxRating = 5
    private var currentRating = 0f
        set(value) {
            if (value > maxRating) {
                throw IllegalArgumentException("Rating cannot be more than max rating")
            } else if (value < 0) {
                throw IllegalArgumentException("Rating cannot be less than 0")
            }

            field = value
        }

    private var assetMap = sortedMapOf(
        0f to R.drawable.ic_star_empty,
        0.5f to R.drawable.ic_star_half,
        1f to R.drawable.ic_star_full
    )

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.SlideRatingView, 0, 0
            )

            ratingSpace = typedArray.getDimension(
                R.styleable.SlideRatingView_rating_space, 0f
            )
            maxRating = typedArray.getInt(
                R.styleable.SlideRatingView_max_rating, 5
            )
            currentRating = typedArray.getFloat(
                R.styleable.SlideRatingView_initial_rating, 0f
            )

            typedArray.recycle()
        }

        addViews()
    }

    private fun addViews() {
        for (i in 1..maxRating) {
            addRatingViews(i)
        }
    }

    private fun addRatingViews(pos: Int) {

        val imageView = getImageView(pos)
        addView(imageView)
    }

    private fun getImageView(pos: Int): ImageView {
        val imageView = ImageView(context)
        val layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        layoutParams.marginEnd = if (pos != maxRating) ratingSpace.toInt() else 0

        imageView.layoutParams = layoutParams
        imageView.tag = pos
        setRatingResource(imageView, pos)

        return imageView
    }

    private fun setRatingResource(imageView: ImageView, pos: Int) {
        imageView.setImageResource(
            when {
                pos < ceil(currentRating) -> assetMap[1f]!!
                pos == ceil(currentRating).toInt() -> assetMap[currentRating - (pos - 1)]!!
                else -> assetMap[0f]!!
            }
        )
    }
}