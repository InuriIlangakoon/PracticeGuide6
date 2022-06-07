package com.example.practiceguide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practiceguide.adapters.PhotoAdapter
import com.example.practiceguide.api.RetrofitApiHandler
import com.example.practiceguide.database.AppDatabase
import com.example.practiceguide.databinding.FragmentFirstBinding
import com.example.practiceguide.models.Photo
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val retrofitAPIHandler = RetrofitApiHandler.create()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        val photos = retrofitAPIHandler.getPhotos()

        photos.enqueue(object : Callback<List<Photo>>{
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                val photosBody = response.body()

                //save data in database
                val db = AppDatabase.getDatabase(view.context)
                photosBody?.forEach {
                    db.photoDao().insertPhoto(it)
                }

                val adapter = PhotoAdapter(photosBody!!, this, {position->onListItemClick(position)})
                binding.recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Snackbar.make(view, "Failure in Callback", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                Log.i("TAG", "onFailure: Callback failed")

            }

        })

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onListItemClick(position:Int){
        Snackbar.make(requireView(), "Clicked on item ${position+1}", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        Log.i("TAG", "onListItemClick: $position clicked")
    }
}