package com.zizi.apps.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zizi.apps.DBKey.Companion.DB_ARTICLES
import com.zizi.apps.R
import com.zizi.apps.databinding.FragmentHomeBinding

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

        articleDB = Firebase.database.reference.child(DB_ARTICLES)  // ????????? ????????? ???



        articleAdapter = ArticleAdapter()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter


        fragmentHomeBinding.addFloatingButton.setOnClickListener{
            if (auth.currentUser != null) {
                val intent = Intent(
                    requireContext(),
                    AddArticleActivity::class.java
                )  // context??? null ?????? ????????? ?????? requireContext?????????
                startActivity(intent)
            }else{
                Snackbar.make(view,"????????? ??? ??????????????????",Snackbar.LENGTH_LONG).show()

            }
        }


        articleDB.addChildEventListener(listener)  // ?????? ???????????? ?????????/?????????, ?????? ???????????? ??? ??? ???????????? ?????? ???????????? ????????? ??? ?????? ????????? ??????.
        //view create ??? ??? ????????? attack????????? view destory??? ??? ?????? remove??? ?????????
    }


    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()  //?????? ?????? ???????????? ???

    }

    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)
    }
}