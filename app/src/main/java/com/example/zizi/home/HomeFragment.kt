package com.example.zizi.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zizi.DBKey.Companion.DB_ARTICLES
import com.example.zizi.R
import com.example.zizi.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding: FragmentHomeBinding? = null
    private lateinit var articleAdapter:ArticleAdapter
    private lateinit var articleDB: DatabaseReference

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object:ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    private val auth: FirebaseAuth by lazy{
        Firebase.auth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleList.clear()

        articleDB = Firebase.database.reference.child(DB_ARTICLES)  // 디비를 가지고 옴



        articleAdapter = ArticleAdapter()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter


        fragmentHomeBinding.addFloatingButton.setOnClickListener{
            if (auth.currentUser != null) {
                val intent = Intent(
                    requireContext(),
                    AddArticleActivity::class.java
                )  // context는 null 값도 허용해 줘서 requireContext사용함
                startActivity(intent)
            }else{
                Snackbar.make(view,"로그인 후 사용해주세요",Snackbar.LENGTH_LONG).show()

            }
        }


        articleDB.addChildEventListener(listener)  // 에드 리스너는 일회성/즉시성, 에드 차일드는 한 번 등록하면 계속 이벤트가 발생할 때 마다 등록이 된다.
        //view create 가 될 때마다 attack해주고 view destory될 때 마다 remove를 해준다
    }


    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()  //뷰를 다시 그려주는 것

    }

    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)
    }
}