package com.example.myapplication.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.data.datasource.remote.api.RecipeDTO

class DetailViewPagerPicsAdapter(val context: Context) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null

    var recipeImages = ArrayList<RecipeDTO.Steps>()

    override fun getCount(): Int {
        return recipeImages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.fragment_image_slide, null)

        val imageView = v.findViewById<ImageView>(R.id.iv_image_slide)
        //imageView.setImageURI(recipeImages[position])//TODO : 서버에서 URL 받아올 경우 setImageURL로 변경

        recipeImages[position].imageUrl?.let {
            if (it.isNotEmpty()) {
                Glide.with(App.instance)
                    .load(it)
                    .placeholder(R.drawable.ic_face)
                    .into(imageView);
            }
        }

        val viewPager = container as ViewPager
        viewPager.addView(v, 0)

        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }

    fun updateRecipeImage(recipesPics: ArrayList<RecipeDTO.Steps>) {
        recipeImages = recipesPics
    }
}