package com.example.onlinestore.presentation.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.onlinestore.R

class ImageSlideAdapter(
    private val context: Context,
    private var imageIds: List<Int>,
) : PagerAdapter() {

    override fun getCount(): Int {
        return imageIds.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        (container as ViewPager).removeView(view)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.view_product_image, null)
        val imageView = view.findViewById<View>(R.id.viewProductImage_imageView) as ImageView

        Glide.with(imageView)
            .load(context.getDrawable(imageIds[position]))
            .into(imageView)

        (container as ViewPager).addView(view, 0)
        return view
    }


}