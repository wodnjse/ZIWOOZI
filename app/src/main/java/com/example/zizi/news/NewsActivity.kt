//package com.example.zizi.news
//
//import com.example.zizi.mypage.MyPageFragment
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.viewpager.widget.PagerAdapter
//import com.example.zizi.R
//
//class NewsActivity(private var context: Context, private val myModelArrayList: ArrayList<MyPageFragment>): PagerAdapter(){
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object`
//    }
//
//    override fun getCount(): Int {
//        return myModelArrayList.size
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val view = LayoutInflater.from(context).inflate(R.layout.news_card_item, container, false)
//
//        val model = myModelArrayList[position]
//        val title = model.title
//        val description = model.description
//        val date = model.date
//        val image = model.image
//
//        view.bannerImg.setImageResource(image)
//
//
//
//
//        return super.instantiateItem(container, position)
//
//    }
//
//}