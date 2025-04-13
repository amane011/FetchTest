package com.example.fetchtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewAllFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var viewModel: ItemViewModel
    private var itemsList: List<Item>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ItemViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )

        // Attach an empty adapter initially
        adapter = ItemAdapter(emptyList())
        recyclerView.adapter = adapter
        viewModel.loadData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when(uiState) {
                        is UiState.Loading -> {

                        }

                        is UiState.ShowContent -> {
                            if (uiState.type is ContentType.ShowData) {
                                itemsList = uiState.type.list
                                showItems()
                            }
                        }

                        is UiState.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun showItems() {
        itemsList?.let {
            if (it.isEmpty()) {
                // Show empty state UI (e.g., a TextView or Toast)
                Toast.makeText(requireContext(), "No items available", Toast.LENGTH_SHORT).show()
            } else {
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )

                adapter = ItemAdapter(it.sortedBy { item -> item.listId })
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            }
        }
    }


    companion object {

    }
}