package com.example.diaryapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_monthly_clicked.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DATE = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [MonthlyClickedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonthlyClickedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var date: String = ""
    private var getDetail : String? = null
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString(ARG_DATE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_clicked, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getDetail = runBlocking {

            database.collection(CollectionName.MY_DIARY.name)
                .document(date).get().await().get("detail").toString()

        }


        textViewDateMonthlyClicked.text = date

        if(getDetail == null){
            textViewDetailMonthlyClicked.text = ""
        }else{
            textViewDetailMonthlyClicked.text = getDetail
        }

        imageViewClose.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }
        imageViewDelete.setOnClickListener {
            deleteData()
        }
    }

    private fun deleteData() {
        if(getDetail != null){
            database.collection(CollectionName.MY_DIARY.name)
                .document(date).delete()
                .addOnSuccessListener {
                    //Log.d("TAG", "DocumentSnapshot successfully deleted!")
                    fragmentManager!!.beginTransaction().remove(this).commit()
                }
                .addOnFailureListener {
                    //  e -> Log.w("TAG", "Error deleting document", e)
                    error("削除できませんでした")
                }
        }else{
            Toast.makeText(this@MonthlyClickedFragment.context, "中身がありません", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MonthlyClickedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(date: String) =
            MonthlyClickedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATE, date)
                }
            }
    }
}
