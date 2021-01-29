package top.mikoto.sangnam.presentation.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.mikoto.sangnam.R
import top.mikoto.sangnam.presentation.viewmodels.SongListViewModel

class SongListFragment : Fragment() {

    companion object {
        fun newInstance() = SongListFragment()
    }

    private lateinit var viewModel: SongListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.song_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SongListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}